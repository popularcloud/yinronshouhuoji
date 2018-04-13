package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.MessageListBeanIntfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class MessageListBean implements MessageListBeanIntfc {

    String title;//标题

    String content;//内容

    long date;//时间

    int status;//消息状态 0未接单 1已接单 2通知类（具体看后台）

    //未接单情况下
    String phone;//用户电话

    String goodsName;//商品名称

    long bookDate;//预约配送时间

    int message_id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setBookDate(long bookDate) {
        this.bookDate = bookDate;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getGoodsName() {
        return goodsName;
    }

    @Override
    public long getBookDate() {
        return bookDate;
    }

    public MessageListBean(String title, String content, int status,
        int message_id, long bookDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.message_id = message_id;
        this.bookDate = bookDate;
    }

    @Override
    public String toString() {
        return "MessageListBean{"
            + "title='"
            + title
            + '\''
            + ", content='"
            + content
            + '\''
            + ", date="
            + date
            + ", status="
            + status
            + ", phone='"
            + phone
            + '\''
            + ", goodsName='"
            + goodsName
            + '\''
            + ", bookDate="
            + bookDate
            + '}';
    }
}
