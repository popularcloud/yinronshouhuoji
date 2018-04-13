package cn.dlc.yinrongshouhuoji.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.fragment.BaseFragment;
import cn.dlc.yinrongshouhuoji.home.bean.MineInfoBean;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.personal.activity.MyroomActivity;
import cn.dlc.yinrongshouhuoji.personal.activity.PersonalDataActivity;
import cn.dlc.yinrongshouhuoji.personal.activity.SettingActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.head_img)
    ImageView mHeadImg;
    @BindView(R.id.songcan_rl)
    RelativeLayout mSongcanRl;
    @BindView(R.id.setting_rl)
    RelativeLayout mSettingRl;
    Unbinder unbinder;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.user_number_tv)
    TextView mUserNumberTv;

    private MineInfoBean mineInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        unbinder =
            ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initHeadImage();
    }

    private void initHeadImage() {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "manEt");
        dataMap.put("token", Config.token);
        doApiPost(Config.INTERFACE_MINE, dataMap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.head_img, R.id.songcan_rl, R.id.setting_rl })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //头像
            case R.id.head_img:
                Intent intent = new Intent(getActivity(),PersonalDataActivity.class);
                intent.putExtra("data",mineInfoBean);
                startActivity(intent);
                break;
            //我的送餐
            case R.id.songcan_rl:
                Intent intent1 = new Intent(getActivity(),MyroomActivity.class);
                intent1.putExtra("data",mineInfoBean);
                startActivity(intent1);
                break;
            //设置
            case R.id.setting_rl:
                Intent intent2 = new Intent(getActivity(),SettingActivity.class);
                intent2.putExtra("data",mineInfoBean);
                startActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        mineInfoBean = new Gson().fromJson(jsonObject.toString(),
                new TypeToken<MineInfoBean>(){}.getType());
        Glide.with(getActivity())
                .load(mineInfoBean.getImage()+"")
                .apply(new RequestOptions().transform(new CircleCrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.chenggongda))
                .into(mHeadImg);
        mNameTv.setText(mineInfoBean.getNickname()+"");
        mUserNumberTv.setText(mineInfoBean.getPhone()+"");
    }

}
