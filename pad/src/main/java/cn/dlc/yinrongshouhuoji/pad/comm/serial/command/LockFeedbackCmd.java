package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 03：锁状态反馈指令
 */
public class LockFeedbackCmd extends Command {

    //public LockFeedbackCmd() {
    //    super(Protocol.FLAG_LOCK_FEEDBACK);
    //}

    public LockFeedbackCmd(byte[] bytes) {
        super(bytes);
    }

    @Override
    public boolean isSend() {
        return false;
    }

    /**
     * 是否锁着
     *
     * @return
     */
    public boolean isLocked() {
        return getData() == Protocol.DATA_LOCK_ON;
    }
}
