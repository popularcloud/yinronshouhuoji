package cn.dlc.yinrongshouhuoji.home.bean.intfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public interface MessageListBeanIntfc {
    String getTitle();//标题

    String getContent();//内容

    long getDate();//时间

    int getStatus();//消息状态 0未接单 1已接单 2通知类（具体看后台）

    //未接单情况下
    String getPhone();//用户电话

    String getGoodsName();//商品名称

    long getBookDate();//预约配送时间
}
