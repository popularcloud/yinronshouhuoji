package cn.dlc.yinrongshouhuoji.pad.comm.http.bean;

/**
 * Created by John on 2018/4/8.
 */

public class LoginBean {

    /**
     * code : 1
     * msg : 登入成功
     * data : {"id":7,"contact_id":0,"avater_id":0,"nickname":"傲娇迪","username":"didi","sex":1,"phone":"15399871063","password":null,"user_type":2,"token":"5bfc6818bda4b9069d8feb508fd8019a","money":0,"score":0,"status":1,"buy_num":0,"remark":null,"last_login_ip":null,"seller_id":0,"ltime":0,"ctime":0,"utime":0}
     */

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * id : 7
         * contact_id : 0
         * avater_id : 0
         * nickname : 傲娇迪
         * username : didi
         * sex : 1
         * phone : 15399871063
         * password : null
         * user_type : 2
         * token : 5bfc6818bda4b9069d8feb508fd8019a
         * money : 0
         * score : 0
         * status : 1
         * buy_num : 0
         * remark : null
         * last_login_ip : null
         * seller_id : 0
         * ltime : 0
         * ctime : 0
         * utime : 0
         */

        public int id;
        public int contact_id;
        public int avater_id;
        public String nickname;
        public String username;
        public int sex;
        public String phone;
        public String password;
        public int user_type;
        public String token;
        public int money;
        public int score;
        public int status;
        public int buy_num;
        public String remark;
        public String last_login_ip;
        public String seller_id;
        public long ltime;
        public long ctime;
        public long utime;
    }
}
