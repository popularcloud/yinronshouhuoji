package cn.dlc.yinrongshouhuoji.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.fragment.BaseFragment;
import cn.dlc.yinrongshouhuoji.home.bean.MenuBean;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.personal.activity.MenuDetailsActivity;
import cn.dlc.yinrongshouhuoji.personal.activity.MyroomActivity;
import cn.dlc.yinrongshouhuoji.personal.adapter.AllGreensAdapter;
import cn.dlc.yinrongshouhuoji.personal.adapter.AllItemAdapter;
import cn.dlc.yinrongshouhuoji.personal.bean.AllBean;
import cn.dlc.yinrongshouhuoji.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class MyFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.fragemt_all_tv)
    TextView mFragemtAllTv;
    @BindView(R.id.fragemt_cv)
    CardView mFragemtCv;
    @BindView(R.id.fragmemt_recyclerview)
    RecyclerView mFragmemtRecyclerview;

    private int mType;
    private ArrayList<AllBean> mLists;
    private ArrayList<MenuBean> mDatas;
    private ArrayList<MenuBean> mMenuBeans;

    private ArrayList<MenuBean> all_menuBean;
    private ArrayList<MenuBean> unSend_menuBean;
    private ArrayList<MenuBean> hasSend_menuBean;


    private AllItemAdapter mItemAdapter;
    private AllGreensAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mType = bundle.getInt("TYPE");
        unbinder =
                ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        mDatas = new ArrayList<>();
        all_menuBean = new ArrayList<>();
        unSend_menuBean = new ArrayList<>();
        hasSend_menuBean = new ArrayList<>();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerview();
    }

    String[] sendStatus = new String[]{"all", "not_sent", "have_sent"};
    int index = -1;

    private void initRecyclerview() {
        mLists = new ArrayList<>();
        switch (mType) {
            case 0:
                index = 0;
                request(sendStatus[0]);
                break;
            case 1:
                index = 1;
                request(sendStatus[1]);
                break;
            case 2:
                index = 2;
                request(sendStatus[2]);
                break;
        }
    }

    int p = 1;
    int psize = 20;

    private void request(String status) {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "my_food");
        dataMap.put("token", Config.token);
        dataMap.put("p", p + "");
        dataMap.put("status", status + "");
        dataMap.put("psize", psize + "");
        doApiPost(Config.INTERFACE_MINE, dataMap);
    }

    /**
     * 全部的设置Adapter和数据
     */
    private void initAllAdapter() {
        mFragmemtRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFragmemtRecyclerview.setVisibility(View.VISIBLE);
        mFragemtCv.setVisibility(View.GONE);
        adapter = new AllGreensAdapter(getActivity());
        mFragmemtRecyclerview.setAdapter(adapter);
        adapter.setNewData(mLists);
        adapter.setClickListener(new AllGreensAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                int currentId = MyroomActivity.mViewPager.getCurrentItem();
                Intent intent = new Intent(getActivity(), MenuDetailsActivity.class);
                switch (currentId) {
                    case 0:
                        int allId = all_menuBean.get(position).getOrder_id();
                        ToastUtil.showMessage(allId + "");
                        break;
                    case 1:
                        int unSendId= unSend_menuBean.get(position).getOrder_id();
                        ToastUtil.showMessage(unSendId + "");
                        break;
                    case 2:
                        int hasSendId = all_menuBean.get(position).getOrder_id();
                        ToastUtil.showMessage(hasSendId + "");
                        break;
                }
                startActivity(intent);
            }
        });


    }

    /**
     * 未派送和已派送的设置data和adapter
     *
     * @param list
     * @param s
     */

    private void initOtherAdapter(ArrayList<MenuBean> list, String s, final int type) {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mItemAdapter = new AllItemAdapter(getActivity());
        mFragmemtRecyclerview.setVisibility(View.GONE);
        mFragemtCv.setVisibility(View.VISIBLE);
        mRecyclerview.setAdapter(mItemAdapter);
        mItemAdapter.setNewData(list);
        mFragemtAllTv.setText(Html.fromHtml(getString(R.string.gongduos, list.size())));
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, BaseRecyclerAdapter.CommonHolder holder,
                                    int position) {
                Intent intent = new Intent(getActivity(), MenuDetailsActivity.class);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {
        super.onNetJSONArray(jsonArray, trxcode);
        ArrayList<MenuBean> beanList = new Gson().fromJson(jsonArray.toString(),
                new TypeToken<List<MenuBean>>() {
                }.getType());
        if (beanList != null && beanList.size() > 0) {

            switch (index) {
                case 0:
                    for (int i = 0; i < beanList.size(); i++) {
                        AllBean allBean = null;
                        mMenuBeans = new ArrayList<>();
                        for (int a = 0; a < beanList.get(i).getGoods_detail().size(); a++) {
                            MenuBean bean = new MenuBean();
                            bean.setGreensName(beanList.get(i).getGoods_detail().get(a).getTitle());
                            bean.setPrice(beanList.get(i).getGoods_detail().get(a).getPrice());
                            bean.setGresensNumb("X " + beanList.get(i).getGoods_detail().get(a).getNum());
                            bean.setCover_url(beanList.get(i).getGoods_detail().get(a).getCover_url());
                            bean.setOrder_id(beanList.get(i).getOrder_id());
                            mMenuBeans.add(bean);
                        }
                        all_menuBean.addAll(mMenuBeans);
                        allBean = new AllBean();
                        allBean.setGreesType(beanList.get(i).getStatus());
                        allBean.setList(mMenuBeans);
                        mLists.add(allBean);
                    }
                    initAllAdapter();
                    break;

                case 1:
                    for (int i = 0; i < beanList.size(); i++) {
                        AllBean allBean = null;
                        mMenuBeans = new ArrayList<>();
                        for (int a = 0; a < beanList.get(i).getGoods_detail().size(); a++) {
                            MenuBean bean = new MenuBean();
                            bean.setGreensName(beanList.get(i).getGoods_detail().get(a).getTitle());
                            bean.setPrice(beanList.get(i).getGoods_detail().get(a).getPrice());
                            bean.setGresensNumb("X " + beanList.get(i).getGoods_detail().get(a).getNum());
                            bean.setCover_url(beanList.get(i).getGoods_detail().get(a).getCover_url());
                            bean.setOrder_id(beanList.get(i).getOrder_id());
                            mMenuBeans.add(bean);
                        }
                        unSend_menuBean.addAll(mMenuBeans);
                        allBean = new AllBean();
                        allBean.setGreesType(beanList.get(i).getStatus());
                        allBean.setList(mMenuBeans);
                        mLists.add(allBean);
                    }
                    initAllAdapter();
                    break;
                case 2:
                    for (int i = 0; i < beanList.size(); i++) {
                        AllBean allBean = null;
                        mMenuBeans = new ArrayList<>();
                        for (int a = 0; a < beanList.get(i).getGoods_detail().size(); a++) {
                            MenuBean bean = new MenuBean();
                            bean.setGreensName(beanList.get(i).getGoods_detail().get(a).getTitle());
                            bean.setPrice(beanList.get(i).getGoods_detail().get(a).getPrice());
                            bean.setGresensNumb("X " + beanList.get(i).getGoods_detail().get(a).getNum());
                            bean.setCover_url(beanList.get(i).getGoods_detail().get(a).getCover_url());
                            bean.setOrder_id(beanList.get(i).getOrder_id());
                            mMenuBeans.add(bean);
                        }
                        hasSend_menuBean.addAll(mMenuBeans);
                        allBean = new AllBean();
                        allBean.setGreesType(beanList.get(i).getStatus());
                        allBean.setList(mMenuBeans);
                        mLists.add(allBean);
                    }
                    initAllAdapter();
                    break;
            }
        }

    }
}
