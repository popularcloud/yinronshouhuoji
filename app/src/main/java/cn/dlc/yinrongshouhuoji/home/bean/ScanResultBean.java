package cn.dlc.yinrongshouhuoji.home.bean;

import java.io.Serializable;

/**
 * 作者：Denggz on 2018/3/26 09:56
 * 描述：
 */

public class ScanResultBean implements Serializable{


    /**
     * cupboard_id : 7
     * station_id : 4
     * device_number : z1
     * title : 小光1号
     */

    private int cupboard_id;
    private int station_id;
    private String device_number;
    private String title;

    public int getCupboard_id() {
        return cupboard_id;
    }

    public void setCupboard_id(int cupboard_id) {
        this.cupboard_id = cupboard_id;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
