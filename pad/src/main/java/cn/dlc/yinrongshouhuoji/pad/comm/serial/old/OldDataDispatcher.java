package cn.dlc.yinrongshouhuoji.pad.comm.serial.old;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.ConfirmCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.GoodsFeedbackCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockFeedbackCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.TempFeedbackCmd;

/**
 * 分发收到的消息
 */
public class OldDataDispatcher {

    public OldDataDispatcher() {
    }

    public void dispatch(byte[] bytes) {
        
 

        @Protocol.Flag int flag = 0xff & bytes[2];
        switch (flag) {
            case Protocol.FLAG_LOCK_FEEDBACK:
                recvLookFeedback(bytes);
                break;
            case Protocol.FLAG_GOODS_FEEDBACK:
                recvGoodsFeedback(bytes);
                break;
            case Protocol.FLAG_TEMP_FEEDBACK_1:
            case Protocol.FLAG_TEMP_FEEDBACK_2:
            case Protocol.FLAG_TEMP_FEEDBACK_3:
                receTempFeedback(bytes);
                break;
            case Protocol.FLAG_CONFIRM:
                recvConfirm(bytes);
                break;
            default:
                break;
        }
    }

    /**
     * 收到 03：锁状态反馈指令
     *
     * @param bytes
     */
    private void recvLookFeedback(byte[] bytes) {
        LockFeedbackCmd cmd = new LockFeedbackCmd(bytes);
        // TODO: 2018/3/30  
    }

    /**
     * 收到 07：物品检测状态反馈指令
     *
     * @param bytes
     */
    private void recvGoodsFeedback(byte[] bytes) {
        GoodsFeedbackCmd cmd = new GoodsFeedbackCmd(bytes);
        // TODO: 2018/3/30  
    }

    /**
     * @param bytes
     */
    private void receTempFeedback(byte[] bytes) {
        TempFeedbackCmd cmd = new TempFeedbackCmd(bytes);
        // TODO: 2018/3/30  
    }

    private void recvConfirm(byte[] bytes) {
        ConfirmCmd cmd = new ConfirmCmd(bytes);
        // TODO: 2018/3/30  
    }
}
