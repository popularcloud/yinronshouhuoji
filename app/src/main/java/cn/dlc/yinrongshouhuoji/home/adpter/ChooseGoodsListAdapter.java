package cn.dlc.yinrongshouhuoji.home.adpter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.ChooseGoodsListBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class ChooseGoodsListAdapter extends BaseRecyclerAdapter<ChooseGoodsListBean> {

    private int mCurrentPosition = -1;
    private Context mContext;
    private int max;//最大放置数量

    public ChooseGoodsListAdapter(Context mContext, int max) {
        this.mContext = mContext;
        this.max = max;
    }

    @Override
    public void setChildViewListener(final CommonHolder holder, int viewType) {
        super.setChildViewListener(holder, viewType);
        ImageView mImgSelector = holder.getView(R.id.img_selector);
        ImageView mImgReduce = holder.getView(R.id.img_reduce);
        ImageView mImgAdd = holder.getView(R.id.img_add);
        final TextView mTvCurrentCount = holder.getView(R.id.tv_current_count);
        mImgSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentPosition(holder.getAdapterPosition());
            }
        });
        mImgReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseGoodsListBean mChooseGoodsListBean = getItem(holder.getAdapterPosition());
                if (mChooseGoodsListBean.getGoodsCount() > 0) {
                    mChooseGoodsListBean.setGoodsCount(mChooseGoodsListBean.getGoodsCount() - 1);
                    mTvCurrentCount.setText(String.valueOf(mChooseGoodsListBean.getGoodsCount()));
                }
            }
        });
        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseGoodsListBean mChooseGoodsListBean = getItem(holder.getAdapterPosition());
                if (mChooseGoodsListBean.getGoodsCount() < max) {
                    mChooseGoodsListBean.setGoodsCount(mChooseGoodsListBean.getGoodsCount() + 1);
                    mTvCurrentCount.setText(String.valueOf(mChooseGoodsListBean.getGoodsCount()));
                }
            }
        });
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_choose_goods_list;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {

        ChooseGoodsListBean mChooseGoodsListBean = getItem(position);

        ImageView mImgSelector = holder.getView(R.id.img_selector);
        ImageView mImgGoodsPic = holder.getView(R.id.img_goods_pic);
        TextView mTvGoodsName = holder.getView(R.id.tv_goods_name);
        TextView mTvGoodsPrice = holder.getView(R.id.tv_goods_price);
        TextView mTvCurrentCount = holder.getView(R.id.tv_current_count);

        if (mCurrentPosition == position) {
            mImgSelector.setSelected(true);
        } else {
            mImgSelector.setSelected(false);
        }
        Glide.with(mContext)
            .load(mChooseGoodsListBean.getGoodsImg())
            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(mImgGoodsPic);
        mTvGoodsName.setText(mChooseGoodsListBean.getGoodsName());
        mTvGoodsPrice.setText(
            mContext.getString(R.string.yuan_, mChooseGoodsListBean.getGoodsPrice()));
        mTvCurrentCount.setText(String.valueOf(mChooseGoodsListBean.getGoodsCount()));
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public ChooseGoodsListBean getSelectedItem() {
        if (mCurrentPosition == -1) {
            return null;
        }
        return getItem(mCurrentPosition);
    }

    public interface OnClickChildListener {

    }
}
