package cn.dlc.yinrongshouhuoji.home.bean;

import java.io.Serializable;

/**
 * 作者：Denggz on 2018/3/21 10:11
 * 描述：登录成功
 */

public class loginBean implements Serializable {

    /**
     * id : 12
     * contact_id : 0
     * avater_id : 0
     * nickname : 小光
     * username : null
     * sex : 1
     * phone : 18589077695
     * password : e10adc3949ba59abbe56e057f20f883e
     * user_type : 2
     * token : 650e6fa8e5be6eae90b9d0f8e6c98fed
     * money : 0
     * score : 0
     * status : 1
     * buy_num : 0
     * remark : null
     * last_login_ip : null
     * seller_id : 5
     * ltime : 0
     * ctime : 1521595807
     * utime : 0
     */

    private int id;
    private int contact_id;
    private int avater_id;
    private String nickname;
    private Object username;
    private int sex;
    private String phone;
    private String password;
    private int user_type;
    private String token;
    private int money;
    private int score;
    private int status;
    private int buy_num;
    private Object remark;
    private Object last_login_ip;
    private int seller_id;
    private int ltime;
    private int ctime;
    private int utime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getAvater_id() {
        return avater_id;
    }

    public void setAvater_id(int avater_id) {
        this.avater_id = avater_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(int buy_num) {
        this.buy_num = buy_num;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(Object last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public int getLtime() {
        return ltime;
    }

    public void setLtime(int ltime) {
        this.ltime = ltime;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public int getUtime() {
        return utime;
    }

    public void setUtime(int utime) {
        this.utime = utime;
    }
}
