package cn.dlc.yinrongshouhuoji.personal.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.personal.view.MyDialog;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class UpdataPassworkActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.username_jiu_passwork_edit)
    EditText mUsernameJiuPassworkEdit;
    @BindView(R.id.username_new_passwork_edit)
    EditText mUsernameNewPassworkEdit;
    @BindView(R.id.username_again_new_passwork_edit)
    EditText mUsernameAgainNewPassworkEdit;
    @BindView(R.id.updata_passwork_bt)
    Button mUpdataPassworkBt;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_updatapasswork;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    //确认修改密码
    @OnClick(R.id.updata_passwork_bt)
    public void onViewClicked() {
        MyDialog dialog = new MyDialog(this);
        //设置true或者false出现的dialog
        dialog.setIsTrue(true);
        dialog.setIsText(getResources().getString(R.string.xiugaichenggong));
        dialog.show();
    }
}
