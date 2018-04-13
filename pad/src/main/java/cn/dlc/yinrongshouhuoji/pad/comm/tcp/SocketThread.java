package cn.dlc.yinrongshouhuoji.pad.comm.tcp;

import android.os.HandlerThread;
import android.os.SystemClock;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by John on 2018/4/9.
 */

public class SocketThread extends Thread implements SocketClient {

    private TcpClient mTcpClient;

    protected final String mHost;
    protected final int mPort;
    protected Socket mSocket;

    protected BufferedInputStream mInputStream;
    protected BufferedOutputStream mOutputStream;

    protected long mLastReveTime;
    private HandlerThread mHandlerThread;

    public SocketThread(TcpClient tcpClient) {
        mTcpClient = tcpClient;
        mHost = tcpClient.getHost();
        mPort = tcpClient.getPort();
    }

    @Override
    public void run() {

        mLastReveTime = SystemClock.uptimeMillis();

        try {
            mSocket = new Socket();
            InetAddress inetAddress = InetAddress.getByName(mHost);
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, mPort);
            mSocket.connect(socketAddress, 3000);

            mInputStream = new BufferedInputStream(mSocket.getInputStream());
            mOutputStream = new BufferedOutputStream(mSocket.getOutputStream());
            afterConnected(mSocket, mInputStream, mOutputStream);

            byte[] bytes = new byte[4096];

            int length;
            while (!Thread.currentThread().isInterrupted()) {
                if (mInputStream.available() > 0) {
                    length = mInputStream.read(bytes);
                    if (length > 0) {
                        mLastReveTime = SystemClock.uptimeMillis();
                        onReceived(bytes, length);
                    } else if (length == -1) {
                        break;
                    }
                } else {
                    SystemClock.sleep(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release();
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        mHandlerThread = new HandlerThread("send-thread");
        mHandlerThread.start();
    }

    public long getLastReceiveTimeOffset() {
        return SystemClock.uptimeMillis() - mLastReveTime;
    }

    private void sendBytes(byte[] bytes, int length) {
        try {
            mOutputStream.write(bytes, 0, length);
            mOutputStream.flush();
        } catch (NullPointerException e) {
            // 不管
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(byte[] bytes) {
        send(bytes, bytes.length);
    }

    @Override
    public void send(final byte[] bytes, final int length) {

        if (Thread.currentThread() == this) {
            sendBytes(bytes, length);
        } else if (mHandlerThread != null) {
            Completable.fromRunnable(new Runnable() {
                @Override
                public void run() {
                    sendBytes(bytes, length);
                }
            }).subscribeOn(AndroidSchedulers.from(mHandlerThread.getLooper())).subscribe();
        }
    }

    @Override
    public void send(String string) {
        send(string.getBytes());
    }

    @Override
    public void afterConnected(Socket socket, BufferedInputStream inputStream,
        BufferedOutputStream outputStream) {
        mTcpClient.afterConnected(socket, inputStream, outputStream);
    }

    @Override
    public void onReceived(byte[] bytes, int length) {
        mTcpClient.onReceived(bytes, length);
    }

    @Override
    public void release() {
        close();
        interrupt();
        mHandlerThread.quitSafely();
        mTcpClient = null;
    }

    private void close() {
        try {
            mSocket.close();
        } catch (NullPointerException e) {
            // 不管
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mInputStream.close();
        } catch (NullPointerException e) {
            // 不管
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mOutputStream.close();
        } catch (NullPointerException e) {
            // 不管
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
