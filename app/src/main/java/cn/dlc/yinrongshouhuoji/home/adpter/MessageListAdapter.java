package cn.dlc.yinrongshouhuoji.home.adpter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.MessageListBean;
import cn.dlc.yinrongshouhuoji.home.utlis.helper.MessageListAdapterHelper;
import cn.dlc.yinrongshouhuoji.util.Utils;

import java.text.SimpleDateFormat;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class MessageListAdapter extends BaseRecyclerAdapter<MessageListBean> {

    private SimpleDateFormat mSimpleDateFormat;
    private Context mContext;
    private OnClickButtonListener mOnClickButtonListener;

    public MessageListAdapter(Context mContext) {
        this.mContext = mContext;
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_message_list;
    }

    @Override
    public void setChildViewListener(final CommonHolder holder, int viewType) {
        super.setChildViewListener(holder, viewType);
        Button mBtnCancel = holder.getView(R.id.btn_cancel);
        Button mBtnAccept = holder.getView(R.id.btn_accept);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickButtonListener != null) {
                    MessageListBean mMessageListBean = getItem(holder.getAdapterPosition());
                    mOnClickButtonListener.onClickCancel(mMessageListBean);
                }
            }
        });
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickButtonListener != null) {
                    MessageListBean mMessageListBean = getItem(holder.getAdapterPosition());
                    mOnClickButtonListener.onClickAccept(mMessageListBean);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        MessageListBean mMessageListBean = getItem(position);

        TextView mTvTitle = holder.getView(R.id.tv_title);
        TextView mTvDate = holder.getView(R.id.tv_date);
        TextView mTvContent = holder.getView(R.id.tv_content);
        Button mBtnCancel = holder.getView(R.id.btn_cancel);
        Button mBtnAccept = holder.getView(R.id.btn_accept);
        LinearLayout mLlButton = holder.getView(R.id.ll_button);
        LinearLayout mLlTextView = holder.getView(R.id.ll_text_view);
        TextView Tv_status = holder.getView(R.id.tv_status);

        mTvTitle.setText(mMessageListBean.getTitle());
        mTvDate.setText(Utils.stampToDateDetail(mMessageListBean.getBookDate()+""));

        MessageListAdapterHelper.setItem(mContext, mSimpleDateFormat, mMessageListBean.getStatus(),
            mLlButton, mLlTextView, mBtnCancel, mBtnAccept, mTvContent,Tv_status, mMessageListBean.getPhone(),
            mMessageListBean.getGoodsName(), mMessageListBean.getBookDate(),
            mMessageListBean.getContent());
    }

    public interface OnClickButtonListener {
        void onClickCancel(MessageListBean mMessageListBean);

        void onClickAccept(MessageListBean mMessageListBean);
    }

    public void setOnClickButtonListener(OnClickButtonListener mOnClickButtonListener) {
        this.mOnClickButtonListener = mOnClickButtonListener;
    }
}
