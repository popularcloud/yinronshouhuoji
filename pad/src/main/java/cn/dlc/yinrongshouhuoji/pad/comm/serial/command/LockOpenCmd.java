package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 01：锁开启控制指令
 */
public class LockOpenCmd extends Command {

    public LockOpenCmd() {
        super(Protocol.FLAG_LOCK_OPEN);
        setData(Protocol.DATA_LOCK_OPEN);
    }

    public LockOpenCmd(int door) {
        super(Protocol.FLAG_LOCK_OPEN, door);
        setData(Protocol.DATA_LOCK_OPEN);
    }

    @Override
    public boolean isSend() {
        return true;
    }
}
