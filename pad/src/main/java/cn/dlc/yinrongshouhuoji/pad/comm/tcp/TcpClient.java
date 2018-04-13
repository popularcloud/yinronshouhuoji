package cn.dlc.yinrongshouhuoji.pad.comm.tcp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import cn.dlc.yinrongshouhuoji.pad.comm.ConfigHelper;
import com.licheedev.myutils.LogPlus;

/**
 * Created by John on 2018/4/9.
 */

public abstract class TcpClient extends HandlerThread implements SocketClient {

    public static final int ACTION_EXIT_HEART_BEAT = 1;
    public static final int ACTION_CONNECT_SOCKET = 2;

    protected final String mHost;
    protected final int mPort;
    protected final ConfigHelper mConfigHelper;
    protected InnerHandler mHandler;

    private SocketThread mSocketThread;

    public TcpClient(String host, int port) {
        super("heart-beat-thread");
        mHost = host;
        mPort = port;
        mConfigHelper = ConfigHelper.get();
    }

    public String getHost() {
        return mHost;
    }

    public int getPort() {
        return mPort;
    }

    @Override
    public void send(byte[] bytes) {
        if (mSocketThread != null) {
            mSocketThread.send(bytes);
        }
    }

    @Override
    public void send(byte[] bytes, int length) {
        if (mSocketThread != null) {
            mSocketThread.send(bytes, length);
        }
    }

    @Override
    public void send(String string) {
        if (mSocketThread != null) {
            mSocketThread.send(string);
        }
    }

    public void release() {
        if (Thread.currentThread() != this) {
            mHandler.sendEmptyMessage(ACTION_EXIT_HEART_BEAT);
            return;
        }
        mHandler.removeCallbacks(mHeartRunnable);
        quitSafely();
        mHandler.tcpClient = null;
        closeSocket();
    }

    private void closeSocket() {
        if (mSocketThread != null) {
            mSocketThread.release();
            mSocketThread = null;
        }
    }

    private void connectSocket() {
        if (Thread.currentThread() != this) {
            mHandler.sendEmptyMessage(ACTION_CONNECT_SOCKET);
            return;
        }
        closeSocket();
        mSocketThread = new SocketThread(this);
        mSocketThread.start();
    }

    @Override
    public synchronized void start() {
        super.start();

        mHandler = new InnerHandler(getLooper(), this);
        mHandler.post(mHeartRunnable);
    }

    /**
     * 用来发送心跳和释放资源的Handler
     */
    private static class InnerHandler extends Handler {

        TcpClient tcpClient;

        InnerHandler(Looper looper, TcpClient tcpClient) {
            super(looper);
            this.tcpClient = tcpClient;
        }

        @Override
        public void handleMessage(Message msg) {
            if (this.tcpClient == null) {
                // Handler已经被释放
                return;
            }

            switch (msg.what) {
                case ACTION_EXIT_HEART_BEAT:
                    // 释放掉
                    tcpClient.release();
                    break;
                case ACTION_CONNECT_SOCKET:
                    tcpClient.connectSocket();
                    break;
            }
        }
    }

    /**
     * 检测心跳
     */
    private Runnable mHeartRunnable = new Runnable() {
        @Override
        public void run() {

            if (!TextUtils.isEmpty(mConfigHelper.getDeviceId())) {
                if (mSocketThread == null || mSocketThread.getLastReceiveTimeOffset() > 3000L) {
                    LogPlus.w("检测到未连接，或超过3秒没收到数据");
                    LogPlus.w("尝试重连");
                    mHandler.removeCallbacks(mHeartRunnable);
                    connectSocket();
                }
                mHandler.postDelayed(mHeartRunnable, 2000);
            } else {
                LogPlus.e("未配置商户信息");
                mHandler.postDelayed(mHeartRunnable, 1000);
            }
        }
    };
}
