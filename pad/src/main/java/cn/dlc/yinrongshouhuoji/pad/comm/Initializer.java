package cn.dlc.yinrongshouhuoji.pad.comm;

import android.app.Activity;
import android.serialport.SerialPort;
import android.text.TextUtils;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.SerialManager;

/**
 * 初始化器
 */
public class Initializer {

    private final Activity mActivity;
    private InitCallback mCallback;
    private final ConfigHelper mConfigHelper;
    private final SerialManager mSerialManager;

    public interface InitCallback {
        /**
         * 商家未登录
         */
        void noDeviceId();

        /**
         * 登录成功
         */
        void deviceIdOk();

        /**
         * 未配置过串口设备
         */
        void noSerialDeviceInit();

        /**
         * 串口未连接
         */
        void noSerialDeviceConnect();
    }

    public Initializer(Activity activity) {
        mActivity = activity;
        mConfigHelper = ConfigHelper.get();
        mSerialManager = SerialManager.get();
    }

    public void setCallback(InitCallback callback) {
        mCallback = callback;
    }

    public void check() {

        if (TextUtils.isEmpty(mConfigHelper.getDeviceId())) {
            if (mCallback != null) {
                mCallback.noDeviceId();
            }
            return;
        } else {
            if (mCallback != null) {
                mCallback.deviceIdOk();
            }
        }

        if (!mConfigHelper.isDeviceInited()) {
            if (mCallback != null) {
                mCallback.noSerialDeviceInit();
            }
            return;
        }

        SerialPort serialPort = mSerialManager.openSelectedDevice();
        if (serialPort == null) {
            if (mCallback != null) {
                mCallback.noSerialDeviceConnect();
            }
            return;
        }
    }
}
