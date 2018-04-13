package cn.dlc.yinrongshouhuoji.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Denggz on 2018/3/21 11:23
 * 描述：售货机列表
 */

public class SaleMachineListBean {


    /**
     * code : 1
     * msg : 获取成功
     * data : [{"cupboard_id":7,"title":"小光1号","address":""}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * cupboard_id : 7
         * title : 小光1号
         * address :
         */
        public DataBean(String deviceNo, String deviceName, String deviceAddress,int cupboard_id) {
            this.deviceNo = deviceNo;
            this.title = deviceName;
            this.address = deviceAddress;
            this.cupboard_id = cupboard_id;
        }



        private int cupboard_id;
        private String title;
        private String address;
        private String deviceNo;

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public int getCupboard_id() {
            return cupboard_id;
        }

        public void setCupboard_id(int cupboard_id) {
            this.cupboard_id = cupboard_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
