package cn.dlc.yinrongshouhuoji.personal.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import cn.dlc.yinrongshouhuoji.R;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class BottomDialog extends Dialog implements View.OnClickListener {

    private TextView mNan;
    private TextView mNu;

    private Context mContext;
    private String mS;
    private  BottomDialogListener mBottomDialogListener;
    public BottomDialog(@NonNull Context context) {
        super(context,R.style.CommonDialogStyle);
        this.mContext = context;
        setContentView(R.layout.dialog_buttom);
        initView();
    }

    private void initView() {
        mNan = findViewById(R.id.dialog_nan_tv);
        mNu = findViewById(R.id.dialog_nu_tv);
        mNan.setOnClickListener(this);
        mNu.setOnClickListener(this);
        findViewById(R.id.dialog_bottom_yes_tv).setOnClickListener(this);
        findViewById(R.id.dialog_bottom_no_tv).setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_nan_tv:
                mNan.setTextColor(mContext.getResources().getColor(R.color.f3333));
                mNu.setTextColor(mContext.getResources().getColor(R.color.f9999));
                mS = mNan.getText().toString();
                break;
            case R.id.dialog_nu_tv:
                mNu.setTextColor(mContext.getResources().getColor(R.color.f3333));
                mNan.setTextColor(mContext.getResources().getColor(R.color.f9999));
                mS = mNu.getText().toString();
                break;
                
            case R.id.dialog_bottom_no_tv:
                mBottomDialogListener.calloff();
                break;
            case R.id.dialog_bottom_yes_tv:
                mBottomDialogListener.succeed(mS);
                break;
        }
    }
    
   public  void  setBottomDialogListener(BottomDialogListener bottomDialogListener){
       this.mBottomDialogListener=bottomDialogListener;
    } 
    
    public  interface BottomDialogListener{
        void succeed(String s);
        void calloff();
    } 
}
