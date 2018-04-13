package cn.dlc.yinrongshouhuoji.personal.adapter;

import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.MenuBean;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class OtherGreensAdapter extends BaseRecyclerAdapter<MenuBean> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_allgreensitem;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.CommonHolder holder, int position) {
        MenuBean bean = getItem(position);
        holder.setText(R.id.adapter_all_item_geers_tv,bean.getGreensName());
        holder.setText(R.id.adapter_all_item_price_tv,bean.getPrice());
        holder.setText(R.id.adapter_all_item_mub_tv,bean.getGresensNumb());
    }
}
