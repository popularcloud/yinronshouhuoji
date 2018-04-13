package cn.dlc.yinrongshouhuoji.home.bean;

import java.util.List;

/**
 * 作者：Denggz on 2018/3/26 13:21
 * 描述：
 */

public class setWaiMaiListBean{


    /**
     * put_log : 1
     * goods : [{"goods_id":5,"title":"海鲜饭","price":"10.00","cover_url":"http://yrshoumaiji.app.xiaozhuschool.com/public/static/dist/img/noimage.gif"}]
     */

    private String put_log;
    String box_number ;
    private List<GoodsBean> goods;

    public String getBox_number() {
        return box_number;
    }

    public void setBox_number(String box_number) {
        this.box_number = box_number;
    }

    public String getPut_log() {
        return put_log;
    }

    public void setPut_log(String put_log) {
        this.put_log = put_log;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goods_id : 5
         * title : 海鲜饭
         * price : 10.00
         * cover_url : http://yrshoumaiji.app.xiaozhuschool.com/public/static/dist/img/noimage.gif
         */

        private int goods_id;
        private String title;
        private String price;
        private String cover_url;

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }
    }
}
