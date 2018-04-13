package cn.dlc.yinrongshouhuoji.personal.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.personal.fragment.MyFragment;
import cn.dlc.yinrongshouhuoji.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class MyroomActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    public static ViewPager mViewPager;
    private String[] mArray;
    private int currunt = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myroom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = findViewById(R.id.view_pager);
        initTitleBar();
        initData();
        initMagicIndicator();
        initViewPager();
    }

    /**
     * 初始化指示器
     */
    public  void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                MyFragment fragment = new MyFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE",position);
                fragment.setArguments(bundle);
                return fragment ;
            }

            @Override
            public int getCount() {
                return mArray.length;
            }
        });

    }

    private void initData() {
        mArray = this.getResources().getStringArray(R.array.illegal_order_array);
    }

    /**
     * 初始化指示器
     */
    private void initMagicIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(this);
        //评分标题
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mArray == null ? 0 : mArray.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(mArray[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 3));
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

}
