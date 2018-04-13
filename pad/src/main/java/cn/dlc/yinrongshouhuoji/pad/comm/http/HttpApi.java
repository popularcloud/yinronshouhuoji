package cn.dlc.yinrongshouhuoji.pad.comm.http;

import cn.dlc.commonlibrary.okgo.OkGoWrapper;
import cn.dlc.commonlibrary.okgo.callback.Bean01Callback;
import cn.dlc.yinrongshouhuoji.pad.comm.http.bean.AdvertisementBean;
import cn.dlc.yinrongshouhuoji.pad.comm.http.bean.BaseBean;
import cn.dlc.yinrongshouhuoji.pad.comm.ConfigHelper;
import cn.dlc.yinrongshouhuoji.pad.comm.model.Door;
import com.lzy.okgo.model.HttpParams;
import io.reactivex.Observable;

/**
 * 用来复制粘贴做模板的类，用来参考的
 */
public class HttpApi {

    private final OkGoWrapper mOkGoWrapper;

    private HttpApi() {
        mOkGoWrapper = OkGoWrapper.instance();
    }

    private static class InstanceHolder {
        private static final HttpApi sInstance = new HttpApi();
    }

    public static HttpApi get() {
        return InstanceHolder.sInstance;
    }

    private String getDeviceId() {
        return ConfigHelper.get().getDeviceId();
    }

    /**
     * 16.商家广告
     *
     * @param callback
     */
    public void advertisement(Bean01Callback<AdvertisementBean> callback) {
        HttpParams params = new HttpParams();
        params.put("api_name", "advertisement");
        params.put("device", getDeviceId());
        mOkGoWrapper.post(Urls.PUBLIC_ADVERTISEMENT, params, AdvertisementBean.class, callback);
    }

    /**
     * rx 16.商家广告
     *
     * @return
     */
    public Observable<AdvertisementBean> rxAdvertisement() {
        HttpParams params = new HttpParams();
        params.put("api_name", "advertisement");
        params.put("device", getDeviceId());
        return mOkGoWrapper.rxPostBean01(Urls.PUBLIC_ADVERTISEMENT, params,
            AdvertisementBean.class);
    }

    /**
     * 2.登入短信验证码
     *
     * @param mobile
     * @param callback
     */
    @Deprecated
    public void sendCode(String mobile, Bean01Callback<BaseBean> callback) {
        HttpParams params = new HttpParams();
        params.put("mobile", mobile);
        params.put("send_type", "login"); // 发送类型 登录验证码传 login，绑定手机 binding
        mOkGoWrapper.post(Urls.PUBLIC_SENDCODE, params, BaseBean.class, callback);
    }

    /**
     * 1.2售卖机校验密码登录
     *
     * @param deviceId
     * @param devicePwd
     * @param callback
     */
    public void login(String deviceId, String devicePwd, Bean01Callback<BaseBean> callback) {
        HttpParams params = new HttpParams();
        params.put("device", deviceId);
        params.put("pwd", devicePwd);
        mOkGoWrapper.post(Urls.PUBLIC_CUPBOARDLOGIN, params, BaseBean.class, callback);
    }

    /**
     * 格子关门时告诉给后台
     *
     * @param door
     */
    public void setBoxClosed(Door door) {
        HttpParams params = new HttpParams();
        params.put("api_name", "setBoxClosed");
        params.put("cupboard_number", getDeviceId());
        params.put("has_sth", door.hasGoods() ? 1 : 0); //格子里有东西 0无 1有
        params.put("box_number", door.getNum());
        mOkGoWrapper.post(Urls.BOX, params, BaseBean.class, new Bean01Callback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                // 不用管
            }

            @Override
            public void onFailure(String message, Throwable tr) {
                // 不用管
            }
        });
    }
}

