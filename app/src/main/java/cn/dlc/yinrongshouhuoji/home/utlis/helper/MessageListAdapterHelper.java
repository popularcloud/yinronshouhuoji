package cn.dlc.yinrongshouhuoji.home.utlis.helper;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.dlc.yinrongshouhuoji.R;
import java.text.SimpleDateFormat;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class MessageListAdapterHelper {

    /**
     * 消息状态不同，显示内容不同
     *
     * @param status 消息状态 0未接单 1已接单 2通知类（具体看后台）
     * @param mLlButton 拒绝订单接受订单布局
     * @param mLlTextView 已接受布局
     * @param mBtnCancel 拒绝订单按钮
     * @param mBtnAccept 接收订单按钮
     * @param mTvContent 消息内容TextView
     * @param mPhone 用户电话
     * @param mGoodsName 商品名称
     * @param mBookDate 预约配送时间
     * @param mContent 通知类内容
     */
    public static final void setItem(Context mContext, SimpleDateFormat mSimpleDateFormat,
        int status, LinearLayout mLlButton, LinearLayout mLlTextView, Button mBtnCancel,
        Button mBtnAccept, TextView mTvContent,TextView Tv_status,String mPhone, String mGoodsName, long mBookDate,
        String mContent) {
        switch (status) {
            case 0:
                mLlButton.setVisibility(View.VISIBLE);
                mLlTextView.setVisibility(View.GONE);
                mTvContent.setText(mContent);

                break;
            case 1:
                mLlButton.setVisibility(View.GONE);
                mLlTextView.setVisibility(View.VISIBLE);
                mTvContent.setText(mContent);
                Tv_status.setText("已接受");
                break;
            case 2:
                mLlButton.setVisibility(View.GONE);
                mLlTextView.setVisibility(View.VISIBLE);
                mTvContent.setText(mContent);
                Tv_status.setText("已拒绝");
                break;
        }
    }
}
