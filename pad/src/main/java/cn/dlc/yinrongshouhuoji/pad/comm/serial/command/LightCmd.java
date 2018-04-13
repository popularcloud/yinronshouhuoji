package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.comm.model.Light;

/**
 * 04：照明灯光控制指令
 * 05：格口灯光控制
 */
public abstract class LightCmd extends Command {

    public LightCmd(@Protocol.LightFlag int flag) {
        super(flag);
    }

    public LightCmd(@Protocol.LightFlag int flag, int door) {
        super(flag, door);
    }

    public LightCmd(@Protocol.LightFlag int flag, int door, int lightNo) {
        super(flag, door);
        turnLightOn(lightNo);
    }

    public LightCmd(@Protocol.LightFlag int flag, int door, Light light) {
        super(flag, door);
        setData(light.getData());
    }

    @Override
    public boolean isSend() {
        return true;
    }

    /**
     * 开启灯光
     *
     * @param lightNo 灯光编号,0表示所有灯关闭，1~7表示灯光n开启
     */
    public void turnLightOn(int lightNo) {
        int data = Protocol.DATA_LIGHT_OFF;
        switch (lightNo) {
            case 1:
                data = Protocol.DATA_LIGHT_1_ON;
                break;
            case 2:
                data = Protocol.DATA_LIGHT_2_ON;
                break;
            case 3:
                data = Protocol.DATA_LIGHT_3_ON;
                break;
            case 4:
                data = Protocol.DATA_LIGHT_4_ON;
                break;
            case 5:
                data = Protocol.DATA_LIGHT_5_ON;
                break;
            case 6:
                data = Protocol.DATA_LIGHT_6_ON;
                break;
            case 7:
                data = Protocol.DATA_LIGHT_7_ON;
                break;
        }
        setData(data);
    }
}
