package cn.dlc.yinrongshouhuoji.pad.activity;

import android.os.Bundle;
import android.view.View;
import cn.dlc.commonlibrary.ui.base.BaseCommonActivity;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public abstract class BaseActivity extends BaseCommonActivity {

    @Override
    protected void beforeSetContentView() {
        enableFullScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView()
            .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    enableFullScreen();
                }
            });
    }

    /**
     * 开启全屏，隐藏状态栏和导航栏
     */
    protected void enableFullScreen() {
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        enableFullScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        enableFullScreen();
    }
}
