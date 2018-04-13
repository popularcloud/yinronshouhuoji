package cn.dlc.yinrongshouhuoji.base.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tu.loadingdialog.LoadingDailog;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import cn.dlc.commonlibrary.ui.base.BaseCommonFragment;
import cn.dlc.commonlibrary.utils.ToastUtil;
import cn.dlc.yinrongshouhuoji.App;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.https.OkHttpUtils;
import cn.dlc.yinrongshouhuoji.https.ResponceCode;
import cn.dlc.yinrongshouhuoji.login.activity.LoginActivity;
import cn.dlc.yinrongshouhuoji.util.NetworkUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public abstract class BaseFragment extends BaseCommonFragment {
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //得到可靠地Activity
    public Activity getMyActivity() {
        return mActivity;
    }

    /**
     * 这个公共方法只对应登录注册我忘记密码等
     *
     * @param api_name 请求的方法名称
     * @param map      参数
     */
    public void doApiPost(final String api_name, Map<String, String> map) {
        if (!NetworkUtil.isConnected(App.instance())) {
            ToastUtil.show(App.instance(), "没有网络");
            return;
        }
        String api = Config.SERVER_API_ADDRESS + api_name;
        FormBody formBody = map2FormBodys(map);
        //创建一个Request
        Request request = new Request.Builder()
                 .url(api)
                .post(formBody)
                .build();
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
                ToastUtil.show(App.instance(),"链接超时。。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resBody = response.body().string();
                showLog("接口返回："+resBody);
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
                                ToastUtil.show(App.instance(), msg);
                                //说明接口请求成功
                                Object object = jsonObject.get("data");
                                if (TextUtils.isEmpty(object.toString())) {
                                    ToastUtil.show(App.instance(), api_name + "接口不规范");
                                    return;
                                }
                                if (object instanceof JSONObject) {
                                    //返回的数据为jsonobject
                                    onNetJSONObject((JSONObject) object, api_name);
                                } else {
                                    //返回的数据为jsonarray
                                    onNetJSONArray((JSONArray) object, api_name);
                                    onNetOk("");
                                }
                            } else if (code == ResponceCode.LOGINOUT) {
                                cn.dlc.commonlibrary.utils.ToastUtil.show(App.instance(), "您被挤下线,请重新登录.");
                                App.instance().extiApp();
                                Intent intent = new Intent();
                                intent.setClass(App.instance(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                String msg = jsonObject.getString("msg");
                                ToastUtil.show(App.instance(), msg);
                                onApiError(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
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
        //发起异步请求，并加入回调
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof ConnectTimeoutException) {
                    onApiError("连接超时");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resBody = response.body().string();
                getMyActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (TextUtils.isEmpty(resBody)) return;
                            JSONObject jsonObject = new JSONObject(resBody);
                            int code = jsonObject.getInt("code");
                            if (code == ResponceCode.SUCCESS) {
                                //说明接口请求成功
                                onNetOk(api_name);
                                Object object = jsonObject.get("data");
                                if (object instanceof JSONObject) {
                                    //返回的数据为jsonobject
                                    onNetJSONObject((JSONObject) object, api_name);
                                } else {
                                    //返回的数据为jsonarray
                                    onNetJSONArray((JSONArray) object, api_name);
                                }
                            } else {
                                String msg = jsonObject.getString("msg");
                                ToastUtil.show(App.instance(), msg);
                                onApiError(msg);
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
     * 把map转成frombody
     *
     * @param params
     * @return
     */
    public FormBody map2FormBodys(Map<String, String> params) {
        if (params == null) return null;
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
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

    public void onApiError(String errtext) {

    }

    /**
     * 返回 code为1的时候回调接口
     */
    public void onNetOk(String trx) {

    }

    public void showLog(String showLog) {
        Log.d("dgz", showLog);
    }

    //等待对话框
    LoadingDailog dialog;

    public void showWaitDialog(String msg,Activity activity) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(activity)
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

}
