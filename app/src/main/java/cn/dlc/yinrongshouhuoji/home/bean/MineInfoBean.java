package cn.dlc.yinrongshouhuoji.home.bean;

import java.io.Serializable;

/**
 * 作者：Denggz on 2018/3/26 16:38
 * 描述：
 */

public class MineInfoBean implements Serializable{

    /**
     * nickname : 小光
     * phone : 18589077695
     * image : http://yrshoumaiji.app.xiaozhuschool.com/public/uploads/
     */

    private String nickname;
    private String phone;
    private String image;
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
