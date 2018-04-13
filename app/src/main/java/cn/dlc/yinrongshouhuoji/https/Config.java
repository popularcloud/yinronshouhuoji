package cn.dlc.yinrongshouhuoji.https;

/**
 * 服务器配置
 */
public class Config {

    /**
     * 对应API服务器地址
     */
    public static final String SERVER_API_ADDRESS = "http://yrshoumaiji.app.xiaozhuschool.com/api/";

    public static String token = "";
    //参数名称
    public static String api_name = "";

    //请求成功
    public static final int REQUEST_SUESS = 1;

    //发票选择地址
    public static final String INTERFACE_LOCATION = "Location/api";
    //发票新增发票、发票内容
    public static final String INTERFACE_ADD_PERSONAGE = "Personage/api";
    //登录
    public static final String INTERFACE_LOGIN = "Public/login";
    //发送短信
    public static final String INTERFACE_SENDCODE = "personage/api";
    //短信登录
    public static final String INTERFACE_SENDCODE_LOGIN = "public/sendCode";

    //开箱
    public static final String INTERFACE_BOX_API = "Box/api";

    public static final String INTERFACE_MINE = "Deliveryman/api";

    public static final String INTERFACE_MAIN_LIST = "Business/api";



}
