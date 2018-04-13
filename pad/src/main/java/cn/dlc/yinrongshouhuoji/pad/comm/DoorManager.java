package cn.dlc.yinrongshouhuoji.pad.comm;

import android.util.SparseArray;
import cn.dlc.yinrongshouhuoji.pad.comm.model.Door;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.Command;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.GoodsFeedbackCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockFeedbackCmd;
import com.licheedev.myutils.LogPlus;
import java.util.ArrayList;
import java.util.List;

/**
 * 串口管理器
 */
public class DoorManager {

    private static final String TAG = "DoorManager";
    private ArrayList<Door> mDoorList;

    private static class InstanceHolder {

        public static DoorManager sManager = new DoorManager();
    }

    public static DoorManager get() {
        return InstanceHolder.sManager;
    }

    private DoorManager() {
        initDoors(Protocol.MAX_DOOR_AMOUNT);
    }

    private void initDoors(int amount) {

        mDoorList = new ArrayList<>();
        SparseArray<Door> doorSparseArray = new SparseArray<>();

        for (int i = 0; i < amount; i++) {
            Door door = new Door(i + 1);
            mDoorList.add(door);
            doorSparseArray.append(i, door);
        }
    }

    /**
     * 获取仓口
     *
     * @param doorNum
     */
    public Door getDoor(int doorNum) {
        return mDoorList.get(doorNum - 1);
    }

    /**
     * 获取仓口，并将仓口标识为存在
     *
     * @param doorNum
     * @return
     */
    public Door getDoorAndMarkExists(int doorNum) {
        Door door = getDoor(doorNum);
        door.setExists(true);
        return door;
    }

    /**
     * 更新仓口状态
     *
     * @param commands
     */
    public void updateDoors(List<Command> commands) {
        if (commands == null) {
            return;
        }

        for (Command command : commands) {
            updateDoor(command);
        }
    }

    /**
     * 更新仓口状态
     *
     * @param command
     */
    public void updateDoor(Command command) {

        if (command == null) {
            return;
        }

        if (command instanceof LockFeedbackCmd) {
            LockFeedbackCmd cmd = (LockFeedbackCmd) command;
            Door door = getDoorAndMarkExists(cmd.getDoor());
            // 标识门锁状态
            door.setLocked(cmd.isLocked());
        } else if (command instanceof GoodsFeedbackCmd) {
            GoodsFeedbackCmd cmd = (GoodsFeedbackCmd) command;
            Door door = getDoorAndMarkExists(cmd.getDoor());
            // 标志物品状态
            door.setHasGoods(cmd.hasGoods());
        }
    }

    /**
     * 获取存在的仓口
     *
     * @return
     */
    public List<Door> getExistDoors() {
        ArrayList<Door> doors = new ArrayList<>();
        for (Door door : mDoorList) {
            if (door.isExists()) {
                doors.add(door);
            }
        }
        return doors;
    }

    /**
     * 打印存在的仓口信息
     */
    public void printExistDoors() {
        List<Door> existDoors = getExistDoors();
        for (Door existDoor : existDoors) {
            LogPlus.i("==================== " + existDoor);
        }
    }
}
