package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.ChooseGoodsListBeanIntfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class ChooseGoodsListBean implements ChooseGoodsListBeanIntfc {

    String goodsImg;//商品图片

    String goodsName;//商品名称

    float goodsPrice;//商品价格

    int goodsCount;//商品数量

    int goods_id;//商品ID

    @Override
    public String getGoodsImg() {
        return goodsImg;
    }

    @Override
    public String getGoodsName() {
        return goodsName;
    }

    @Override
    public float getGoodsPrice() {
        return goodsPrice;
    }

    @Override
    public int getGoodsCount() {
        return goodsCount;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsPrice(float goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public ChooseGoodsListBean(String goodsImg, String goodsName, float goodsPrice,
        int goodsCount,int goods_id) {
        this.goodsImg = goodsImg;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsCount = goodsCount;
        this.goods_id = goods_id;
    }

    @Override
    public String toString() {
        return "ChooseGoodsListBean{"
            + "goodsImg='"
            + goodsImg
            + '\''
            + ", goodsName='"
            + goodsName
            + '\''
            + ", goodsPrice="
            + goodsPrice
            + ", goodsCount="
            + goodsCount
            + '}';
    }
}
