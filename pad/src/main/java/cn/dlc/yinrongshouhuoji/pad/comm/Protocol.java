package cn.dlc.yinrongshouhuoji.pad.comm;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 协议
 */
public interface Protocol {

    /**
     * 帧头
     */
    byte FRAME_HEAD = 0x1B;
    /**
     * 帧尾部0，一起是0x0D0A
     */
    byte FRAME_FOOT_0 = 0x0D;
    /**
     * 帧尾部1，一起是0x0D0A
     */
    byte FRAME_FOOT_1 = 0x0A;
    /**
     * 包长度
     */
    int PACKAGE_LENGTH = 7;

    /**
     * 仓口最大数目
     */
    int MAX_DOOR_AMOUNT = 256 - 2;

    /**
     * 发送数据后，接收数据超时时间，毫秒
     */
    long RECEIVE_TIME_OUT = 40;

    /**
     * 两次接收数据之间，判断已不再会有数据的超时时间，毫秒
     */
    long RECEIVE_INTERVAL_TIME_OUT = 10;
    
    
    

    ////////////////////  特征码  ////////////////////////////////////

    /**
     * 特征码01：锁开启控制指令，主机→控制板
     */
    int FLAG_LOCK_OPEN = 0x01;
    /**
     * 特征码02：锁状态查询指令，主机→控制板
     */
    int FLAG_LOCK_QUERY = 0x02;
    /**
     * 特征码   * 03：锁状态反馈指令
     */
    int FLAG_LOCK_FEEDBACK = 0x03;

    /**
     * 特征码04：照明灯光控制指令，主机→控制板
     */
    int FLAG_LIGHTING_LIGHT = 0x04;
    /**
     * 特征码05：格口灯光控制，主机→控制板
     */
    int FLAG_DOOR_LIGHT = 0x05;
    /**
     * 特征码06：物品检测查询指令,主机→控制板
     */
    int FLAG_GOODS_QUERY = 0x06;
    /**
     * 特征码07：物品检测状态反馈指令,控制板→主机
     */
    int FLAG_GOODS_FEEDBACK = 0x07;

    /**
     * 特征码08：温区1温度查询指令,控制板→主机
     */
    int FLAG_TEMP_QUERY_1 = 0x08;
    /**
     * 特征码09：温区2温度查询指令,主机→控制板
     */
    int FLAG_TEMP_QUERY_2 = 0x09;
    /**
     * 特征码0a：温区3温度查询指令,主机→控制板
     */
    int FLAG_TEMP_QUERY_3 = 0x0a;
    /**
     * 特征码0b：温区1温度反馈指令,控制板→主机
     */
    int FLAG_TEMP_FEEDBACK_1 = 0x0b;

    /**
     * 特征码0c：温区2温度反馈指令,控制板→主机
     */
    int FLAG_TEMP_FEEDBACK_2 = 0x0c;

    /**
     * 特征码0d：温区3温度反馈指令,控制板→主机
     */
    int FLAG_TEMP_FEEDBACK_3 = 0x0d;

    /**
     * 特征码11：制冷机1控制,主机→控制板
     */
    int FLAG_COOL_CTRL_1 = 0x11;
    /**
     * 特征码12：制冷机2控制,主机→控制板
     */
    int FLAG_COOL_CTRL_2 = 0x12;
    /**
     * 特征码13：制冷机3控制,主机→控制板
     */
    int FLAG_COOL_CTRL_3 = 0x13;
    /**
     * 特征码AA：确认命令,控制板→主机
     */
    int FLAG_CONFIRM = 0xaa;

    /**
     * 特征码
     */
    @IntDef({
        FLAG_LOCK_OPEN, FLAG_LOCK_QUERY, FLAG_LOCK_FEEDBACK, FLAG_LIGHTING_LIGHT, FLAG_DOOR_LIGHT,
        FLAG_GOODS_QUERY, FLAG_GOODS_FEEDBACK, FLAG_TEMP_QUERY_1, FLAG_TEMP_QUERY_2,
        FLAG_TEMP_QUERY_3, FLAG_TEMP_FEEDBACK_1, FLAG_TEMP_FEEDBACK_2, FLAG_TEMP_FEEDBACK_3,
        FLAG_COOL_CTRL_1, FLAG_COOL_CTRL_2, FLAG_COOL_CTRL_3, FLAG_CONFIRM
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flag {
    }

    /**
     * 灯光特征码
     */
    @IntDef({
        FLAG_LIGHTING_LIGHT, FLAG_DOOR_LIGHT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LightFlag {
    }

    /**
     * 温度查询特征码
     */
    @IntDef({
        FLAG_TEMP_QUERY_1, FLAG_TEMP_QUERY_2, FLAG_TEMP_QUERY_3
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TempQueryFlag {
    }

    /**
     * 温度反馈指令特征码
     */
    @IntDef({
        FLAG_TEMP_FEEDBACK_1, FLAG_TEMP_FEEDBACK_2, FLAG_TEMP_FEEDBACK_3
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TempFeedbackFlag {
    }

    /**
     * 制冷机控制特征码
     */
    @IntDef({
        FLAG_COOL_CTRL_1, FLAG_COOL_CTRL_2, FLAG_COOL_CTRL_3
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface CoolFlag {
    }

    /////////////////////////////////////// 窗口号  ///////////////////////////////////////

    /**
     * 仓口号，00表示非仓口命令
     */
    int DOOR_NONE = 0x00;
    /**
     * 仓口号，ff标识对所有仓口进行操作
     */
    int DOOR_ALL = 0xff;

    //////////////////////////////////// 数据位 //////////////////////////////////////////////

    /**
     * 数据位，AA：无数据代表查询指令
     */
    int DATA_NONE = 0xAA;

    /**
     * 数据位，A1：锁开启命令
     */
    int DATA_LOCK_OPEN = 0xA1;
    /**
     * 数据位，A2：锁状态为开启(门开着的）
     */
    int DATA_LOCK_OFF = 0xA2;
    /**
     * 数据位，A3：锁状态为关闭（门锁着的）
     */
    int DATA_LOCK_ON = 0xA3;

    /**
     * 数据位，B1：灯光1开启
     */
    int DATA_LIGHT_1_ON = 0xB1;
    /**
     * 数据位，B2：灯光2开启
     */
    int DATA_LIGHT_2_ON = 0xB2;
    /**
     * 数据位，B3：灯光3开启
     */
    int DATA_LIGHT_3_ON = 0xB3;
    /**
     * 数据位，B4：灯光4开启
     */
    int DATA_LIGHT_4_ON = 0xB4;
    /**
     * 数据位，B5：灯光5开启
     */
    int DATA_LIGHT_5_ON = 0xB5;
    /**
     * 数据位，B6：灯光6开启
     */
    int DATA_LIGHT_6_ON = 0xB6;
    /**
     * 数据位，B7：灯光7开启
     */
    int DATA_LIGHT_7_ON = 0xB7;
    /**
     * 数据位，B8：所有灯光关闭
     */
    int DATA_LIGHT_OFF = 0xB8;

    /**
     * 数据位,所有灯关控制数据位
     */
    int[] DATA_LIGHT_CTRLS = {
        DATA_LIGHT_OFF, DATA_LIGHT_1_ON, DATA_LIGHT_2_ON, DATA_LIGHT_3_ON, DATA_LIGHT_4_ON,
        DATA_LIGHT_5_ON, DATA_LIGHT_6_ON, DATA_LIGHT_7_ON
    };

    /**
     * 数据位，A4：仓内有物品
     */
    int DATA_GOODS_YES = 0xA4;

    /**
     * 数据位,A5：仓内无物品
     */
    int DATA_GOODS_NO = 0xA5;

    /**
     * 数据位,最小温度值 0 ,取值范围（0-48度）
     */
    int DATA_MIN_TEMP = 0x00;
    /**
     * 数据位,最大温度值 48 ,取值范围（0-48度）
     */
    int DATA_MAX_TEMP = 0x30;

    /**
     * 数据位,A6：制冷机开启
     */
    int DATA_COOL_ON = 0xA6;
    /**
     * 数据位,A7：制冷机关闭
     */
    int DATA_COOL_OFF = 0xA7;
}
