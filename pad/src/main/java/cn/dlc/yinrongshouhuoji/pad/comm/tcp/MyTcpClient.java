package cn.dlc.yinrongshouhuoji.pad.comm.tcp;

import com.google.gson.Gson;
import com.licheedev.myutils.LogPlus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import cn.dlc.yinrongshouhuoji.pad.comm.model.OpenLattice;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockOpenCmd;

/**
 * Created by John on 2018/4/9.
 */

public class MyTcpClient extends TcpClient {

    public MyTcpClient(String host, int port) {
        super(host, port);
    }

    @Override
    public void afterConnected(Socket socket, BufferedInputStream inputStream,
        BufferedOutputStream outputStream) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("macno", mConfigHelper.getDeviceId());
            send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceived(byte[] bytes, int length) {
        //LogPlus.i(Arrays.toString(bytes));
        String string = new String(bytes, 0, length, Charset.forName("utf-8"));
        LogPlus.w("tcp_recv", string);

        if(!"1".equals(string)){
            OpenLattice res = new Gson().fromJson(string, OpenLattice.class);
            switch (res.getCmd()){
                case "call_app_to_open_door":
                    break;
                case "call_app_to_open_door_all":
                    break;
            }
/*            LockOpenCmd lockOpenCmd = new LockOpenCmd(res.getBox_number());
            mSerialManager.sendCommand(lockOpenCmd);*/
        }
    }

}
