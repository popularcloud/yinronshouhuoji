package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 08：温区1温度查询指令
 * 09：温区2温度查询指令
 * 0a：温区3温度查询指令
 */
public class TempQueryCmd extends Command {

    public TempQueryCmd(@Protocol.TempQueryFlag int flag) {
        super(flag);
        setData(Protocol.DATA_NONE);
    }

    public TempQueryCmd(@Protocol.TempQueryFlag int flag, int door) {
        super(flag, door);
        setData(Protocol.DATA_NONE);
    }

    @Override
    public boolean isSend() {
        return true;
    }
}
