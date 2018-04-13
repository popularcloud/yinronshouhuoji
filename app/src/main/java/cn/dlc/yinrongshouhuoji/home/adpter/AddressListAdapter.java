package cn.dlc.yinrongshouhuoji.home.adpter;

import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.AddressListBean;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class AddressListAdapter extends BaseRecyclerAdapter<AddressListBean> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_address_list;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        AddressListBean mAddressListBean = getItem(position);

        TextView mTvAddressArea = holder.getView(R.id.tv_address_area);
        TextView mTvAddressDetail = holder.getView(R.id.tv_address_detail);
        mTvAddressArea.setText(mAddressListBean.getAddressArea());
        mTvAddressDetail.setText(mAddressListBean.getAddressDetail());
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }
}
