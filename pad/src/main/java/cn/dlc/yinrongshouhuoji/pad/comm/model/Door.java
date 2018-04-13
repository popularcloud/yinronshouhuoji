package cn.dlc.yinrongshouhuoji.pad.comm.model;

/**
 * 仓口
 */
public class Door {

    /**
     * 仓口号
     */
    private int num;
    /**
     * 仓口是否存在
     */
    private boolean exists;

    /**
     * 仓口是否标记为异常的
     */
    private boolean abnormal;

    /**
     * 仓口是否已锁着
     */
    private boolean locked;
    /**
     * 仓口是否已有物品
     */
    private boolean hasGoods;

    public Door(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean hasGoods() {
        return hasGoods;
    }

    public void setHasGoods(boolean hasGoods) {
        this.hasGoods = hasGoods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Door door = (Door) o;

        return num == door.num;
    }

    @Override
    public int hashCode() {
        return num;
    }

    @Override
    public String toString() {
        return "Door{"
            + "num="
            + num
            + ", exists="
            + exists
            + ", abnormal="
            + abnormal
            + ", locked="
            + locked
            + ", hasGoods="
            + hasGoods
            + '}';
    }
}

