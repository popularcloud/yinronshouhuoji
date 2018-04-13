package cn.dlc.yinrongshouhuoji.personal.bean;


import java.util.ArrayList;

import cn.dlc.yinrongshouhuoji.home.bean.MenuBean;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class AllBean {
    public  int greesType;
    
    public ArrayList<MenuBean> mList;

    public int getGreesType() {
        return greesType;
    }

    public void setGreesType(int greesType) {
        this.greesType = greesType;
    }

    public ArrayList<MenuBean> getList() {
        return mList;
    }

    public void setList(ArrayList<MenuBean> list) {
        mList = list;
    }

    @Override
    public String toString() {
        return "AllBean{" + "greesType='" + greesType + '\'' + ", mList=" + mList + '}';
    }
}
