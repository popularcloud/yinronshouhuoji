package cn.dlc.yinrongshouhuoji.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;


import com.android.tu.loadingdialog.LoadingDailog;
import com.facebook.stetho.common.LogUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.dlc.commonlibrary.ui.base.BaseCommonActivity;
import cn.dlc.commonlibrary.ui.dialog.WaitingDialog;
import cn.dlc.commonlibrary.utils.ToastUtil;
import cn.dlc.yinrongshouhuoji.App;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.https.OkHttpUtils;
import cn.dlc.yinrongshouhuoji.https.ResponceCode;
import cn.dlc.yinrongshouhuoji.login.activity.LoginActivity;
import cn.dlc.yinrongshouhuoji.util.NetworkUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public abstract class BaseActivity extends BaseCommonActivity {


    private WaitingDialog mWaitingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        setTranslucentStatus(); // 沉浸状态栏
    }


    ////////////////////////////////////////网络请求相关/////////////////////////////////////////////////////////////

    /**
     * 这个公共方法只对应登录注册我忘记密码等
     *
     * @param api_name 请求的方法名称
     * @param map      参数
     */
    public void doApiPost(final String api_name, Map<String, String> map) {
        if (!NetworkUtil.isConnected(this)) {
            ToastUtil.show(this, "没有网络");
            return;
        }
        String api = Config.SERVER_API_ADDRESS + api_name;
        FormBody formBody = map2FormBodys(map);
        //创建一个Request
        Request request = new Request.Builder()
                .url(api)
                .post(formBody)
                .build();
        if (!isNetWorkAvailable()){
            ToastUtil.show(this,"网络似乎走神了,请检查..");
            dissMissWaitDialog();
            return;
        }
        showLog("\n\n接口：" + api);
        for (int i = 0;i<formBody.size();i++){
            showLog("\n参数"+i+"：参数名："+formBody.encodedName(i)+"\t\t参数值："+formBody.encodedValue(i));
        }
        //发起异步请求，并加入回调
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showLog(e.getMessage());
                dissMissWaitDialog();
                if (e instanceof ConnectTimeoutException) {
                          onApiError("连接超时", e.getMessage()+"");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resBody = response.body().string();
                showLog(resBody);
                dissMissWaitDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (TextUtils.isEmpty(resBody)) return;
                            JSONObject jsonObject = new JSONObject(resBody);
                            int code = jsonObject.getInt("code");
                            if (code == ResponceCode.SUCCESS) {
                                String msg = jsonObject.getString("msg" + "");
                                ToastUtil.show(getApplicationContext(), msg);
                                onNetSucessNoData(api_name);
                                //说明接口请求成功
                                Object object = jsonObject.get("data");
                                if (TextUtils.isEmpty(object.toString())) {
                                    ToastUtil.show(getApplicationContext(), api_name + "接口不规范");
                                    return;
                                }
                                if (object instanceof JSONObject) {
                                    //返回的数据为jsonobject
                                    onNetJSONObject((JSONObject) object, api_name);
                                    showLog("回调：JSONObject");
                                } else if (object instanceof Integer){
                                    onNetJSONObject(jsonObject, api_name);
                                    showLog("回调：Intger---JSONObject");
                                }else {
                                    //返回的数据为jsonarray
                                    showLog("回调：JSONArray");
                                    onNetJSONArray((JSONArray) object, api_name);
                                }
                            } else if (code == ResponceCode.LOGINOUT) {
                                ToastUtil.show(getApplicationContext(), "您被挤下线,请重新登录.");
                                App.instance().extiApp();
                                Intent intent = new Intent();
                                intent.setClass(BaseActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                String msg = jsonObject.getString("msg");
                                onApiError(msg, api_name);
                                ToastUtil.show(getApplicationContext(), msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }


    /**
     * 文件上传
     *
     * @param api_name
     * @param map
     */
    public void doUploadSubmit(final String api_name, Map<String, String> map, File z_img, String fileKey) {
        //多个文件集合
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置为表单类型
        builder.setType(MultipartBody.FORM);
        //添加表单键值
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        //身份证正面
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), z_img);
        builder.addFormDataPart(fileKey, z_img.getName(), fileBody);

        Request request = new Request.Builder().url(Config.SERVER_API_ADDRESS + api_name).post(builder.build()).build();
        //发起异步请求，并加入回调
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.show(getActivity(),e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resBody = response.body().string();
                LogUtil.e("请求:" + api_name + "\n\r" + "返回:" + resBody);
                try {
                    final JSONObject jsonObject = new JSONObject(resBody);
                    final int code = jsonObject.getInt("code");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (code == ResponceCode.SUCCESS) {
                                onUploadSuccess(api_name);
                            } else {
                                try {
                                    String msg = jsonObject.getString("msg");
                                    onUploadFailure(api_name, msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onUploadSuccess(String api_name) {

    }

    /**
     * 上传失败
     *
     * @param api_name
     */
    public void onUploadFailure(String api_name, String msg) {
        ToastUtil.show(getActivity(),msg);
    }


    /**
     * 这个公共方法只对应登录注册我忘记密码等
     *
     * @param api_name 请求的方法名称
     * @param map      参数
     */
    public void doUserPost(final String api_name, Map<String, String> map) {
        FormBody formBody = map2FormBodys(map);
        //创建一个Request
        Request request = new Request.Builder()
                .url(Config.SERVER_API_ADDRESS + api_name)
                .post(formBody)
                .build();
        onNetOk();
        //发起异步请求，并加入回调
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showLog("dgz" + e.getMessage());
                dissMissWaitDialog();
                if (e instanceof ConnectTimeoutException) {
                    onApiError("连接超时", "");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resBody = response.body().string();
                showLog("dgz" + api_name + "\n\r" + "返回:" + resBody);
                dissMissWaitDialog();
                Log.e("spm", "result:" + resBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (TextUtils.isEmpty(resBody)) return;
                            JSONObject jsonObject = new JSONObject(resBody);
                            int code = jsonObject.getInt("code");
                            if (code == ResponceCode.SUCCESS) {
                                //说明接口请求成功
                                onMsg(api_name, jsonObject.getString("msg"));
                                Object object = jsonObject.get("data");
                                if (object instanceof JSONObject) {
                                    //返回的数据为jsonobject
                                    onNetJSONObject((JSONObject) object, api_name);
                                } else {
                                    //返回的数据为jsonarray
                                    onNetJSONArray((JSONArray) object, api_name);
                                }
                            } else if (code == ResponceCode.LOGINOUT) {
                                dissMissWaitDialog();
                                ToastUtil.show(getApplicationContext(), "您被挤下线,请重新登录.");
                                App.instance().extiApp();
                                Intent intent = new Intent();
                                intent.setClass(BaseActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                String msg = jsonObject.getString("msg");
                                ToastUtil.show(getApplicationContext(), msg);
                                onApiError(msg, api_name);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    public void onNetSucessNoData(String trxcode) {

    }

    /**
     * 返回 JSONObject数据
     *
     * @param jsonObject 数据体
     * @param trxcode    请求方法
     */
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {

    }

    /**
     * 返回 JSONArray数据
     *
     * @param jsonArray 数据体
     * @param trxcode   请求方法
     */
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {

    }


    /**
     * 返回 data为[]的时候回调
     */
    public void onNetOk() {
        if (!isNetWorkAvailable()){
            ToastUtil.show(this,"网络似乎走神了,请检查..");
            dissMissWaitDialog();
            return;
        }
    }

    public void onMsg(String api_name, String msg) {

    }

    public void onApiError(String errtext, String api) {
    }


    /*****************************************************************************************/
    /**
     * 显示等待对话框
     *
     * @param text
     * @param cancelable
     */
    public void showWaitingDialog(String text, boolean cancelable) {
        if (mWaitingDialog == null) {
            mWaitingDialog = WaitingDialog.newDialog(this).setMessage(text);
        }
        if (mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
        mWaitingDialog.setCancelable(cancelable);
        mWaitingDialog.show();
    }

    public void showWaitingDialog(int stringRes, boolean cancelable) {
        showWaitingDialog(getString(stringRes), cancelable);
    }

    /**
     * 把map转成frombody
     *
     * @param params
     * @return
     */
    public FormBody map2FormBodys(Map<String, String> params) {
        showLog("params:" + params.toString());
        if (params == null) return null;
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }


    /********************************************************************************************************/
    /**
     * 权限请求结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    private final int mRequestCode = 1024;
    private RequestPermissionCallBack mRequestPermissionCallBack;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        StringBuilder permissionName = new StringBuilder();
        for (String s : permissions) {
            permissionName = permissionName.append(s + "\r\n");
        }
        switch (requestCode) {
            case mRequestCode: {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        hasAllGranted = false;
                        //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                        // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            new android.app.AlertDialog.Builder(BaseActivity.this).setTitle("PermissionTest")//设置对话框标题
                                    .setMessage("【用户选择了不再提示按钮，或者系统默认不在提示（如MIUI）。" +
                                            "引导用户到应用设置页去手动授权,注意提示用户具体需要哪些权限】" +
                                            "获取相关权限失败:" + permissionName +
                                            "将导致部分功能无法正常使用，需要到设置页面手动授权")//设置显示的内容
                                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            //TODO Auto-generated method stub
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mRequestPermissionCallBack.denied();
                                }
                            }).show();//在按键响应事件中显示此对话框
                        } else {
                            //用户拒绝权限请求，但未选中“不再提示”选项
                            mRequestPermissionCallBack.denied();
                        }
                        break;
                    }
                }
                if (hasAllGranted) {
                    mRequestPermissionCallBack.granted();
                }
            }
        }
    }

    /**************************权限相关********************************************/
    // 含有全部的权限
    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否需要检查权限
     */
    public static boolean needCheckPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /**
     * 是否拥有权限
     */
    public static boolean hasPermissons(@NonNull Activity activity, @NonNull String... permissions) {
        if (!needCheckPermission()) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    // 显示缺失权限提示
    public void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package" + getPackageName()));
        startActivity(intent);
    }


    /**
     * 发起权限请求
     *
     * @param context
     * @param permissions
     * @param callback
     */
    public void requestPermissions(final Context context, final String[] permissions,
                                   RequestPermissionCallBack callback) {
        this.mRequestPermissionCallBack = callback;
        StringBuilder permissionNames = new StringBuilder();
        for (String s : permissions) {
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new android.app.AlertDialog.Builder(BaseActivity.this).setTitle("PermissionTest")//设置对话框标题
                            .setMessage("【用户曾经拒绝过你的请求，所以这次发起请求时解释一下】" +
                                    "您好，需要如下权限：" + permissionNames +
                                    " 请允许，否则将影响部分功能的正常使用。")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    //TODO Auto-generated method stub
                                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                                }
                            }).show();//在按键响应事件中显示此对话框
                } else {
                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                }
                break;
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    /**
     * 权限请求结果回调接口
     */
    public interface RequestPermissionCallBack {
        /**
         * 同意授权
         */
        void granted();

        /**
         * 取消授权
         */
        void denied();
    }

    public void showLog(String showLog) {
        Log.d("dgz", showLog);
    }

    //等待对话框
    LoadingDailog dialog;

    public void showWaitDialog(String msg) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage(msg)
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();
        dialog.show();
    }

    //关闭对话框
    public void dissMissWaitDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    public boolean isNetWorkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
