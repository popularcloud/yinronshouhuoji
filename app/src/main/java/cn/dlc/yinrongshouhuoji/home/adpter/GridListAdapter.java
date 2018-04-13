package cn.dlc.yinrongshouhuoji.home.adpter;

import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.GridListBean;
import cn.dlc.yinrongshouhuoji.home.bean.HomeDetailBean;
import cn.dlc.yinrongshouhuoji.home.utlis.helper.GridListHelper;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class GridListAdapter extends BaseRecyclerAdapter<HomeDetailBean.BoxesBean> {
    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_grid_list;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        HomeDetailBean.BoxesBean mGridListBean = getItem(position);
        TextView mTvGrid = holder.getView(R.id.tv_grid);
        mTvGrid.setText(mGridListBean.getBox_number());
        GridListHelper.setGrid(mTvGrid, mGridListBean.getStatus());
    }
}
