package cn.dlc.yinrongshouhuoji.pad.comm.serial;

import android.serialport.SerialPort;
import cn.dlc.yinrongshouhuoji.pad.comm.ConfigHelper;
import cn.dlc.yinrongshouhuoji.pad.comm.DoorManager;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.Command;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.GoodsQueryCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockOpenCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockQueryCmd;
import cn.dlc.yinrongshouhuoji.pad.utils.rx.CompleteObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * 串口管理器
 */
public class SerialManager {

    private static final String TAG = "SerialManager";

    private static volatile SerialManager sManager = null;

    public static SerialManager get() {

        SerialManager manager = sManager;
        if (manager == null) {
            synchronized (SerialManager.class) {
                manager = sManager;
                if (manager == null) {
                    manager = new SerialManager();
                    sManager = manager;
                }
            }
        }
        return manager;
    }

    private SerialHandler mSerialHandler;

    private SerialManager() {
        mSerialHandler = new SerialHandler();
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
        return mSerialHandler.getSerialPort() != null;
    }

    /**
     * 打开串口
     *
     * @param devicePath
     * @param baudrate
     * @return
     */
    public SerialPort open(String devicePath, int baudrate) {
        return mSerialHandler.openSerial(devicePath, baudrate);
    }

    /**
     * 关闭串口
     */
    public void closeSerial() {
        mSerialHandler.closeSerial();
    }

    /**
     * 释放资源
     */
    public void release() {
        mSerialHandler.release();
        sManager = null;
    }

    /**
     * 打开已配置好的串口设备
     */
    public SerialPort openSelectedDevice() {

        SerialPort serialPort = mSerialHandler.getSerialPort();

        if (serialPort == null) {
            String path = ConfigHelper.get().getSelectedDevicePath();
            int baudrate = ConfigHelper.get().getSelectedBaudrate();
            return open(path, baudrate);
        }

        return serialPort;
    }

    /**
     * 发送数据
     *
     * @return
     */
    public void sendCommand(Command command) {
        mSerialHandler.send(command);
    }

    /**
     * 查询锁状态
     *
     * @param doorNum
     * @return
     */
    public Observable<List<Command>> queryLock(int doorNum) {

        return mSerialHandler
            // 发送查询锁命令
            .rxSend(new LockQueryCmd(doorNum))
            // 更新仓口
            .doOnNext(new Consumer<List<Command>>() {
                @Override
                public void accept(List<Command> commands) throws Exception {
                    DoorManager.get().updateDoors(commands);
                }
            });
    }

    /**
     * 查询锁状态
     *
     * @param doorNum
     * @param runnable 查询完的结果
     */
    public void queryLock(int doorNum, final Runnable runnable) {

        queryLock(doorNum).observeOn(Schedulers.io())
            .subscribe(new CompleteObserver<List<Command>>() {
                @Override
                public void onComplete() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
    }

    /**
     * 查询仓口物品状态
     *
     * @param doorNum
     */
    public Observable<List<Command>> queryGoods(int doorNum) {
        return mSerialHandler.rxSend(new GoodsQueryCmd(doorNum))
            .doOnNext(new Consumer<List<Command>>() {
                @Override
                public void accept(List<Command> commands) throws Exception {
                    // 更新仓口
                    DoorManager.get().updateDoors(commands);
                }
            });
    }

    /**
     * 查询仓口物品状态
     *
     * @param doorNum
     * @param runnable
     */
    public void queryGoods(int doorNum, final Runnable runnable) {

        queryGoods(doorNum).observeOn(Schedulers.io())
            .subscribe(new CompleteObserver<List<Command>>() {
                @Override
                public void onComplete() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
    }

    /**
     * 查询锁和物品状态
     *
     * @param doorNum
     */
    public Observable<List<Command>> queryLockAndGoods(final int doorNum) {
        return mSerialHandler.rxSend(new LockQueryCmd(doorNum))
            .flatMap(new Function<List<Command>, ObservableSource<List<Command>>>() {
                @Override
                public ObservableSource<List<Command>> apply(List<Command> commands)
                    throws Exception {
                    return mSerialHandler.rxSend(new GoodsQueryCmd(doorNum), commands);
                }
            })
            .doOnNext(new Consumer<List<Command>>() {
                @Override
                public void accept(List<Command> commands) throws Exception {
                    // 更新仓口
                    DoorManager.get().updateDoors(commands);
                }
            });
    }

    /**
     * 查询锁和物品状态
     *
     * @param doorNum
     * @param runnable
     */
    public void queryLockAndGoods(final int doorNum, final Runnable runnable) {
        queryLockAndGoods(doorNum).observeOn(Schedulers.io())
            .subscribe(new CompleteObserver<List<Command>>() {
                @Override
                public void onComplete() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
    }

    /**
     * 打开锁，并查询锁状态
     *
     * @param doorNum
     */
    public Observable<List<Command>> openAndQueryLock(final int doorNum) {

        return mSerialHandler
            // 发送打开锁命令
            .rxSend(new LockOpenCmd(doorNum))
            // 查询锁命令
            .flatMap(new Function<List<Command>, ObservableSource<List<Command>>>() {
                @Override
                public ObservableSource<List<Command>> apply(List<Command> commands)
                    throws Exception {

                    return mSerialHandler.rxSend(new LockQueryCmd(doorNum));
                }
            })
            // 更新仓口
            .doOnNext(new Consumer<List<Command>>() {
                @Override
                public void accept(List<Command> commands) throws Exception {

                    DoorManager.get().updateDoors(commands);
                }
            });
    }

    /**
     * 打开锁，并查询锁状态
     *
     * @param doorNum
     * @param runnable
     */
    public void openAndQueryLock(final int doorNum, final Runnable runnable) {
        openAndQueryLock(doorNum).observeOn(Schedulers.io())
            .subscribe(new CompleteObserver<List<Command>>() {
                @Override
                public void onComplete() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
    }

    /**
     * 打开锁并查询物品状态
     *
     * @param doorNum
     */
    public Observable<List<Command>> openAndQueryGoods(final int doorNum) {
        return mSerialHandler
            // 发送打开锁命令
            .rxSend(new LockOpenCmd(doorNum))
            // 查询物品命令
            .flatMap(new Function<List<Command>, ObservableSource<List<Command>>>() {
                @Override
                public ObservableSource<List<Command>> apply(List<Command> commands)
                    throws Exception {

                    return mSerialHandler.rxSend(new GoodsQueryCmd(doorNum));
                }
            })
            // 更新仓口
            .doOnNext(new Consumer<List<Command>>() {
                @Override
                public void accept(List<Command> commands) throws Exception {

                    DoorManager.get().updateDoors(commands);
                }
            });
    }

    /**
     * 打开锁并查询物品状态
     *
     * @param doorNum
     * @param runnable
     */
    public void openAndQueryGoods(final int doorNum, final Runnable runnable) {

        openAndQueryGoods(doorNum).observeOn(Schedulers.io())
            .subscribe(new CompleteObserver<List<Command>>() {
                @Override
                public void onComplete() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
    }

    /**
     * 打开锁并查询锁状态和物品状态
     *
     * @param doorNum
     */
    public Observable<List<Command>> openAndQueryLockGoods(final int doorNum) {
        return mSerialHandler
            // 发送打开锁命令
            .rxSend(new LockOpenCmd(doorNum))
            // 查询锁命令
            .flatMap(new Function<List<Command>, ObservableSource<List<Command>>>() {
                @Override
                public ObservableSource<List<Command>> apply(List<Command> commands)
                    throws Exception {

                    return mSerialHandler.rxSend(new LockQueryCmd(doorNum));
                }
            })
            // 查询物品命令
            .flatMap(new Function<List<Command>, ObservableSource<List<Command>>>() {
                @Override
                public ObservableSource<List<Command>> apply(List<Command> commands)
                    throws Exception {

                    return mSerialHandler.rxSend(new GoodsQueryCmd(doorNum), commands);
                }
            })
            // 更新仓口
            .doOnNext(new Consumer<List<Command>>() {
                @Override
                public void accept(List<Command> commands) throws Exception {

                    DoorManager.get().updateDoors(commands);
                }
            });
    }

    /**
     * 打开锁并查询锁状态和物品状态
     *
     * @param doorNum
     * @param runnable
     */
    public void openAndQueryLockGoods(final int doorNum, final Runnable runnable) {
        openAndQueryLockGoods(doorNum).observeOn(Schedulers.io())
            .subscribe(new CompleteObserver<List<Command>>() {
                @Override
                public void onComplete() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
    }
}
