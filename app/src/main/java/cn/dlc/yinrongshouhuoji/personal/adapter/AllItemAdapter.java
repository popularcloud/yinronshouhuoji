package cn.dlc.yinrongshouhuoji.personal.adapter;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.MenuBean;
import cn.dlc.yinrongshouhuoji.util.GlideImageLoader;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class AllItemAdapter extends BaseRecyclerAdapter<MenuBean> {

    private Context context;

    public AllItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_allgreensitem;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        MenuBean bean = getItem(position);
        holder.setText(R.id.adapter_all_item_geers_tv,bean.getGreensName());
        holder.setText(R.id.adapter_all_item_price_tv,bean.getPrice());
        holder.setText(R.id.adapter_all_item_mub_tv,bean.getGresensNumb());
        ImageView imageView = holder.getImage(R.id.adapter_all_img);
        try {
            Glide.with(context)
                    .load(bean.getCover_url())
                    .apply(new RequestOptions().transform(new CircleCrop())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.chenggongda))
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
