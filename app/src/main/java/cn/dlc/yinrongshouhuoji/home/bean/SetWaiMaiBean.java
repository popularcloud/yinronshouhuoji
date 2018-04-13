package cn.dlc.yinrongshouhuoji.home.bean;

import java.io.Serializable;

/**
 * 作者：Denggz on 2018/3/26 10:50
 * 描述：
 */

public class SetWaiMaiBean implements Serializable{


    /**
     * code : 1
     * msg : 可用仓数量
     * data : 10
     */

    private int code;
    private String msg;
    private int data;
    private int box_empty_num;
    private int put_log;

    public int getPut_log() {
        return put_log;
    }

    public void setPut_log(int put_log) {
        this.put_log = put_log;
    }

    public int getBox_empty_num() {
        return box_empty_num;
    }

    public void setBox_empty_num(int box_empty_num) {
        this.box_empty_num = box_empty_num;
    }

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
