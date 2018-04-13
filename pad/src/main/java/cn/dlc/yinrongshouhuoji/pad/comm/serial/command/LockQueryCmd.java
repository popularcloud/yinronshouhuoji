package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 02：锁状态查询指令
 */
public class LockQueryCmd extends Command {

    public LockQueryCmd() {
        super(Protocol.FLAG_LOCK_QUERY);
        setData(Protocol.DATA_NONE);
    }

    public LockQueryCmd(int door) {
        super(Protocol.FLAG_LOCK_QUERY, door);
        setData(Protocol.DATA_NONE);
    }

    @Override
    public boolean isSend() {
        return true;
    }
}
