package cn.dlc.yinrongshouhuoji.pad.comm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import cn.dlc.yinrongshouhuoji.pad.Information;
import cn.dlc.yinrongshouhuoji.pad.comm.tcp.MyTcpClient;
import cn.dlc.yinrongshouhuoji.pad.comm.tcp.TcpClient;

/**
 * Created by John on 2018/4/9.
 */

public class CommService extends Service {

    private TcpClient mTcpClient;

    public static class CommBinder extends Binder {

        private CommService mService;

        public CommBinder(CommService service) {
            mService = service;
        }

        public CommService getService() {
            return mService;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CommBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        mTcpClient = new MyTcpClient(Information.HOST, Information.PORT);
        mTcpClient.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
