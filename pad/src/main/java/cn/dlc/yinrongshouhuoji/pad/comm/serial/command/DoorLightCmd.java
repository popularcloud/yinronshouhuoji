package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.comm.model.Light;

/**
 * 05：格口灯光控制
 */

public class DoorLightCmd extends LightCmd {

    public DoorLightCmd() {
        super(Protocol.FLAG_DOOR_LIGHT);
    }

    public DoorLightCmd(int door) {
        super(Protocol.FLAG_DOOR_LIGHT, door);
    }

    public DoorLightCmd(int door, int lightNo) {
        super(Protocol.FLAG_DOOR_LIGHT, door, lightNo);
    }

    public DoorLightCmd(int door, Light light) {
        super(Protocol.FLAG_DOOR_LIGHT, door, light);
    }
}
