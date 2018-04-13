package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.DeviceListBeanIntfc;
import java.io.Serializable;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class DeviceListBean implements DeviceListBeanIntfc,Serializable {

    String deviceNo;//设备编号

    String deviceName;//设备名称

    String deviceAddress;//设备地址

    @Override
    public String getDeviceNo() {
        return deviceNo;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getDeviceAddress() {
        return deviceAddress;
    }

    public DeviceListBean(String deviceNo, String deviceName, String deviceAddress) {
        this.deviceNo = deviceNo;
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    @Override
    public String toString() {
        return "DeviceListBean{"
            + "deviceNo='"
            + deviceNo
            + '\''
            + ", deviceName='"
            + deviceName
            + '\''
            + ", deviceAddress='"
            + deviceAddress
            + '\''
            + '}';
    }
}
