package cn.dlc.yinrongshouhuoji.home.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.dlc.commonlibrary.utils.DialogUtil;
import cn.dlc.yinrongshouhuoji.R;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class TwoTextViewDialog extends Dialog {

    public TextView mTvTitle;
    public TextView mTvContent;
    public Button mBtnConfirm;
    public Button mBtnCancel;
    public ImageView mImgClose;
    private Context mContext;
    private OnClickButtonListener mOnClickButtonListener;

    public TwoTextViewDialog(@NonNull Context context) {
        this(context, 0);
    }

    public TwoTextViewDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.CommonDialogStyle);
        mContext = context;
        setContentView(R.layout.dialog_two_text_view);
        DialogUtil.adjustDialogLayout(this, false, false);
        DialogUtil.setGravity(this, Gravity.CENTER);
        mTvTitle = findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_content);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnCancel = findViewById(R.id.btn_cancel);
        mImgClose = findViewById(R.id.img_close);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickButtonListener != null) {
                    mOnClickButtonListener.onClickConfirm();
                }
                dismiss();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickButtonListener != null) {
                    mOnClickButtonListener.onClickCancel();
                }
                dismiss();
            }
        });
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setTitle(int titleId) {
        mTvTitle.setText(mContext.getResources().getString(titleId));
    }

    public void setContent(String content) {
        mTvContent.setText(content);
    }

    public void setContent(int contentId) {
        mTvContent.setText(mContext.getResources().getString(contentId));
    }

    public void setBtnConfirm(String btnConfirm) {
        mBtnConfirm.setText(btnConfirm);
    }

    public void setBtnConfirm(int btnConfirmId) {
        mBtnConfirm.setText(mContext.getResources().getString(btnConfirmId));
    }

    public void setBtnCancel(String btnCancel) {
        mBtnCancel.setText(btnCancel);
    }

    public void setBtnCancel(int btnCancelId) {
        mBtnCancel.setText(mContext.getResources().getString(btnCancelId));
    }

    public interface OnClickButtonListener {
        void onClickConfirm();

        void onClickCancel();
    }

    public void setOnClickButtonListener(OnClickButtonListener mOnClickButtonListener) {
        this.mOnClickButtonListener = mOnClickButtonListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mOnClickButtonListener.onClickCancel();
    }
}
