package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.AddressListBeanIntfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class AddressListBean implements AddressListBeanIntfc {

    String addressArea;//地址地区
    String addressDetail;//详细地址

    @Override
    public String getAddressArea() {
        return addressArea;
    }

    @Override
    public String getAddressDetail() {
        return addressDetail;
    }

    public AddressListBean(String addressArea, String addressDetail) {
        this.addressArea = addressArea;
        this.addressDetail = addressDetail;
    }

    @Override
    public String toString() {
        return "AddressListBean{"
            + "addressArea='"
            + addressArea
            + '\''
            + ", addressDetail='"
            + addressDetail
            + '\''
            + '}';
    }
}
