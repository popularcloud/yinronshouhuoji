package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.TakeOutListBeanIntfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class TakeOutListBean implements TakeOutListBeanIntfc {


    /**
     * box_number : 8
     * goods_id : 3
     * put_time : 0
     * title : 番茄炒饭
     * goods_num : 1
     */

    private String box_number;
    private int goods_id;
    private int put_time;
    private String title;
    private int goods_num;

    public String getBox_number() {
        return box_number;
    }

    public void setBox_number(String box_number) {
        this.box_number = box_number;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getPut_time() {
        return put_time;
    }

    public void setPut_time(int put_time) {
        this.put_time = put_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    @Override
    public String getGridNo() {
        return null;
    }

    @Override
    public String getGoodsName() {
        return null;
    }

    @Override
    public int getGoodsCount() {
        return 0;
    }

    @Override
    public long getDate() {
        return 0;
    }
}
