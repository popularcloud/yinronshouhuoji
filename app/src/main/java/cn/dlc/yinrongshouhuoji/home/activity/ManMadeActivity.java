package cn.dlc.yinrongshouhuoji.home.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TwoButtonDialog;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.personal.view.MyDialog;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class ManMadeActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.edit_grid_no)
    EditText mEditGridNo;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private SaleMachineListBean.DataBean mDeviceListBean;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_man_made;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeviceListBean = (SaleMachineListBean.DataBean) getIntent().getSerializableExtra("data");
        initTitleBar();
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    @OnClick({ R.id.btn_confirm })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                confirm();
                break;
        }
    }

    private void confirm() {
        String gridNo = mEditGridNo.getText().toString();
        if (TextUtils.isEmpty(gridNo)) {
            showOneToast(R.string.toast_qingshurugezihao);
            return;
        }
        showOpenDialog(gridNo);
    }

    private void showOpenDialog(final String gridNo) {
        TwoButtonDialog mOpenDialog = new TwoButtonDialog(this);
        mOpenDialog.setTitle(R.string.tishi);
        mOpenDialog.setContent(getString(R.string.shifoukaiqi, gridNo));
        mOpenDialog.setBtnConfirm(R.string.queding);
        mOpenDialog.setBtnCancel(R.string.quxiao);
        mOpenDialog.setOnClickButtonListener(new TwoButtonDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("api_name", "openBoxes");
                dataMap.put("token", Config.token);
                dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
                dataMap.put("box_number", gridNo);
                doApiPost(Config.INTERFACE_BOX_API, dataMap);
            }

            @Override
            public void onClickCancel() {
            }
        });
        mOpenDialog.show();
    }

    private void showSuccessDialog() {
        MyDialog mSuccessDialog = new MyDialog(this);
        mSuccessDialog.setIsText(R.string.chenggongkaiqi);
        mSuccessDialog.setIsTrue(true);
        mSuccessDialog.show();
    }
}
