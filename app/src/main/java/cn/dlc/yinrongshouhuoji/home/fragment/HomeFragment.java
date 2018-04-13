package cn.dlc.yinrongshouhuoji.home.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.commonlibrary.ui.widget.EmptyView;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.commonlibrary.utils.rv_tool.EmptyRecyclerView;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.fragment.BaseFragment;
import cn.dlc.yinrongshouhuoji.home.activity.SetTakeOutActivity;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.activity.DeviceControlActivity;
import cn.dlc.yinrongshouhuoji.home.activity.DeviceDetailActivity;
import cn.dlc.yinrongshouhuoji.home.adpter.DeviceListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.DeviceListBean;
import cn.dlc.yinrongshouhuoji.home.bean.ScanResultBean;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.login.activity.LoginActivity;
import cn.dlc.yinrongshouhuoji.util.Constance;
import cn.dlc.yinrongshouhuoji.util.ToastUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.licheedev.myutils.LogPlus;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    @BindView(R.id.rv_device_list)
    RecyclerView mRvDeviceList;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout mRefreshLayout;

    private static final int REQUEST_CODE_SCAN = 111;

    private int page = 0;
    private int psize = 20;
    private DeviceListAdapter mDeviceListAdapter;
    private Map<Integer, List<SaleMachineListBean.DataBean>> mFakeMap;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar();
        initFakeData();
        initRecyclerView();
    }

    private void initTitleBar() {
        mTitleBar.rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constance.isScanEnter = true;
                scan();

            }
        });
    }

    private void initFakeData() {
        showWaitDialog("正在派送数据",getActivity());
    }

    private void requestApi(){
        Map<String,String> dataMap =new  HashMap<String,String>();
        dataMap.put("api_name", "cupBoard");
        dataMap.put("token",Config.token);
        dataMap.put("page",page+"");
        dataMap.put("psize",psize+"");
        doApiPost(Config.INTERFACE_MAIN_LIST,dataMap);
    }

    private void initRecyclerView() {
        mDeviceListAdapter = new DeviceListAdapter(getActivity());
        mDeviceListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, BaseRecyclerAdapter.CommonHolder holder,
                int position) {
                SaleMachineListBean.DataBean mDeviceListBean = mDeviceListAdapter.getItem(position);
                startActivity(DeviceDetailActivity.newIntent(getActivity(), mDeviceListBean));
                Constance.isScanEnter = false;
            }
        });
        mRvDeviceList.setLayoutManager(
            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvDeviceList.setAdapter(mDeviceListAdapter);
        EmptyRecyclerView.bind(mRvDeviceList, mEmptyView);
        initRefresh();
    }

    private void initRefresh() {
        ProgressLayout mProgressLayout = new ProgressLayout(getActivity());
        mProgressLayout.setColorSchemeResources(R.color.color_ff9557);
        mRefreshLayout.setHeaderView(mProgressLayout);
        mRefreshLayout.setFloatRefresh(true);
        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page++;
                requestApi();

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            }
        });
        mRefreshLayout.startRefresh();
    }



    private void scan() {
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
        * 也可以不传这个参数
        * 不传的话  默认都为默认不震动  其他都为true
        * */
        ZxingConfig config = new ZxingConfig();
        config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
        config.setPlayBeep(true);//是否播放提示音
        config.setShake(true);//是否震动

        //如果不传 ZxingConfig的话，两行代码就能搞定了
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    String content="";//设备编号
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                content = data.getStringExtra(Constant.CODED_CONTENT);
                LogPlus.e("content ===== " + content);
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("api_name", "scan");
                dataMap.put("token", Config.token);
                dataMap.put("device_number",content);
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
            }
        }
    }

    @Override
    public void showWaitDialog(String msg, Activity activity) {
        super.showWaitDialog(msg,activity);
    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        ScanResultBean scanResultBean =
                new Gson().fromJson(jsonObject.toString(),
                        new TypeToken<ScanResultBean>(){}.getType());
        startActivity(DeviceControlActivity.newIntent(getContext(),
                new SaleMachineListBean.DataBean(content, scanResultBean.getTitle(),"",scanResultBean.getCupboard_id())));

    }

    @Override
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {
        super.onNetJSONArray(jsonArray, trxcode);
        if (!"".equals(jsonArray.toString())){
            List<SaleMachineListBean.DataBean> machineListBean = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<SaleMachineListBean.DataBean>>(){}.getType());
            mFakeMap = new HashMap<>();
            mFakeMap.put(page,machineListBean);
            if (page == 1) {
                List<SaleMachineListBean.DataBean> mList = mFakeMap.get(page);
                if (mList != null && mList.size() != 0) {
                    page++;
                mDeviceListAdapter.setNewData(mList);
                    mRefreshLayout.finishRefreshing();
                }
            } else {
                List<SaleMachineListBean.DataBean> mList = mFakeMap.get(page);
                if (mList != null && mList.size() != 0) {
                    page++;
                mDeviceListAdapter.appendData(mList);
                } else {
                    showOneToast(R.string.meiyougengduoshuju);
                }
                mRefreshLayout.finishLoadmore();
            }

        }
    }

    @Override
    public void onApiError(String errtext) {
        super.onApiError(errtext);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
