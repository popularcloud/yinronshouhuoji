package cn.dlc.yinrongshouhuoji.pad.comm.serial.command;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;

/**
 * 命令封装
 */
public abstract class Command {

    protected int door;
    protected int flag;
    protected int data;

    protected Command(@Protocol.Flag int flag) {
        this.flag = flag;
    }

    protected Command(@Protocol.Flag int flag, int door) {
        this(flag);
        this.door = door;
    }

    protected Command(byte[] bytes) {
        this.door = 0xff & bytes[1];
        this.flag = 0xff & bytes[2];
        this.data = 0xff & bytes[3];
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    @Protocol.Flag
    public int getFlag() {
        return flag;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    /**
     * 转成字节数组
     * 起始位    仓口ID号    特征码    效数据   校验位   数据尾
     */
    public byte[] toBytes() {
        byte[] bytes = new byte[7];
        bytes[0] = Protocol.FRAME_HEAD;
        bytes[1] = (byte) door;
        bytes[2] = (byte) flag;
        bytes[3] = (byte) data;
        bytes[4] = (byte) (door ^ flag ^ data);
        bytes[5] = Protocol.FRAME_FOOT_0;
        bytes[6] = Protocol.FRAME_FOOT_1;
        return bytes;
    }

    public abstract boolean isSend();

    @Override
    public String toString() {
        return "Command{" + "door=" + door + ", flag=" + flag + ", data=" + data + '}';
    }
}
