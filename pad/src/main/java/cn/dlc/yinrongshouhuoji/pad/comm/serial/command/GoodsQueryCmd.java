package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 06：物品检测查询指令
 */
public class GoodsQueryCmd extends Command {

    public GoodsQueryCmd() {
        super(Protocol.FLAG_GOODS_QUERY);
        setData(Protocol.DATA_NONE);
    }

    public GoodsQueryCmd(int door) {
        super(Protocol.FLAG_GOODS_QUERY, door);
        setData(Protocol.DATA_NONE);
    }

    @Override
    public boolean isSend() {
        return true;
    }
}
