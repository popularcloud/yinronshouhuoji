package cn.dlc.yinrongshouhuoji.home.bean;

import cn.dlc.yinrongshouhuoji.home.bean.intfc.GridListBeanIntfc;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class GridListBean implements GridListBeanIntfc {

    String gridNo;//格子编号

    int status;//格子状态 0空仓 1外卖 2空盒 3预定 4故障 5过期 6未关门 （具体看后台）

    @Override
    public String getGridNo() {
        return gridNo;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public GridListBean(String gridNo, int status) {
        this.gridNo = gridNo;
        this.status = status;
    }

    @Override
    public String toString() {
        return "GridListBean{" + "gridNo='" + gridNo + '\'' + ", status=" + status + '}';
    }
}
