package cn.dlc.yinrongshouhuoji.pad.comm.serial.old;

import android.os.SystemClock;
import com.licheedev.myutils.LogPlus;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读串口线程
 */
public class OldSerialReadThread extends Thread {

    private static final String TAG = "OldSerialReadThread";

    private BufferedInputStream mInputStream;
    private OldDataReceiver mReceiver;

    public OldSerialReadThread(InputStream is, OldDataReceiver receiver) {
        mInputStream = new BufferedInputStream(is);
        mReceiver = receiver;
    }

    @Override
    public void run() {
        byte[] received = new byte[1024];
        int size;

        LogPlus.e("开始读线程");

        while (true) {

            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {

                int available = mInputStream.available();

                if (available > 0) {
                    size = mInputStream.read(received);
                    if (size > 0) {
                        onDataReceive(received, size);
                    }
                } else {
                    SystemClock.sleep(1);
                }
            } catch (IOException e) {
                LogPlus.e("读取数据失败", e);
            }
        }

        LogPlus.e("结束读进程");
    }

    /**
     * 处理获取到的数据，解决粘包、分包等
     *
     * @param received
     * @param size
     */
    private void onDataReceive(byte[] received, int size) {
        //String hexStr = ByteUtil.bytes2HexStr(received, 0, size);
        //LogPlus.e("收到数据=" + hexStr);
        // 读取数据
        mReceiver.receive(received, size);
    }

    /**
     * 停止读线程
     */
    public void close() {

        try {
            mInputStream.close();
        } catch (IOException e) {
            LogPlus.e("异常", e);
        } finally {
            super.interrupt();
        }
    }
}
