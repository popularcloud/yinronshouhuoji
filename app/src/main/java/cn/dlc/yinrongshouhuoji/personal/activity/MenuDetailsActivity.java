package cn.dlc.yinrongshouhuoji.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.MenuBean;
import cn.dlc.yinrongshouhuoji.personal.adapter.AllItemAdapter;

import java.util.ArrayList;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class MenuDetailsActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.menu_paisong_img)
    ImageView mMenuPaisongImg;
    @BindView(R.id.menu_paisong_tv)
    TextView mMenuPaisongTv;
    @BindView(R.id.menu_recyclerview)
    RecyclerView mMenuRecyclerview;
    @BindView(R.id.menu_all_tv)
    TextView mMenuAllTv;
    @BindView(R.id.fragemt_cv)
    CardView mFragemtCv;
    @BindView(R.id.menu_ll)
    LinearLayout mMenuLl;
    @BindView(R.id.menu_address_tv)
    TextView mMenuAddressTv;
    @BindView(R.id.menu_getto_time_tv)
    TextView mMenuGettoTimeTv;
    @BindView(R.id.menu_indent_tv)
    TextView mMenuIndentTv;
    @BindView(R.id.menu_deal_tv)
    TextView mMenuDealTv;
    @BindView(R.id.menu_set_up_tiame_tv)
    TextView mMenuSetUpTiameTv;
    @BindView(R.id.menu_payment_time_tv)
    TextView mMenuPaymentTimeTv;
    @BindView(R.id.menu_arrive_time_tv)
    TextView mMenuArriveTimeTv;
    private ArrayList<MenuBean> mDatas;
    private int mTpye;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_menudetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initData();
        initView();
        initRecyclerView();
    }

    /**
     * 对其他view赋值
     */
    private void initView() {
        switch (mTpye) {
            case 0:
                mMenuPaisongImg.setImageResource(R.mipmap.weipaisong);
                mMenuPaisongTv.setText("未派送");
                mMenuLl.setVisibility(View.GONE);
                break;
            case 1:
                mMenuPaisongImg.setImageResource(R.mipmap.yipaisong);
                mMenuPaisongTv.setText("已派送");
                mMenuLl.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        mTpye = intent.getIntExtra("TYPE", -1);
        //未派送的
        mDatas = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            MenuBean bean = new MenuBean();
//            bean.setGreensName("苦瓜炒蛋");
//            bean.setPrice("18.00元");
//            bean.setGresensNumb("X 1");
//            mDatas.add(bean);
//        }
    }

    private void initRecyclerView() {
        mMenuRecyclerview.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AllItemAdapter adapter = new AllItemAdapter(getApplication());
        adapter.setNewData(mDatas);
        mMenuRecyclerview.setAdapter(adapter);
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }
}
