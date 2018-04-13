package cn.dlc.yinrongshouhuoji.home.adpter;

import android.content.Context;
import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.TakeOutListBean;
import java.text.SimpleDateFormat;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class TakeOutListAdapter extends BaseRecyclerAdapter<TakeOutListBean> {

    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;

    public TakeOutListAdapter(Context mContext) {
        this.mContext = mContext;
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_take_out_list;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        TakeOutListBean mTakeOutListBean = getItem(position);
        TextView mTvGridNo = holder.getView(R.id.tv_grid_no);
        TextView mTvGoods = holder.getView(R.id.tv_goods);
        TextView mTvDate = holder.getView(R.id.tv_date);
        mTvGridNo.setText(mTakeOutListBean.getBox_number()+"");
        mTvGoods.setText(mContext.getString(R.string._fen, mTakeOutListBean.getTitle(),
            mTakeOutListBean.getGoods_num()));
        mTvDate.setText(mSimpleDateFormat.format(mTakeOutListBean.getDate() * 1000L));
    }
}
