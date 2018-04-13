package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

/**
 * Created by John on 2018/3/30.
 */

public class ConfirmCmd extends Command {

    //public ConfirmCmd() {
    //    super(Protocol.FLAG_CONFIRM);
    //}

    public ConfirmCmd(byte[] bytes) {
        super(bytes);
    }

    @Override
    public boolean isSend() {
        return false;
    }
}
