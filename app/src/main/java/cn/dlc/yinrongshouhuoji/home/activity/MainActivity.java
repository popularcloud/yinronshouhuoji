package cn.dlc.yinrongshouhuoji.home.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import butterknife.BindView;
import cn.dlc.commonlibrary.ui.adapter.CommonPagerAdapter;
import cn.dlc.commonlibrary.ui.widget.NoScrollViewPager;
import cn.dlc.commonlibrary.ui.widget.alphatabs.AlphaTabView;
import cn.dlc.commonlibrary.ui.widget.alphatabs.AlphaTabsIndicator;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.loginBean;
import cn.dlc.yinrongshouhuoji.home.fragment.HomeFragment;
import cn.dlc.yinrongshouhuoji.home.fragment.MessageFragment;
import cn.dlc.yinrongshouhuoji.home.fragment.MineFragment;

import java.util.ArrayList;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.tab_home)
    AlphaTabView mTabHome;
    @BindView(R.id.tab_message)
    AlphaTabView mTabMessage;
    @BindView(R.id.tab_mine)
    AlphaTabView mTabMine;
    @BindView(R.id.alphaIndicator)
    AlphaTabsIndicator mAlphaIndicator;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    public loginBean loginBean = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
        loginBean = (loginBean)getIntent().getSerializableExtra("loginBean");
    }

    private void initFragment() {

        HomeFragment homeFragment = new HomeFragment();
        MessageFragment messageFragment = new MessageFragment();
        MineFragment mineFragment = new MineFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(messageFragment);
        fragments.add(mineFragment);
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mAlphaIndicator.setViewPager(mViewPager);
    }
}
