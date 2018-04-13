package cn.dlc.yinrongshouhuoji.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.bean.DeviceListBean;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.ButtonDialog;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.EditTextDialog;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TwoButtonDialog;
import cn.dlc.yinrongshouhuoji.https.Config;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class DeviceControlActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.fl_query)
    FrameLayout mFlQuery;
    @BindView(R.id.fl_set_take_out)
    FrameLayout mFlSetTakeOut;
    @BindView(R.id.fl_clean_device)
    FrameLayout mFlCleanDevice;
    @BindView(R.id.fl_man_made)
    FrameLayout mFlManMade;

    private static final String EXTRA_BEAN = "extra_bean";

    private SaleMachineListBean.DataBean mDeviceListBean;

    public static Intent newIntent(Context mContext, SaleMachineListBean.DataBean mDeviceListBean) {
        Intent mIntent = new Intent(mContext, DeviceControlActivity.class);
        mIntent.putExtra(EXTRA_BEAN, mDeviceListBean);
        mDeviceListBean = mDeviceListBean;
        return mIntent;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_device_control;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveIntent();
        initTitleBar();
    }

    private void resolveIntent() {
        mDeviceListBean = (SaleMachineListBean.DataBean) getIntent().getSerializableExtra(EXTRA_BEAN);
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
        mTitleBar.setTitle(mDeviceListBean.getTitle() + "");
    }

    @OnClick({R.id.fl_query, R.id.fl_set_take_out, R.id.fl_clean_device, R.id.fl_man_made})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_query://查询
                startActivity(DeviceDetailActivity.newIntent(this, mDeviceListBean));
                break;
            case R.id.fl_set_take_out://放置外卖
                Intent intent = new Intent(this, SetTakeOutActivity.class);
                intent.putExtra("data", mDeviceListBean);
                startActivity(intent);
                break;
            case R.id.fl_clean_device://清机
                showCleanDeviceDialog();
                break;
            case R.id.fl_man_made://手动操作
                Intent intent_hander = new Intent(this, ManMadeActivity.class);
                intent_hander.putExtra("data", mDeviceListBean);
                startActivity(intent_hander);
                break;
        }
    }

    private void showCleanDeviceDialog() {//是否打开所有仓门？
        TwoButtonDialog mCleanDeviceDialog = new TwoButtonDialog(this);
        mCleanDeviceDialog.setTitle(R.string.tishi);
        mCleanDeviceDialog.setContent(R.string.shifoudakaisuoyoucangmeng);
        mCleanDeviceDialog.setBtnConfirm(R.string.queding);
        mCleanDeviceDialog.setBtnCancel(R.string.tiaoguo);
        mCleanDeviceDialog.setOnClickButtonListener(new TwoButtonDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                showEditFaultNoDialog();
            }

            @Override
            public void onClickCancel() {
                showSkipDialog();
            }
        });
        mCleanDeviceDialog.show();
    }

    private void showEditFaultNoDialog() {//配售员查选该售卖机仓门状态
        final EditTextDialog mEditFaultNoDialog = new EditTextDialog(this);
        mEditFaultNoDialog.setTitle(R.string.tishi);
        mEditFaultNoDialog.setContent(R.string.dakaicangmen);
        mEditFaultNoDialog.setEditTextHint(R.string.qingshuruguzhanggekoubianhao);
        mEditFaultNoDialog.setBtnConfirm(R.string.queding);
        mEditFaultNoDialog.setOnClickButtonListener(new EditTextDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                showOneToast(mEditFaultNoDialog.mEditText.getText().toString());
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("api_name", "submitOpenErrBox");
                dataMap.put("token", Config.token);
                dataMap.put("cupboard_id",mDeviceListBean.getCupboard_id()+"");
                dataMap.put("box_number",mEditFaultNoDialog.mEditText.getText().toString());
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
                showCloseAllDialog();
            }
        });
        mEditFaultNoDialog.show();
    }

    private void showCloseAllDialog() {//请关闭所有仓门
        ButtonDialog mCloseAllDialog = new ButtonDialog(this);
        mCloseAllDialog.setTitle(R.string.tishi);
        mCloseAllDialog.setContent(R.string.qingguangbisuoyoucangmen);
        mCloseAllDialog.setBtnConfirm(R.string.queding);
        mCloseAllDialog.setOnClickButtonListener(new ButtonDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                showSkipDialog();
            }
        });
        mCloseAllDialog.show();
    }

    private void showSkipDialog() {//系统检测到
        ButtonDialog mSkipDialog = new ButtonDialog(this);
        mSkipDialog.setTitle(R.string.tishi);
        mSkipDialog.setContent(getString(R.string.qingjicuowu, "110、112"));
        mSkipDialog.setBtnConfirm(R.string.queding);
        mSkipDialog.setOnClickButtonListener(new ButtonDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                showCleanSuccessDialog();
            }
        });
        mSkipDialog.show();
    }

    private void showCleanSuccessDialog() {//清机成功
        ButtonDialog mCleanSuccessDialog = new ButtonDialog(this);
        mCleanSuccessDialog.setTitle(R.string.tishi);
        mCleanSuccessDialog.setContent(getString(R.string.qingjichenggong, "6"));
        mCleanSuccessDialog.setBtnConfirm(R.string.queding);
        mCleanSuccessDialog.show();
    }
}
