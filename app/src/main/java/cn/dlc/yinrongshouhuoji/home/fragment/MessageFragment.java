package cn.dlc.yinrongshouhuoji.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import cn.dlc.commonlibrary.ui.widget.EmptyView;
import cn.dlc.commonlibrary.utils.rv_tool.EmptyRecyclerView;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.fragment.BaseFragment;
import cn.dlc.yinrongshouhuoji.home.adpter.MessageListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.MessageListBean;
import cn.dlc.yinrongshouhuoji.home.bean.MsgArrayBean;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TwoButtonDialog;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.personal.view.MyDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class MessageFragment extends BaseFragment {
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    @BindView(R.id.rv_message_list)
    RecyclerView mRvMessageList;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout mRefreshLayout;

    private int page;
    private int psize = 10;
    private MessageListAdapter mMessageListAdapter;
    private Map<Integer, List<MessageListBean>> mFakeMap;
    private String api = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFakeData();
        initRecyclerView();
    }

    private void initFakeData() {
        mFakeMap = new HashMap<>();
        Map<String, String> dataMap = new HashMap<String, String>();
        api = "getBuHuoYuanMessage";
        dataMap.put("api_name", api);
        dataMap.put("token", Config.token);
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    private void initRecyclerView() {
        mMessageListAdapter = new MessageListAdapter(getActivity());
        mMessageListAdapter.setOnClickButtonListener(
                new MessageListAdapter.OnClickButtonListener() {
                    @Override
                    public void onClickCancel(MessageListBean mMessageListBean) {
                        showCancelOrdersDialog(mMessageListBean);
                    }

                    @Override
                    public void onClickAccept(MessageListBean mMessageListBean) {
                        showAcceptOrdersDialog(mMessageListBean);
                    }
                });
        mRvMessageList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvMessageList.setAdapter(mMessageListAdapter);
        EmptyRecyclerView.bind(mRvMessageList, mEmptyView);
        initRefresh();
    }

    private Integer[] accept = new Integer[]{1, 2};
    private int index = 0;

    private void showCancelOrdersDialog(final MessageListBean mMessageListBean) {
        //mMessageListBean 可能需要ID什么的，看接口
        TwoButtonDialog mCancelOrdersDialog = new TwoButtonDialog(getActivity());
        mCancelOrdersDialog.setTitle(R.string.tishi);
        mCancelOrdersDialog.setContent(R.string.shifouquerenjujuedingdan);
        mCancelOrdersDialog.setBtnConfirm(R.string.queding);
        mCancelOrdersDialog.setBtnCancel(R.string.quxiao);
        mCancelOrdersDialog.setOnClickButtonListener(new TwoButtonDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                index = 2;
                Map<String, String> dataMap = new HashMap<String, String>();
                api = "modifyMessageState";
                dataMap.put("api_name", api);
                dataMap.put("token", Config.token);
                dataMap.put("type", accept[1] + "");
                dataMap.put("message_id", mMessageListBean.getMessage_id() + "");
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
            }

            @Override
            public void onClickCancel() {

            }
        });
        mCancelOrdersDialog.show();
    }

    private void showAcceptOrdersDialog(final MessageListBean mMessageListBean) {
        //mMessageListBean 可能需要ID什么的，看接口
        TwoButtonDialog mCancelOrdersDialog = new TwoButtonDialog(getActivity());
        mCancelOrdersDialog.setTitle(R.string.tishi);
        mCancelOrdersDialog.setContent(R.string.shifouquerenjieshoudingdan);
        mCancelOrdersDialog.setBtnConfirm(R.string.queding);
        mCancelOrdersDialog.setBtnCancel(R.string.quxiao);
        mCancelOrdersDialog.setOnClickButtonListener(new TwoButtonDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                index = 1;
                Map<String, String> dataMap = new HashMap<String, String>();
                api = "modifyMessageState";
                dataMap.put("api_name", api);
                dataMap.put("token", Config.token);
                dataMap.put("type", accept[0] + "");
                dataMap.put("message_id", mMessageListBean.getMessage_id() + "");
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
            }

            @Override
            public void onClickCancel() {

            }
        });
        mCancelOrdersDialog.show();
    }

    private void showCancelTipsDialog(boolean b) {
        MyDialog mMyDialog = new MyDialog(getActivity());
        if (b) {
            mMyDialog.setIsText(R.string.chenggongjujuedingdan);
        } else {
            mMyDialog.setIsText(R.string.jujueshibai);
        }
        mMyDialog.setIsTrue(b);
        mMyDialog.show();
    }

    private void showAcceptTipsDialog(boolean b) {
        MyDialog mMyDialog = new MyDialog(getActivity());
        if (b) {
            mMyDialog.setIsText(R.string.chenggongjieshoudingdan);
        } else {
            mMyDialog.setIsText(R.string.jieshoushibai);
        }
        mMyDialog.setIsTrue(b);
        mMyDialog.show();
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
                mFakeMap = new HashMap<>();
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("api_name", "getBuHuoYuanMessage");
                dataMap.put("token", Config.token);
                dataMap.put("page", page + "");
                dataMap.put("psize", psize + "");
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                page++;
                mFakeMap = new HashMap<>();
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("api_name", "getBuHuoYuanMessage");
                dataMap.put("token", Config.token);
                dataMap.put("page", page + "");
                dataMap.put("psize", psize + "");
                doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
            }
        });
        mRefreshLayout.startRefresh();
    }


    @Override
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {
        super.onNetJSONArray(jsonArray, trxcode);
        if (api.equals("getBuHuoYuanMessage")) {
            List<MessageListBean> mListAll = new ArrayList<>();
            List<MsgArrayBean> arrayBean = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<MsgArrayBean>>() {
                    }.getType());
            for (int i = 0; i < arrayBean.size(); i++) {
                List<MessageListBean> mList = new ArrayList<>();
                mList.add(new MessageListBean(arrayBean.get(i).getType(), arrayBean.get(i).getContent() + "",
                        arrayBean.get(i).getStatus(),
                        arrayBean.get(i).getMessage_id(),
                        arrayBean.get(i).getCtime()));
                mListAll.addAll(mList);
            }
            mFakeMap.put(page, mListAll);

            if (page == 1) {
                List<MessageListBean> mList = mFakeMap.get(page);
                if (mList != null && mList.size() != 0) {
                    mMessageListAdapter.setNewData(mList);
                    mRefreshLayout.finishRefreshing();
                }
            } else {
                List<MessageListBean> mList = mFakeMap.get(page);
                if (mList != null && mList.size() != 0) {
                    mMessageListAdapter.appendData(mList);
                } else {
                    showOneToast(R.string.meiyougengduoshuju);
                }
                mRefreshLayout.finishLoadmore();
            }
            mRefreshLayout.finishRefreshing();
            mRefreshLayout.finishLoadmore();
        } else {
            switch (index) {
                case 1:
                    showAcceptTipsDialog(true);
                    break;
                case 2:
                    showCancelTipsDialog(true);
                    break;
            }
            api = "getBuHuoYuanMessage";
            mRefreshLayout.startRefresh();
        }
    }

}
