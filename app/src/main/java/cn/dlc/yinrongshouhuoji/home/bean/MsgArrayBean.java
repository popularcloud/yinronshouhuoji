package cn.dlc.yinrongshouhuoji.home.bean;

/**
 * 作者：Denggz on 2018/3/26 16:41
 * 描述：
 */

public class MsgArrayBean {


    /**
     * message_id : 19
     * type : 1
     * content : 客户：jerry预约商品名称: 牛肉饭预约配送时间: 2018-03-16 20:20:20请注意处理
     * status : 0
     * ctime : 1521534640
     */

    private int message_id;
    private String type;
    private String content;
    private int status;
    private int ctime;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }
}
