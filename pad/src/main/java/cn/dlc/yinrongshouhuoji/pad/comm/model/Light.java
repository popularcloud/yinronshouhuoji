package cn.dlc.yinrongshouhuoji.pad.comm.model;

import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_1_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_2_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_3_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_4_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_5_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_6_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_7_ON;
import static cn.dlc.yinrongshouhuoji.pad.comm.Protocol.DATA_LIGHT_OFF;

/**
 * 灯光
 */
public enum Light {

    /**
     * 灯光0，即关闭
     */
    LV0(DATA_LIGHT_OFF),

    /**
     * 灯光1
     */
    LV1(DATA_LIGHT_1_ON),

    /**
     * 灯光2
     */
    LV2(DATA_LIGHT_3_ON),

    /**
     * 灯光3
     */
    LV3(DATA_LIGHT_4_ON),

    /**
     * 灯光4
     */
    LV4(DATA_LIGHT_5_ON),

    /**
     * 灯光5
     */
    LV5(DATA_LIGHT_6_ON),

    /**
     * 灯光6
     */
    LV6(DATA_LIGHT_7_ON),

    /**
     * 异常灯光
     */
    ABNORMAL(DATA_LIGHT_2_ON);

    private int data;

    Light(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public static Light getLv(int level) {
        if (level < 0 || level >= values().length) {
            return ABNORMAL;
        } else {
            return values()[level];
        }
    }

}
