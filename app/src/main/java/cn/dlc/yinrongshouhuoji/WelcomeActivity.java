package cn.dlc.yinrongshouhuoji;

import android.content.Intent;
import android.os.Bundle;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.activity.MainActivity;
import cn.dlc.yinrongshouhuoji.login.activity.LoginActivity;
import cn.dlc.yinrongshouhuoji.util.Constance;
import cn.dlc.yinrongshouhuoji.util.SPUtils;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填入你要启动的activity
        Constance.token = SPUtils.getString("token");
        if (!"".equals(Constance.token)){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else {
            startActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    protected int getLayoutID() {
        return 0;
    }
}
