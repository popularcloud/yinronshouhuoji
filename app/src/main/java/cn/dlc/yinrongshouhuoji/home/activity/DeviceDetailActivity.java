package cn.dlc.yinrongshouhuoji.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.adpter.GridListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.GridListBean;
import cn.dlc.yinrongshouhuoji.home.bean.HomeDetailBean;
import cn.dlc.yinrongshouhuoji.home.utlis.helper.DeviceAddressHelper;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.util.Constance;
import cn.dlc.yinrongshouhuoji.util.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class DeviceDetailActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.tv_device_no)
    TextView mTvDeviceNo;
    @BindView(R.id.tv_device_name)
    TextView mTvDeviceName;
    @BindView(R.id.tv_device_address)
    TextView mTvDeviceAddress;
    @BindView(R.id.tv_grid_sum)
    TextView mTvGridSum;
    @BindView(R.id.tv_kongcang)
    TextView mTvKongcang;
    @BindView(R.id.tv_konghe)
    TextView mTvKonghe;
    @BindView(R.id.tv_guzhang)
    TextView mTvGuzhang;
    @BindView(R.id.tv_weiguanmen)
    TextView mTvWeiguanmen;
    @BindView(R.id.tv_waimai)
    TextView mTvWaimai;
    @BindView(R.id.tv_yuding)
    TextView mTvYuding;
    @BindView(R.id.tv_guoqi)
    TextView mTvGuoqi;
    @BindView(R.id.tv_waimai_detail)
    TextView mTvWaimaiDetail;
    @BindView(R.id.tv_yuding_detail)
    TextView mTvYudingDetail;
    @BindView(R.id.rv_grid)
    RecyclerView mRvGrid;

    private static final String EXTRA_BEAN = "extra_bean";

    private SaleMachineListBean.DataBean mDeviceListBean;
    private GridListAdapter mGridListAdapter;
    private List<HomeDetailBean.BoxesBean> mFakeList;
    private HomeDetailBean homeDetailBean;

    private int page = 1;

    public static Intent newIntent(Context mContext, SaleMachineListBean.DataBean mDeviceListBean) {
        Intent mIntent = new Intent(mContext, DeviceDetailActivity.class);
        mIntent.putExtra(EXTRA_BEAN, mDeviceListBean);
        return mIntent;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        showWaitDialog("正在加载数据.");
        onNetOk();
        resolveIntent();
        initTitleBar();
        initFakeData();
//        initRecyclerView();
    }

    private void resolveIntent() {
        mDeviceListBean = (SaleMachineListBean.DataBean) getIntent().getSerializableExtra(EXTRA_BEAN);
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
        mTitleBar.setTitle(mDeviceListBean.getTitle());
        request();
    }

    private void request(){
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "cupboardDetails");
        dataMap.put("token", Config.token);
        dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    private void initFakeData() {
        mTvDeviceNo.setText(mDeviceListBean.getCupboard_id()+"");
        mTvDeviceName.setText(mDeviceListBean.getTitle());
        final String mDeviceAddress = mDeviceListBean.getAddress();
        DeviceAddressHelper.setDeviceAddress(this, mTvDeviceAddress, mDeviceAddress);
        mTvGridSum.setText(Html.fromHtml(getString(R.string.zongshu_, 50)));
        //格子里面的ID根据中文拼音命名，英文水平有限
        mTvKongcang.setText(getString(R.string.kongcang, 0));
        mTvWaimai.setText(getString(R.string.waimai, 0));
        mTvKonghe.setText(getString(R.string.konghe, 0));
        mTvYuding.setText(getString(R.string.yuding, 0));
        mTvGuzhang.setText(getString(R.string.guzhang, 0));
        mTvGuoqi.setText(getString(R.string.guoqi, 0));
        mTvWeiguanmen.setText(getString(R.string.weiguanmen, 0));
        mFakeList = new ArrayList<>();
    }

    private void initRecyclerView() {
        mGridListAdapter = new GridListAdapter();
        mRvGrid.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mRvGrid.setNestedScrollingEnabled(false);
        mRvGrid.setAdapter(mGridListAdapter);
        mGridListAdapter.setNewData(mFakeList);
    }

    @OnClick({ R.id.tv_waimai_detail, R.id.tv_yuding_detail, R.id.tv_device_address })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_waimai_detail://外卖详情
                Intent intent = new Intent(DeviceDetailActivity.this,TakeOutListActivity.class);
                intent.putExtra("data",mDeviceListBean);
                startActivity(intent);
                break;
            case R.id.tv_yuding_detail://预定详情
                Intent intent2 = new Intent(DeviceDetailActivity.this,BookListActivity.class);
                intent2.putExtra("data",mDeviceListBean);
                startActivity(intent2);
                break;
            case R.id.tv_device_address:
                Intent intent3 = new Intent(DeviceDetailActivity.this,SetAddressActivity.class);
                intent3.putExtra("data",mDeviceListBean);
                startActivity(intent3);
                break;
        }
    }

    @Override
    public void showWaitDialog(String msg) {
        super.showWaitDialog(msg);
    }

    @Override
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {
        super.onNetJSONArray(jsonArray, trxcode);

    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        if (mFakeList!=null){
            if (mFakeList.size()>0){
                mFakeList.clear();
            }
        }
        homeDetailBean =  new Gson().fromJson(jsonObject.toString(),new TypeToken<HomeDetailBean>(){}.getType());
        List<HomeDetailBean.BoxesBean> boxesBeans = homeDetailBean.getBoxes();
        for (int i = 0;i<boxesBeans.size();i++){
            mFakeList.add(i,boxesBeans.get(i));
        }
        initRecyclerView();
        mTvKongcang.setText(getString(R.string.kongcang,homeDetailBean.getInfo().getBox_empty_num()));
        mTvWaimai.setText(getString(R.string.waimai, homeDetailBean.getInfo().getBox_back_num()));
        mTvKonghe.setText(getString(R.string.konghe, homeDetailBean.getInfo().getBox_empty_num()));
        mTvYuding.setText(getString(R.string.yuding, homeDetailBean.getInfo().getReserve()));
        mTvGuzhang.setText(getString(R.string.guzhang, homeDetailBean.getInfo().getBox_err_num()));
        mTvGuoqi.setText(getString(R.string.guoqi, homeDetailBean.getInfo().getOverdue()));
        mTvWeiguanmen.setText(getString(R.string.weiguanmen, homeDetailBean.getInfo().getBox_have_num()));
        mTvGridSum.setText(Html.fromHtml(getString(R.string.zongshu_, mFakeList.size())));
        if (Constance.isScanEnter){
            mTvDeviceAddress.setClickable(false);
            mTvDeviceAddress.setCompoundDrawables(null, null,null, null);
        }
        mTvDeviceAddress.setText(homeDetailBean.getInfo().getAddress()+"");
    }

    @Override
    public void onNetOk() {
        super.onNetOk();
    }


    @Override
    protected void onResume() {
        super.onResume();
        request();
    }
}
