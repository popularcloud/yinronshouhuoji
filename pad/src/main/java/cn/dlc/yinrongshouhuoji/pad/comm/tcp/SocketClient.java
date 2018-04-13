package cn.dlc.yinrongshouhuoji.pad.comm.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * Created by John on 2018/4/9.
 */

public interface SocketClient {

    void send(byte[] bytes);

    void send(byte[] bytes, int length);

    void send(String string);

    void afterConnected(Socket socket, BufferedInputStream inputStream,
        BufferedOutputStream outputStream);
    
    void onReceived(byte[] bytes, int length);

    void release();
}
