package cn.dlc.yinrongshouhuoji.pad.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import cn.dlc.yinrongshouhuoji.pad.comm.CommService;

/**
 * Created by John on 2018/1/31.
 */

public abstract class BaseServiceActivity extends BaseActivity {

    private Intent mServiceIntent;
    private ServiceConnection mConn;

    /**
     * 连接用车服务服务
     */
    protected void connectCarService(boolean startService, final boolean bindService) {
        mServiceIntent = new Intent(this, CommService.class);

        if (startService) {
            startService(mServiceIntent);
        }

        if (bindService) {
            mConn = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    CommService.CommBinder binder = (CommService.CommBinder) service;
                    BaseServiceActivity.this.onServiceConnected(binder.getService());
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    BaseServiceActivity.this.onServiceDisconnected();
                }
            };
            bindService(mServiceIntent, mConn, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 解绑服务
     */
    public void unbindCommService() {
        if (mConn != null) {
            unbindService(mConn);
            mConn = null;
        }
        
    }

    protected abstract void onServiceConnected(CommService commService);

    protected abstract void onServiceDisconnected();
}
