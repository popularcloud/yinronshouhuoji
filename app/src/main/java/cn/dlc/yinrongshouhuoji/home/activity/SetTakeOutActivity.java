package cn.dlc.yinrongshouhuoji.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.bean.ScanResultBean;
import cn.dlc.yinrongshouhuoji.home.bean.SetWaiMaiBean;
import cn.dlc.yinrongshouhuoji.https.Config;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class SetTakeOutActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.tv_activated_grid)
    TextView mTvActivatedGrid;
    @BindView(R.id.img_reduce)
    ImageView mImgReduce;
    @BindView(R.id.tv_current_count)
    TextView mTvCurrentCount;
    @BindView(R.id.img_add)
    ImageView mImgAdd;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private int activatedCount;
    private int mCurrentCount;

    private String api_name = "";

    private SaleMachineListBean.DataBean mDeviceListBean;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_set_take_out;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api_name = "";
        mDeviceListBean = (SaleMachineListBean.DataBean) getIntent().getSerializableExtra("data");
        initTitleBar();
        initFakeData();
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    private void initFakeData() {
        Map<String, String> dataMap = new HashMap<String, String>();
        api_name = "getCupbaordEmptyBoxNum";
        dataMap.put("api_name", "getCupbaordEmptyBoxNum");
        dataMap.put("token", Config.token);
        dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
        mTvCurrentCount.setText(String.valueOf(mCurrentCount));
    }

    @OnClick({R.id.img_reduce, R.id.img_add, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_reduce:
                reduce();
                break;
            case R.id.img_add:
                add();
                break;
            case R.id.btn_confirm://立即放置
                confirm();
                break;
        }
    }

    private void add() {
        if (mCurrentCount < activatedCount) {
            mCurrentCount ++;
            mTvCurrentCount.setText(String.valueOf(mCurrentCount));
        }
    }

    private void reduce() {
        if (mCurrentCount > 0) {
            mCurrentCount -= 1;
            mTvCurrentCount.setText(String.valueOf(mCurrentCount));
        }
    }

    private void confirm() {
        int max = Integer.valueOf(mTvCurrentCount.getText().toString());
        if (max == 0) {
            showOneToast(R.string.zongshuliangbunengweiling);
            return;
        }
        mTvCurrentCount.setText(String.valueOf(mCurrentCount));
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "makePutLog");
        api_name = "makePutLog";
        dataMap.put("num", mCurrentCount + "");
        dataMap.put("token", Config.token);
        dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        SetWaiMaiBean setWaiMaiBean =
                new Gson().fromJson(jsonObject.toString(),
                        new TypeToken<SetWaiMaiBean>() {
                        }.getType());
        if (!api_name.equals("makePutLog")) {
            activatedCount = setWaiMaiBean.getData();
            mTvActivatedGrid.setText(String.valueOf(setWaiMaiBean.getData()));
        }else {
            int log = setWaiMaiBean.getPut_log();
            startActivity(ChooseGoodsActivity.newIntent(this, log,mDeviceListBean.getCupboard_id()));
        }
    }

}
