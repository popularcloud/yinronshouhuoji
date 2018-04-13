package cn.dlc.yinrongshouhuoji.login.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.utils.CountDownHelper;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.activity.MainActivity;
import cn.dlc.yinrongshouhuoji.home.bean.loginBean;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.DialogHelper;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.util.ActivityInfo;
import cn.dlc.yinrongshouhuoji.util.SPUtils;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_check_code)
    EditText mEditCheckCode;
    @BindView(R.id.tv_get_check_code)
    TextView mTvGetCheckCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    String mPhone;
    String mCheckCode;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //mEditPhone.setText("18589077695");
       // mEditCheckCode.setText("8898");
    }

    @OnClick({R.id.tv_get_check_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_check_code:
                String code = getCheckCode();
                Map<String, String> map = new HashMap<>();
                if (!"".equals(code)) {
                    map.put("mobile", code);
                }
                showWaitDialog("正在发送验证码.");
                Config.api_name = Config.INTERFACE_SENDCODE_LOGIN;
                doApiPost(Config.api_name, map);
                break;
            case R.id.btn_login:
                boolean isLogin = login();
                if (isLogin) {
                    Map<String, String> loginMap = new HashMap<>();
                    loginMap.put("phone", mPhone);
                    loginMap.put("code", mCheckCode);
                    showWaitDialog("正在登录.");
                    Config.api_name = Config.INTERFACE_LOGIN;
                    doApiPost(Config.api_name, loginMap);
                }
                break;
        }
    }

    private String getCheckCode() {
        mPhone = mEditPhone.getText().toString();
        if (TextUtils.isEmpty(mPhone) || mPhone.length() != 11) {
            showOneToast(R.string.qingshuruzhengquedeshoujihao);
            return "";
        }
        CountDownHelper countDownHelper = new CountDownHelper(60, 1, TimeUnit.SECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    mTvGetCheckCode.setText(millisUntilFinished / 1000 + "");
                } catch (Exception e) {
                    new Throwable("报空我就try-catch").printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                try {
                    mTvGetCheckCode.setText("重新获取");
                } catch (Exception e) {
                    new Throwable("报空我就try-catch").printStackTrace();
                }
            }
        };
        countDownHelper.start();
        return mPhone;
    }


    private boolean login() {
        boolean scusess = true;
        mPhone = mEditPhone.getText().toString();
        mCheckCode = mEditCheckCode.getText().toString();
        if (TextUtils.isEmpty(mPhone) || mPhone.length() != 11) {
            showOneToast("开始表演吧");
            scusess = false;
        }
        if (TextUtils.isEmpty(mCheckCode) || mCheckCode.length() != 4) {
            showOneToast(R.string.qingshuruzhengquedeyanzhengma);
            scusess = false;
        }
        return scusess;
    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        if (Config.api_name.equals(Config.INTERFACE_LOGIN)) {
            showLog(jsonObject.toString());
            loginBean classBean = new Gson().fromJson(jsonObject.toString(), new TypeToken<loginBean>() {
            }.getType());
            if (classBean != null) {
                String token = classBean.getToken();
                if (!"".equals(token)) {
                    Config.token = token;
                    SPUtils.putString("token", token);
                }
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("loginBean", classBean);
                startActivity(intent);
            }
        }
    }

    @Override
    public void showWaitDialog(String msg) {
        super.showWaitDialog(msg);
    }

    @Override
    public void dissMissWaitDialog() {
        super.dissMissWaitDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needCheckPermission()) {
            if (hasPermissons(this, needPermissions)) {
            } else {
                ActivityCompat.requestPermissions(this, needPermissions, ActivityInfo.ACTIVITY_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ActivityInfo.ACTIVITY_CODE && hasAllPermissionsGranted(grantResults)) {
            DialogHelper.createAlertDialog(this).show();
        } else {
            showMissingPermissionDialog();
        }
    }
}
