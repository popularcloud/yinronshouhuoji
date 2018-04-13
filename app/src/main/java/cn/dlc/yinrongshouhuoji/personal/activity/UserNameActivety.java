package cn.dlc.yinrongshouhuoji.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.util.ToastUtil;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class UserNameActivety extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.username_edit)
    EditText mUsernameEdit;
    @BindView(R.id.username_bt)
    Button mUsernameBt;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    //确定
    @OnClick(R.id.username_bt)
    public void onViewClicked() {
        String username = mUsernameEdit.getText()+"";
        ToastUtil.showMessage(username);
        if ("".equals(username)){
            ToastUtil.showMessage("请填写用户名");
            return;
        }
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "modifyNickname");
        dataMap.put("token", Config.token);
        dataMap.put("nickname",username);
        doApiPost(Config.INTERFACE_MINE, dataMap);
        Intent intent = new Intent();
        intent.putExtra("name",mUsernameEdit.getText().toString());
        setResult(2,intent);
        finish();
    }
}
