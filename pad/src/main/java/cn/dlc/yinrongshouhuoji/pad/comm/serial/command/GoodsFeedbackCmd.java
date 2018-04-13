package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 07：物品检测状态反馈指令
 */
public class GoodsFeedbackCmd extends Command {

    //public GoodsFeedbackCmd() {
    //    super(Protocol.FLAG_GOODS_FEEDBACK);
    //}

    public GoodsFeedbackCmd(byte[] bytes) {
        super(bytes);
    }

    @Override
    public boolean isSend() {
        return false;
    }

    /**
     * 仓内有物品
     *
     * @return
     */
    public boolean hasGoods() {
        return getData() == Protocol.DATA_GOODS_YES;
    }
}
