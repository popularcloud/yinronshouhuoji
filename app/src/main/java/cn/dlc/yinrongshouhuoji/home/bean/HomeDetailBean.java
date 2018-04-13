package cn.dlc.yinrongshouhuoji.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Denggz on 2018/3/24 17:17
 * 描述：
 */

public class HomeDetailBean implements Serializable {


    /**
     * info : {"cupboard_id":7,"title":"小光1号","device_number":"z1","address":"","box_empty_num":0,"box_err_num":0,"box_have_num":0,"box_back_num":0,"reserve":"","overdue":4}
     * boxes : [{"box_id":46,"box_number":"1","opened":0,"status":0},{"box_id":47,"box_number":"2","opened":0,"status":0},{"box_id":48,"box_number":"3","opened":0,"status":0},{"box_id":49,"box_number":"4","opened":0,"status":5},{"box_id":50,"box_number":"5","opened":0,"status":0},{"box_id":51,"box_number":"6","opened":0,"status":5},{"box_id":52,"box_number":"7","opened":0,"status":5},{"box_id":53,"box_number":"8","opened":0,"status":5},{"box_id":54,"box_number":"9","opened":0,"status":0},{"box_id":55,"box_number":"a","opened":0,"status":0}]
     */

    private InfoBean info;
    private List<BoxesBean> boxes;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<BoxesBean> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<BoxesBean> boxes) {
        this.boxes = boxes;
    }

    public static class InfoBean implements Serializable{
        /**
         * cupboard_id : 7
         * title : 小光1号
         * device_number : z1
         * address :
         * box_empty_num : 0
         * box_err_num : 0
         * box_have_num : 0
         * box_back_num : 0
         * reserve :
         * overdue : 4
         */

        private int cupboard_id;
        private String title;
        private String device_number;
        private String address;
        private int box_empty_num;
        private int box_err_num;
        private int box_have_num;
        private int box_back_num;
        private int reserve;
        private int overdue;

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

        public String getDevice_number() {
            return device_number;
        }

        public void setDevice_number(String device_number) {
            this.device_number = device_number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBox_empty_num() {
            return box_empty_num;
        }

        public void setBox_empty_num(int box_empty_num) {
            this.box_empty_num = box_empty_num;
        }

        public int getBox_err_num() {
            return box_err_num;
        }

        public void setBox_err_num(int box_err_num) {
            this.box_err_num = box_err_num;
        }

        public int getBox_have_num() {
            return box_have_num;
        }

        public void setBox_have_num(int box_have_num) {
            this.box_have_num = box_have_num;
        }

        public int getBox_back_num() {
            return box_back_num;
        }

        public void setBox_back_num(int box_back_num) {
            this.box_back_num = box_back_num;
        }

        public int getReserve() {
            return reserve;
        }

        public void setReserve(int reserve) {
            this.reserve = reserve;
        }

        public int getOverdue() {
            return overdue;
        }

        public void setOverdue(int overdue) {
            this.overdue = overdue;
        }
    }

    public static class BoxesBean implements Serializable{
        /**
         * box_id : 46
         * box_number : 1
         * opened : 0
         * status : 0
         */

        private int box_id;
        private String box_number;
        private int opened;
        private int status;

        public int getBox_id() {
            return box_id;
        }

        public void setBox_id(int box_id) {
            this.box_id = box_id;
        }

        public String getBox_number() {
            return box_number;
        }

        public void setBox_number(String box_number) {
            this.box_number = box_number;
        }

        public int getOpened() {
            return opened;
        }

        public void setOpened(int opened) {
            this.opened = opened;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
