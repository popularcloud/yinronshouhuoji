package cn.dlc.yinrongshouhuoji.home.utlis.helper;

import android.widget.TextView;
import cn.dlc.yinrongshouhuoji.R;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class GridListHelper {

    /**
     * 设置不用的格子样式
     *
     * @param mTvGrid
     * @param status 格子状态 0空仓 1外卖 2空盒 3预定 4故障 5过期 6未关门 （具体看后台）
     */
    public static final void setGrid(TextView mTvGrid, int status) {
        switch (status) {
            case 0:
                mTvGrid.setBackgroundResource(R.mipmap.bg_kongcang);
                break;
            case 1:
                mTvGrid.setBackgroundResource(R.mipmap.bg_waimai);
                break;
            case 2:
                mTvGrid.setBackgroundResource(R.mipmap.bg_konghe);
                break;
            case 3:
                //PM说预定没颜色标记
                break;
            case 4:
                mTvGrid.setBackgroundResource(R.mipmap.bg_guzhang);
                break;
            case 5:
                mTvGrid.setBackgroundResource(R.mipmap.bg_guoqi);
                break;
            case 6:
                mTvGrid.setBackgroundResource(R.mipmap.bg_weiguanmen);
                break;
        }
    }
}
