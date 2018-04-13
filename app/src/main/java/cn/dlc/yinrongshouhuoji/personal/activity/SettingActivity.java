package cn.dlc.yinrongshouhuoji.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.commonlibrary.utils.ToastUtil;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.personal.utill.CacheUtil;
import cn.dlc.yinrongshouhuoji.personal.view.UpdateDialog;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.setting_clean_rl)
    RelativeLayout mSettingCleanRl;
    @BindView(R.id.setting_update_rl)
    RelativeLayout mSettingUpdateRl;
    @BindView(R.id.setting_we_rl)
    RelativeLayout mSettingWeRl;
    @BindView(R.id.setting_clean_numb_tv)
    TextView mSettingCleanNumbTv;
    @BindView(R.id.setting_update_numb_tv)
    TextView mSettingUpdateNumbTv;
    @BindView(R.id.setting_update_passwork_rl)
    RelativeLayout mSettingUpdatePassworkRl;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    @OnClick({ R.id.setting_clean_rl, R.id.setting_update_rl, R.id.setting_we_rl ,R.id.setting_update_passwork_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //修改密码
            case R.id.setting_update_passwork_rl:
                startActivity(UpdataPassworkActivity.class);
                break;
            //清理缓存
            case R.id.setting_clean_rl:
                CacheUtil.clearAllCache(this);
                try {
                    mSettingCleanNumbTv.setText(CacheUtil.getTotalCacheSize(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //版本跟新
            case R.id.setting_update_rl:
                UpdateDialog Dialog = new UpdateDialog(this);
                Dialog.setOnCallBackListener(new UpdateDialog.OnCallBack() {
                    @Override
                    public void update() {
                        ToastUtil.show(SettingActivity.this, "已经是最新版本！");
                    }
                });
                break;
            //关于我们
            case R.id.setting_we_rl:
                startActivity(AboutUsActivity.class);
                break;
        }
    }

}
