package cn.dlc.yinrongshouhuoji.pad.activity;

import android.os.Bundle;

/**
 * Created by John on 2018/4/4.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(MainActivity.class);
        finish();
    }
}
