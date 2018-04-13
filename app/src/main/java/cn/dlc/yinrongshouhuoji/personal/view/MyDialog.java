package cn.dlc.yinrongshouhuoji.personal.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import cn.dlc.commonlibrary.utils.DialogUtil;
import cn.dlc.yinrongshouhuoji.R;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class MyDialog extends Dialog {

    private ImageView mImg;
    private TextView mTv;

    private Context mContext;

    public MyDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.mContext = context;
        setContentView(R.layout.dialog_affirm);
        DialogUtil.adjustDialogLayout(this,false,false);
        DialogUtil.setGravity(this, Gravity.CENTER);
        initView();
    }

    private void initView() {
        mImg = findViewById(R.id.dialog_yes_img);
        mTv = findViewById(R.id.dialog_yes_tv);
    }

    public void setIsTrue(Boolean b) {
        if (b) {
            mImg.setImageResource(R.mipmap.chenggongda);
        } else {
            mImg.setImageResource(R.mipmap.shiobaida);
        }
    }

    public void setIsText(String s) {
        mTv.setText(s);
    }

    public void setIsText(int id) {
        mTv.setText(mContext.getResources().getString(id));
    }
}
