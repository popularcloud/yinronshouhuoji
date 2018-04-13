package cn.dlc.yinrongshouhuoji.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import cn.dlc.commonlibrary.ui.widget.EmptyView;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.commonlibrary.utils.rv_tool.EmptyRecyclerView;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.adpter.TakeOutListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.TakeOutListBean;
import cn.dlc.yinrongshouhuoji.https.Config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class TakeOutListActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    @BindView(R.id.rv_take_out_list)
    RecyclerView mRvTakeOutList;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout mRefreshLayout;

    private int page = 1;
    private TakeOutListAdapter mTakeOutListAdapter;
    private Map<Integer, List<TakeOutListBean>> mFakeMap;
    private SaleMachineListBean.DataBean mDeviceListBean;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_take_out_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initFakeData();
        initRecyclerView();
        showWaitDialog("正在派送数据.");
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    private void initFakeData() {
        mDeviceListBean = (SaleMachineListBean.DataBean) getIntent().getSerializableExtra("data");
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "goodTakeaway");
        dataMap.put("token", Config.token);
        dataMap.put("page", page + "");
        dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
        dataMap.put("psize", 20 + "");
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    private void initRecyclerView() {
        mTakeOutListAdapter = new TakeOutListAdapter(getActivity());
        mRvTakeOutList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvTakeOutList.setAdapter(mTakeOutListAdapter);
        EmptyRecyclerView.bind(mRvTakeOutList, mEmptyView);
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
                page = 1;
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("api_name", "goodTakeaway");
                dataMap.put("token", Config.token);
                dataMap.put("page", page + "");
                dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
                dataMap.put("psize", 20 + "");
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
            }
        });
        mRefreshLayout.startRefresh();
    }


    @Override
    public void showWaitDialog(String msg) {
        super.showWaitDialog(msg);
    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);

    }

    @Override
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {
        super.onNetJSONArray(jsonArray, trxcode);
        if (!"".equals(jsonArray.toString())){
            List<TakeOutListBean> machineListBean = new Gson().fromJson(jsonArray.toString(),new TypeToken<List<TakeOutListBean>>(){}.getType());
            mFakeMap = new HashMap<>();
            mFakeMap.put(page,machineListBean);
            if (page == 1) {
                List<TakeOutListBean> mList = mFakeMap.get(page);
                if (mList != null && mList.size() != 0) {
                    page++;
                    mTakeOutListAdapter.setNewData(mList);
                    mRefreshLayout.finishRefreshing();
                }
            } else {
                List<TakeOutListBean> mList = mFakeMap.get(page);
                if (mList != null && mList.size() != 0) {
                    page++;
                    mTakeOutListAdapter.appendData(mList);
                } else {
                    showOneToast(R.string.meiyougengduoshuju);
                }
                mRefreshLayout.finishLoadmore();
            }

        }
    }
}
