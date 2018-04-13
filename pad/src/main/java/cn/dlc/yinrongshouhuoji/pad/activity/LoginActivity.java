package cn.dlc.yinrongshouhuoji.pad.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.okgo.callback.Bean01Callback;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.pad.R;
import cn.dlc.yinrongshouhuoji.pad.comm.http.HttpApi;
import cn.dlc.yinrongshouhuoji.pad.comm.http.bean.BaseBean;
import com.licheedev.myutils.LogPlus;

/**
 * Created by John on 2018/4/4.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_get_code)
    Button mBtnGetCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    private CountDownTimer mCountDownTimer;
    private boolean isCountingDown;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleBar.leftExit(this);

        isCountingDown = false;

        mCountDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                LogPlus.e("tick=" + millisUntilFinished);
                mBtnGetCode.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                isCountingDown = false;
                mBtnGetCode.setText(R.string.fetch_code);
                mBtnGetCode.setEnabled(true);
            }
        };
    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
    }

    @OnClick({ R.id.btn_get_code, R.id.btn_login })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code: {
                String mobile = mEtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    showOneToast(R.string.shoujihaomabunengweikong);
                    return;
                }
                sendCode(mobile);
                break;
            }
            case R.id.btn_login: {
                login();
                break;
            }
        }
    }

    private void startCountDown() {
        if (!isCountingDown) {
            isCountingDown = true;
            mBtnGetCode.setEnabled(false);
            mCountDownTimer.start();
        }
    }

    private void sendCode(String mobile) {
        showWaitingDialog(R.string.zhengzaifasong, false);
        HttpApi.get().sendCode(mobile, new Bean01Callback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                dismissWaitingDialog();
                startCountDown();
                showOneToast(R.string.fasongchenggong);
            }

            @Override
            public void onFailure(String message, Throwable tr) {
                dismissWaitingDialog();
                showOneToast(message);
            }
        });
    }

    private void login() {
        String mobile = mEtPhone.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showOneToast(R.string.shoujihaomabunengweikong);
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showOneToast(R.string.yanzhengmabunengweikong);
            return;
        }

        showWaitingDialog("登记设备", false);
        HttpApi.get().login(mobile, code, new Bean01Callback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                dismissWaitingDialog();
                showOneToast("登记成功");
            }

            @Override
            public void onFailure(String message, Throwable tr) {
                dismissWaitingDialog();
                showOneToast(message);
            }
        });
    }
}
