package cn.dlc.yinrongshouhuoji.pad.comm.serial.old;

import android.os.HandlerThread;
import android.serialport.SerialPort;
import cn.dlc.yinrongshouhuoji.pad.utils.ByteUtil;
import cn.dlc.yinrongshouhuoji.pad.comm.ConfigHelper;
import com.licheedev.myutils.LogPlus;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 串口管理器
 */
public class OldSerialManager implements OldDataReceiver.ValidDataCallback {

    private static final String TAG = "OldSerialManager";

    private final OldDataReceiver mReceiver;

    private OldSerialReadThread mReadThread;
    private OutputStream mOutputStream;

    private HandlerThread mSendThread;
    private Scheduler mSendScheduler;

    private HandlerThread mReceiveThread;
    private Scheduler mReceiveScheduler;
    private final OldDataDispatcher mDispatcher;

    private static class InstanceHolder {

        public static OldSerialManager
            sManager = new OldSerialManager();
    }

    public static OldSerialManager get() {
        return InstanceHolder.sManager;
    }

    private SerialPort mSerialPort;

    private OldSerialManager() {

        // 数据接收器
        mReceiver = new OldSerialDataReceiver();
        mReceiver.setValidDataCallback(this);

        // 数据分发
        mDispatcher = new OldDataDispatcher();
    }

    /**
     * 是否已经配置过串口设备
     *
     * @return
     */
    public boolean isDeviceInited() {
        return ConfigHelper.get().isDeviceInited();
    }

    /**
     * 是否已经打开了窗口设备
     *
     * @return
     */
    public boolean isDevicedOpened() {
        return mSerialPort != null;
    }

    /**
     * 打开串口
     *
     * @param devicePath
     * @param baudrate
     * @return
     */
    public SerialPort open(String devicePath, int baudrate) {

        if (mSerialPort != null) {
            close();
        }

        try {
            File device = new File(devicePath);
            mSerialPort = new SerialPort(device, baudrate, 0);

            mReadThread = new OldSerialReadThread(mSerialPort.getInputStream(), mReceiver);
            mReadThread.start();

            mOutputStream = mSerialPort.getOutputStream();

            mSendThread = new HandlerThread("send-thread");
            mSendThread.start();
            mSendScheduler = AndroidSchedulers.from(mSendThread.getLooper());

            mReceiveThread = new HandlerThread("receive-thread");
            mReceiveThread.start();
            mReceiveScheduler = AndroidSchedulers.from(mReceiveThread.getLooper());

            return mSerialPort;
        } catch (Throwable tr) {
            LogPlus.w(TAG, "打开串口失败", tr);
            close();
            return null;
        }
    }

    /**
     * 打开已配置好的串口设备
     */
    public SerialPort openSelectedDevice() {

        if (mSerialPort == null) {
            String path = ConfigHelper.get().getSelectedDevicePath();
            int baudrate = ConfigHelper.get().getSelectedBaudrate();
            return open(path, baudrate);
        }

        return mSerialPort;
    }

    /**
     * 收到有效数据
     *
     * @param data
     */
    @Override
    public void onReceiveValidData(final byte[] data) {
        
        LogPlus.e("收到数据="+ByteUtil.bytes2HexStr(data));

        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                mDispatcher.dispatch(data);
            }
        }).subscribeOn(mReceiveScheduler).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                // 空实现
            }

            @Override
            public void onComplete() {
                // 空实现
            }

            @Override
            public void onError(Throwable e) {
                // 空实现
            }
        });
    }

    /**
     * 关闭串口
     */
    public void close() {
        if (mReadThread != null) {
            mReadThread.close();
        }

        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mSendThread != null) {
            mSendThread.quitSafely();
        }

        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
     * 发送数据
     *
     * @param datas
     * @return
     */
    private void sendData(byte[] datas) throws Exception {
        mOutputStream.write(datas);
    }

    /**
     * (rx包裹)发送数据
     *
     * @param datas
     * @return
     */
    private Observable<Object> rxSendData(final byte[] datas) {

        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    sendData(datas);
                    emitter.onNext(new Object());
                } catch (Exception e) {

                    LogPlus.e("发送：" + ByteUtil.bytes2HexStr(datas) + " 失败", e);

                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                        return;
                    }
                }
                emitter.onComplete();
            }
        });
    }

    public void sendOnIo(byte[] datas) {

        if (mSerialPort == null || mSendScheduler == null) {
            return;
        }

        rxSendData(datas).subscribeOn(mSendScheduler).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {
                LogPlus.e("发送失败", e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
