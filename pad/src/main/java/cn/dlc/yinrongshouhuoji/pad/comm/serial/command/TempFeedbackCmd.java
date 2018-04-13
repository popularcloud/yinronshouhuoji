package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

/**
 * 0b：温区1温度反馈指令
 * 0c：温区2温度反馈指令
 * 0d：温区3温度反馈指令
 */
public class TempFeedbackCmd extends Command {

    //public TempFeedbackCmd(@Protocol.TempFeedbackFlag int flag) {
    //    super(flag);
    //}

    public TempFeedbackCmd(byte[] bytes) {
        super(bytes);
    }

    @Override
    public boolean isSend() {
        return false;
    }

    /**
     * 温度值 0-48度
     *
     * @return
     */
    public int getTemperature() {
        return getData();
    }
}
