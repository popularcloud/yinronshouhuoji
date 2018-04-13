package cn.dlc.yinrongshouhuoji.home.bean;

import java.util.List;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class MenuBean {


    /**
     * order_id : 64
     * goods_num : 1
     * status : 0
     * goods_detail : [{"title":"蛋炒饭","cover_url":"http://yrshoumaiji.app.xiaozhuschool.com/public/uploads/20180328/6e1f3019fb4c4d3c012547b76c5bac12.jpg","price":"8.00","desc":"<p>蛋炒饭123<\/p>","poster_url":"http://yrshoumaiji.app.xiaozhuschool.com/public/uploads/avatar/14173589-4bae-4dec-a17c-039f762a6c5d.png","num":1}]
     */

    private int order_id;
    private int goods_num;
    private int status;

    private String greensName;
    private String price;
    private String gresensNumb;
    private String cover_url;


    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getGreensName() {
        return greensName;
    }

    public void setGreensName(String greensName) {
        this.greensName = greensName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGresensNumb() {
        return gresensNumb;
    }

    public void setGresensNumb(String gresensNumb) {
        this.gresensNumb = gresensNumb;
    }

    private List<GoodsDetailBean> goods_detail;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GoodsDetailBean> getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(List<GoodsDetailBean> goods_detail) {
        this.goods_detail = goods_detail;
    }

    public static class GoodsDetailBean {
        /**
         * title : 蛋炒饭
         * cover_url : http://yrshoumaiji.app.xiaozhuschool.com/public/uploads/20180328/6e1f3019fb4c4d3c012547b76c5bac12.jpg
         * price : 8.00
         * desc : <p>蛋炒饭123</p>
         * poster_url : http://yrshoumaiji.app.xiaozhuschool.com/public/uploads/avatar/14173589-4bae-4dec-a17c-039f762a6c5d.png
         * num : 1
         */

        private String title;
        private String cover_url;
        private String price;
        private String desc;
        private String poster_url;
        private int num;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPoster_url() {
            return poster_url;
        }

        public void setPoster_url(String poster_url) {
            this.poster_url = poster_url;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
