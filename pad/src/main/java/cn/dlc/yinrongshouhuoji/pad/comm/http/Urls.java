package cn.dlc.yinrongshouhuoji.pad.comm.http;

/**
 * Created by John on 2018/4/4.
 */

public class Urls {

    public static final String BASE_URL = "http://yrshoumaiji.app.xiaozhuschool.com/";
    public static final String PUBLIC_ADVERTISEMENT = appendUrl("api/public/advertisement");
    public static final String PUBLIC_SENDCODE = appendUrl("api/public/sendCode");
    public static final String PUBLIC_CUPBOARDLOGIN = appendUrl("api/Public/cupboardLogin");
    public static final String BOX = appendUrl("api/Box/api");
    
    

    private static String appendUrl(String subUrl) {
        return BASE_URL + subUrl;
    }
}
