package cn.dlc.yinrongshouhuoji.home.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.dlc.commonlibrary.utils.DialogUtil;
import cn.dlc.yinrongshouhuoji.R;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class EditTextDialog extends Dialog {

    public TextView mTvTitle;
    public TextView mTvContent;
    public Button mBtnConfirm;
    public EditText mEditText;

    public ImageView mImgClose;
    private Context mContext;
    private OnClickButtonListener mOnClickButtonListener;

    public EditTextDialog(@NonNull Context context) {
        this(context, 0);
    }

    public EditTextDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.CommonDialogStyle);
        mContext = context;
        setContentView(R.layout.dialog_edit_text);
        DialogUtil.adjustDialogLayout(this, false, false);
        DialogUtil.setGravity(this, Gravity.CENTER);
        mTvTitle = findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_content);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mEditText = findViewById(R.id.edit_text);
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

    public void setEditTextHint(String hint) {
        mEditText.setHint(hint);
    }

    public void setEditTextHint(int hintId) {
        mEditText.setHint(mContext.getResources().getString(hintId));
    }

    public void setBtnConfirm(String btnConfirm) {
        mBtnConfirm.setText(btnConfirm);
    }

    public void setBtnConfirm(int btnConfirmId) {
        mBtnConfirm.setText(mContext.getResources().getString(btnConfirmId));
    }

    public interface OnClickButtonListener {
        void onClickConfirm();
    }

    public void setOnClickButtonListener(OnClickButtonListener mOnClickButtonListener) {
        this.mOnClickButtonListener = mOnClickButtonListener;
    }
}
