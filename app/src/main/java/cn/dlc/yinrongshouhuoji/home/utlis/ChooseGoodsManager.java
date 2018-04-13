package cn.dlc.yinrongshouhuoji.home.utlis;

import android.content.Context;
import android.text.Html;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.ChooseGoodsListBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liuwenzhuo on 2018/3/15.
 */

public class ChooseGoodsManager {

    private static ChooseGoodsManager mInstance;

    private static Map<String, Integer> mMap;

    private ChooseGoodsManager() {
        mMap = new HashMap<>();
    }

    public static ChooseGoodsManager getInstance() {
        if (mInstance == null) {
            mInstance = new ChooseGoodsManager();
            return mInstance;
        } else {
            return mInstance;
        }
    }

    //添加放置后的商品
    public void addData(ChooseGoodsListBean mChooseGoodsListBean) {
        if (mMap.containsKey(mChooseGoodsListBean.getGoodsName())) {
            int count = mMap.get(mChooseGoodsListBean.getGoodsName());
            mMap.put(mChooseGoodsListBean.getGoodsName(),
                count + mChooseGoodsListBean.getGoodsCount());
        } else {
            mMap.put(mChooseGoodsListBean.getGoodsName(), mChooseGoodsListBean.getGoodsCount());
        }
    }

    //全部放置结束后调用，返回拼接好的商品名称+数量字符串
    public String getListStr(Context mContext) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Integer>> iterator = mMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> mEntry = iterator.next();
            sb.append(Html.fromHtml(
                mContext.getString(R.string.name_fen, mEntry.getKey(), mEntry.getValue())));
        }
        clearData();
        return sb.toString();
    }

    public void clearData() {
        mMap.clear();
    }
}
