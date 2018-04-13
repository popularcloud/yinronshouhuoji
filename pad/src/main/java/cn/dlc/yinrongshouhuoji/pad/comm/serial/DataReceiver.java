package cn.dlc.yinrongshouhuoji.pad.comm.serial;

/**
 * Created by John on 2018/3/29.
 */

public interface DataReceiver {

    /**
     * 有效数据回调
     */
    interface ValidDataCallback {
        /**
         * 当抽到有效数据时
         *
         * @param data
         */
        void onReceiveValidData(byte[] data);
    }

    /**
     * 接收数据
     *
     * @param data
     * @param length
     * @return
     */
    void receive(byte[] data, int length);

    /**
     * 接收数据
     *
     * @param data
     */
    void receive(byte data);

    /**
     * 清除缓存
     */
    void resetCache();

    ValidDataCallback getValidDataCallback();

    void setValidDataCallback(ValidDataCallback callback);
}
