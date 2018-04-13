package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.BookListBeanIntfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class BookListBean implements BookListBeanIntfc {

    String goodsName;//商品名称

    int goodsCount;//商品数量

    long date;//预定时间

    int id;

    int  goods_id;

    int goods_num;

    String title;

    long ctime;


    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public String getGoodsName() {
        return goodsName;
    }

    @Override
    public int getGoodsCount() {
        return goodsCount;
    }

    @Override
    public long getDate() {
        return date;
    }

    public BookListBean(String goodsName, int goodsCount, long date) {
        this.goodsName = goodsName;
        this.goodsCount = goodsCount;
        this.date = date;
    }

    @Override
    public String toString() {
        return "BookListBean{"
            + "goodsName='"
            + goodsName
            + '\''
            + ", goodsCount="
            + goodsCount
            + ", date="
            + date
            + '}';
    }
}
