package cn.dlc.yinrongshouhuoji.home.utlis.helper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import cn.dlc.yinrongshouhuoji.R;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class DeviceAddressHelper {

    /**
     *
     * @param mContext
     * @param mTvDeviceAddress
     * @param mDeviceAddress
     */
    public static final void setDeviceAddress(Context mContext, TextView mTvDeviceAddress,
        String mDeviceAddress) {
        if (mDeviceAddress != null && mDeviceAddress.length() != 0) {
            mTvDeviceAddress.setText(mDeviceAddress);
            mTvDeviceAddress.setTextColor(ContextCompat.getColor(mContext, R.color.color_333));
        } else {
            mTvDeviceAddress.setText(mContext.getResources().getString(R.string.weisheding));
            mTvDeviceAddress.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff9557));
        }
    }
}
