package cn.dlc.yinrongshouhuoji.home.adpter;

import android.content.Context;
import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.utlis.helper.DeviceAddressHelper;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class DeviceListAdapter extends BaseRecyclerAdapter<SaleMachineListBean.DataBean> {


    private Context mContext;

    public DeviceListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_device_list;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        SaleMachineListBean.DataBean mDeviceListBean = getItem(position);
        TextView mTvDeviceNo = holder.getView(R.id.tv_device_no);
        TextView mTvDeviceName = holder.getView(R.id.tv_device_name);
        TextView mTvDeviceAddress = holder.getView(R.id.tv_device_address);
        mTvDeviceNo.setText(mDeviceListBean.getCupboard_id()+"");
        mTvDeviceName.setText(mDeviceListBean.getTitle());
        DeviceAddressHelper.setDeviceAddress(mContext, mTvDeviceAddress,
            mDeviceListBean.getAddress());
    }
}
