package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.comm.model.Light;

/**
 * 04：照明灯光控制指令
 */

public class LightingLightCmd extends LightCmd {
    
    public LightingLightCmd() {
        super(Protocol.FLAG_LIGHTING_LIGHT);
    }

    public LightingLightCmd(int door) {
        super(Protocol.FLAG_LIGHTING_LIGHT, door);
    }

    public LightingLightCmd(int door, int lightNo) {
        super(Protocol.FLAG_LIGHTING_LIGHT, door, lightNo);
    }

    public LightingLightCmd(int door, Light light) {
        super(Protocol.FLAG_LIGHTING_LIGHT, door, light);
    }
}
