package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 11：制冷机1控制
 * 12：制冷机2控制
 * 13：制冷机3控制
 */
public class CoolCtrlCmd extends Command {

    public CoolCtrlCmd(@Protocol.CoolFlag int flag) {
        super(flag);
    }

    public CoolCtrlCmd(@Protocol.CoolFlag int flag, int door) {
        super(flag, door);
    }

    public CoolCtrlCmd(@Protocol.CoolFlag int flag, int door, boolean on) {
        super(flag, door);
        setCoolOn(on);
    }

    @Override
    public boolean isSend() {
        return true;
    }

    /**
     * 设置制冷机开启或者关闭
     *
     * @param on
     * @return
     */
    public void setCoolOn(boolean on) {
        if (on) {
            setData(Protocol.DATA_COOL_ON);
        } else {
            setData(Protocol.DATA_COOL_OFF);
        }
    }
}
