package cn.dlc.yinrongshouhuoji.pad.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import cn.dlc.yinrongshouhuoji.pad.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import java.util.List;

/**
 * 轮播图帮助类
 *
 * @param <T> 轮播图的数据类型
 */
public class BannerHelper<T> {

    private RequestOptions mRequestOptions;
    private Activity mActivity;

    private List<T> mBannerData;
    private Banner mBanner;
    private IBannerWork<T> mBannerWork;

    /**
     * 轮播图操作
     *
     * @param <T>
     */
    public interface IBannerWork<T> {
        /**
         * 配置轮播图
         * 参考 https://github.com/youth5201314/banner
         *
         * @param banner
         */
        void configBanner(Banner banner);

        /**
         * 获取轮播图需要的url
         *
         * @param item
         * @return
         */
        String transformBannerUrl(T item);

        /**
         * 响应点击轮播图
         *
         * @param position
         * @param item
         */
        void onClickBanner(int position, T item);
    }

    public abstract static class DefaultBannerWork<T> implements IBannerWork<T> {

        @Override
        public void configBanner(Banner banner) {

        }

        @Override
        public void onClickBanner(int position, T item) {

        }
    }

    public BannerHelper(Activity activity) {

        mActivity = activity;

        mRequestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.drawable.shape_default_bg)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate();
    }

    /**
     * 绑定轮播图
     *
     * @param banner
     * @param bannerWork
     */
    public void bindBanner(@NonNull Banner banner, @NonNull IBannerWork<T> bannerWork) {
        mBanner = banner;
        mBannerWork = bannerWork;
        preInitBanner(banner);
        mBannerWork.configBanner(banner);
    }

    /**
     * 预初始化轮播图
     *
     * @param banner
     */
    private void preInitBanner(Banner banner) {

        banner.setDelayTime(4000);// 延迟设置为4秒
        banner.setViewPagerIsScroll(true);// 开启滑动
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {

                String string = mBannerWork.transformBannerUrl((T) path);
                Glide.with(mActivity)
                    .applyDefaultRequestOptions(mRequestOptions)
                    .load(string)
                    .into(imageView);
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                try {

                    T t = mBannerData.get(position);
                    mBannerWork.onClickBanner(position, t);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        });
    }

    /**
     * 更新轮播图
     *
     * @param bannerData
     */
    public void updateBanner(List<T> bannerData) {
        mBannerData = bannerData;
        if (mBanner != null) {
            mBanner.update(bannerData);
        }
    }
}
