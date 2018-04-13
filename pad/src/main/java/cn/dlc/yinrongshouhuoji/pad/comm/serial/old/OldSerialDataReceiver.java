package cn.dlc.yinrongshouhuoji.pad.comm.serial.old;

import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.utils.ByteUtil;
import com.licheedev.myutils.LogPlus;
import java.nio.ByteBuffer;

/**
 * Created by John on 2018/3/29.
 */

public class OldSerialDataReceiver implements OldDataReceiver {

    private ByteBuffer mByteBuffer;
    private ValidDataCallback mCallback;

    public OldSerialDataReceiver() {
        mByteBuffer = ByteBuffer.allocate(2048);
        mByteBuffer.clear();
    }

    //   起始位    仓口ID号    特征码    效数据   校验位   数据尾
    //0x1b          x          x        x         x         D 0A
    private void checkData() {

        mByteBuffer.flip();

        byte b;
        byte[] twoBytes = new byte[2];

        int frameStart;
        int frameEnd;
        // 必须比包长度大
        while (mByteBuffer.remaining() >= Protocol.PACKAGE_LENGTH) {
            mByteBuffer.mark(); // 标记一下开始的位置
            // 标记一个第一个元素
            frameStart = mByteBuffer.position();

            b = mByteBuffer.get();
            if (b != Protocol.FRAME_HEAD) { // 第1个byte要0x1B
                continue;
            }

            // 跳过 仓口ID号    特征码    效数据   校验位 (1 1 1 1)
            mByteBuffer.position(mByteBuffer.position() + 4);

            // 数据尾
            mByteBuffer.get(twoBytes);
            frameEnd = mByteBuffer.position();

            // 数据尾符合规则
            if (twoBytes[0] == Protocol.FRAME_FOOT_0 && twoBytes[1] == Protocol.FRAME_FOOT_1) {
                mByteBuffer.position(frameStart + 1);
                int toDiff = mByteBuffer.get(); // 仓口ID号
                toDiff = toDiff ^ mByteBuffer.get();// 特征码
                toDiff = toDiff ^ mByteBuffer.get();// 效数据
                byte check = mByteBuffer.get(); // 校验位
                
                if (check == (byte) toDiff) {
                    // 校验成功
                    // 拿到整个包
                    mByteBuffer.reset();
                    byte[] data = new byte[Protocol.PACKAGE_LENGTH];
                    mByteBuffer.get(data);
                    if (getValidDataCallback() != null) {
                        getValidDataCallback().onReceiveValidData(data);
                    }
                } else {
                    //校验失败，舍弃这个包
                    mByteBuffer.position(frameEnd);
                }
            } else {
                // 不符合就跳到第二个为重新来
                mByteBuffer.position(frameStart + 1);
            }
        }
        // 最后清掉之前处理过的不合适的数据
        mByteBuffer.compact();
    }

    @Override
    public void receive(byte[] data, int length) {

        LogPlus.e("收到数据=" + ByteUtil.bytes2HexStr(data, 0, length));

        mByteBuffer.put(data, 0, length);
        checkData();
    }

    @Override
    public void receive(byte data) {
        mByteBuffer.put(data);
        checkData();
    }

    @Override
    public synchronized void resetCache() {
        mByteBuffer.clear();
    }

    @Override
    public ValidDataCallback getValidDataCallback() {
        return mCallback;
    }

    @Override
    public void setValidDataCallback(ValidDataCallback handler) {
        mCallback = handler;
    }
}
