package cn.dlc.yinrongshouhuoji.pad.comm.serial;

import android.os.HandlerThread;
import android.os.SystemClock;
import android.serialport.SerialPort;
import android.support.annotation.Nullable;
import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.Command;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.ConfirmCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.GoodsFeedbackCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockFeedbackCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.TempFeedbackCmd;
import cn.dlc.yinrongshouhuoji.pad.utils.ByteUtil;
import cn.dlc.yinrongshouhuoji.pad.utils.rx.EmptyObserver;
import com.licheedev.myutils.LogPlus;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 真正的串口操作
 */
public class SerialHandler implements DataReceiver.ValidDataCallback {

    /**
     * 接收数据超时阈值
     */
    public static final long RECV_TIME_OUT = Protocol.RECEIVE_TIME_OUT;
    public static final long RECV_INTERVAL_TIME_OUT = Protocol.RECEIVE_INTERVAL_TIME_OUT;

    private final HandlerThread mSerialThread;
    private final HandlerThread mDispatchThread;
    private final Scheduler mSerialScheduler;
    private final Scheduler mDispatchScheduler;

    private final DataReceiver mReceiver;
    private final byte[] mRecvBuffer;

    private BufferedInputStream mInputStream;
    private BufferedOutputStream mOutputStream;
    private SerialPort mSerialPort;

    /**
     * 接收到的命令集合
     */
    private List<Command> mRecvCommands;
    /**
     * 最后一次发送的命令
     */
    private Command mLastSentCommand;
    /**
     * 标识是否已经接收数据完毕，每次发送命令后都会重置为false
     */
    private boolean mRecvFinish;
    /**
     * 上次收到的有效数据，每次发送命令后都会重置为null
     */
    private byte[] mLastRecvBytes;

    public SerialHandler() {

        mReceiver = new SerialDataReceiver(1024);
        mReceiver.setValidDataCallback(this);

        mRecvBuffer = new byte[100];

        mSerialThread = new HandlerThread("serial-thread");
        mSerialThread.start();
        mSerialScheduler = AndroidSchedulers.from(mSerialThread.getLooper());

        mDispatchThread = new HandlerThread("dispatch-thread");
        mDispatchThread.start();
        mDispatchScheduler = AndroidSchedulers.from(mDispatchThread.getLooper());
    }

    /**
     * 打开串口
     *
     * @param devicePath
     * @param baudrate
     * @return
     */
    public synchronized SerialPort openSerial(String devicePath, int baudrate) {

        if (mSerialPort != null) {
            closeSerial();
        }

        try {
            mSerialPort = new SerialPort(devicePath, baudrate, 0);
            mInputStream = new BufferedInputStream(mSerialPort.getInputStream());
            mOutputStream = new BufferedOutputStream(mSerialPort.getOutputStream());
            return mSerialPort;
        } catch (Throwable tr) {
            closeSerial();
            LogPlus.w("打开串口失败", tr);
            return null;
        }
    }

    /**
     * 关闭串口
     */
    public synchronized void closeSerial() {
        try {
            mOutputStream.close();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            mOutputStream = null;
        }

        try {
            mInputStream.close();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            mInputStream = null;
        }

        try {
            mSerialPort.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSerialPort = null;
        }
    }

    /**
     * 获取打开的串口
     *
     * @return
     */
    public synchronized SerialPort getSerialPort() {
        return mSerialPort;
    }

    /**
     * 释放资源
     */
    public void release() {

        closeSerial();

        mSerialThread.quitSafely();
        mDispatchThread.quitSafely();
    }

    /**
     * 发送数据，已try catch
     *
     * @param bytes
     * @param off
     * @param len
     */
    private void trySend(byte[] bytes, int off, int len) {

        LogPlus.i("SerialHandler", "发送数据=" + ByteUtil.bytes2HexStr(bytes, 0, len));

        try {
            mOutputStream.write(bytes, off, len);
            mOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据，已try catch
     *
     * @param bytes
     */
    private void trySend(byte[] bytes) {
        trySend(bytes, 0, bytes.length);
    }

    /**
     * rx 发送并接受数据，已在专用的线程进行调度，需要自己进行订阅
     *
     * @param command 发送的命令
     * @param toRecvCommands 用来存储之后接收到的数据的集合，可以为null
     * @return 返回Observable，会发射出接收到的数据 List<Command>
     * @see ConfirmCmd
     * @see LockFeedbackCmd
     * @see GoodsFeedbackCmd
     * @see TempFeedbackCmd
     */
    public Observable<List<Command>> rxSend(final Command command,
        @Nullable final List<Command> toRecvCommands) {

        return Observable.create(new ObservableOnSubscribe<List<Command>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Command>> emitter) throws Exception {

                mLastRecvBytes = null; // 重置最新接收的数据
                mRecvFinish = false; // 重置接收完成状态
                mLastSentCommand = command; // 设置最后一次发送的命令

                mRecvCommands = toRecvCommands;

                if (mRecvCommands == null) {
                    mRecvCommands = new ArrayList<>();
                }

                // 先发送
                trySend(command.toBytes());

                long sendTime = SystemClock.uptimeMillis();
                // 记录一下发送后的时间，用来判断接收数据是否超时
                long recvTime = 0;
                int len;
                // 接收到的有效数据

                //LogPlus.i("disposed=" + emitter.isDisposed());

                while (!emitter.isDisposed()) {
                    // 先看看有没有可读数据，免得一直阻塞线程

                    try {
                        int available = mInputStream.available();

                        LogPlus.e("available=" + available);
                        
                        if (available > 0) {
                            
                            len = mInputStream.read(mRecvBuffer);
                            if (len > 0) {
                                // 接收并校验数据，有效数据在 onReceiveValidData()进行回调
                                mReceiver.receive(mRecvBuffer, len);
                                recvTime = SystemClock.uptimeMillis(); // 更新一下接收数据时间
                            }
                        } else {
                            // 没有数据就暂停一秒，以免出现CPU过度占用
                            SystemClock.sleep(1);
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        SystemClock.sleep(1);
                    }
                    // 检查数据是否接收完毕
                    mRecvFinish = checkReceiveFinish(sendTime, recvTime);

                    if (mRecvFinish) {
                        break;
                    }
                }

                LogPlus.e("SerialHandler", "结束了，door=" + command.getDoor());

                emitter.onNext(mRecvCommands);
                emitter.onComplete();
            }
        }).subscribeOn(mSerialScheduler);
    }

    /**
     * rx 发送并接受数据，已在专用的线程进行调度，需要自己进行订阅
     *
     * @param command 发送的命令
     * @return 返回Observable，会发射出接收到的数据 List<Command>
     * @see ConfirmCmd
     * @see LockFeedbackCmd
     * @see GoodsFeedbackCmd
     * @see TempFeedbackCmd
     */
    public Observable<List<Command>> rxSend(Command command) {
        return rxSend(command, null);
    }

    /**
     * 发送命令
     *
     * @param command
     */
    public void send(final Command command) {
        rxSend(command).subscribe(new EmptyObserver());
    }

    /**
     * 当收到有效的数据时
     *
     * @param data
     */
    @Override
    public void onReceiveValidData(byte[] data) {

        mLastRecvBytes = data;

        Command recvCommand = dispatch(data);
        if (recvCommand != null) {
            mRecvCommands.add(recvCommand);
        }

        @Protocol.Flag int flag = 0xff & data[2];
        if (flag == Protocol.FLAG_CONFIRM) { // 确认类型的命令，收到一条就可以了
            mRecvFinish = true;
        } else {
            // 发送的命令不是针对所有门的话，代表是单口操作，收到一条就可以了
            mRecvFinish = mLastSentCommand.getDoor() != Protocol.DOOR_ALL;
        }
    }

    /**
     * 检查接收到的数据,并在其他线程分发数据
     *
     * @param sendTime 发送命令的时间戳
     * @param recvTime 上次收到数据的时间戳
     * @return true：表示接收数据完毕；false:表示还需要继续接收数据
     */
    private boolean checkReceiveFinish(long sendTime, long recvTime) {

        // 已完成就直接返回
        if (mRecvFinish) {
            return true;
        }

        if (recvTime == 0) { // 表示一直没收到数据
            long sendOffset = SystemClock.uptimeMillis() - sendTime;
            return sendOffset > RECV_TIME_OUT;
        } else {
            // 有接收到过数据，但是距离上一个数据已经超时
            long recvOffset = SystemClock.uptimeMillis() - recvTime;
            return recvOffset > RECV_INTERVAL_TIME_OUT;
        }
    }

    private Command dispatch(byte[] bytes) {

        LogPlus.e("SerialHandler", "收到有效数据=" + ByteUtil.bytes2HexStr(bytes));

        @Protocol.Flag int flag = 0xff & bytes[2];
        switch (flag) {
            case Protocol.FLAG_LOCK_FEEDBACK:
                return new LockFeedbackCmd(bytes);
            case Protocol.FLAG_GOODS_FEEDBACK:
                return new GoodsFeedbackCmd(bytes);
            case Protocol.FLAG_TEMP_FEEDBACK_1:
            case Protocol.FLAG_TEMP_FEEDBACK_2:
            case Protocol.FLAG_TEMP_FEEDBACK_3:
                return new TempFeedbackCmd(bytes);
            case Protocol.FLAG_CONFIRM:
                return new ConfirmCmd(bytes);
            default:
                return null;
        }
    }
}
