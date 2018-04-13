package cn.dlc.yinrongshouhuoji.personal.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.MineCenterBean;
import cn.dlc.yinrongshouhuoji.https.Config;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.setting_update_numb_tv)
    TextView mSettingUpdateNumbTv;
    @BindView(R.id.setting_update_rl)
    RelativeLayout mSettingUpdateRl;
    @BindView(R.id.aboutus_yinwen)
    TextView mAboutusYinwen;
    @BindView(R.id.img_logo)
    ImageView Ig_logo;
    @BindView(R.id.copanyName)
    TextView tv_CopanyName;
    @BindView(R.id.vision)
    TextView tv_Version;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "about");
        dataMap.put("token", Config.token);
        doApiPost(Config.INTERFACE_MINE, dataMap);
    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        MineCenterBean mineCenterBean = new Gson().fromJson(jsonObject.toString(), new TypeToken<MineCenterBean>() {
        }.getType());
        if (mineCenterBean != null) {
            try {
                Glide.with(getActivity())
                        .load(mineCenterBean.getItem_logo() + "")
                        .apply(new RequestOptions().transform(new CircleCrop())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.mipmap.chenggongda))
                        .into(Ig_logo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_CopanyName.setText(mineCenterBean.getName() + "");
            tv_Version.setText(mineCenterBean.getVersion() + "");
            mSettingUpdateNumbTv.setText(mineCenterBean.getTel() + "");
        }
    }
}
