package cn.dlc.yinrongshouhuoji.personal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.bean.MineInfoBean;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.DialogHelper;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.personal.view.BottomDialog;
import cn.dlc.yinrongshouhuoji.util.ActivityForResultUtil;
import cn.dlc.yinrongshouhuoji.util.Constance;
import cn.dlc.yinrongshouhuoji.util.CropPhotoUtil;
import cn.dlc.yinrongshouhuoji.util.SPUtils;
import cn.dlc.yinrongshouhuoji.util.ToastUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 页面:李旭康  on  2018/3/13.
 * 对接口:
 * 作用:
 */

public class PersonalDataActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.personal_data_img)
    ImageView mPersonalDataImg;
    @BindView(R.id.personal_data_yingfu_tv)
    TextView mPersonalDataYingfuTv;
    @BindView(R.id.personal_data_yingfu_rl)
    RelativeLayout mPersonalDataYingfuRl;
    @BindView(R.id.personal_data_xingbie_tv)
    TextView mPersonalDataXingbieTv;
    @BindView(R.id.personal_data_xingbie_rl)
    RelativeLayout mPersonalDataXingbieRl;
    private MineInfoBean mineInfoBean;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_aboutus_personaldata;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBar();
        initCircle();
    }

    //圆形图片
    private void initCircle() {
        mineInfoBean = (MineInfoBean)getIntent().getSerializableExtra("data");
        mPersonalDataImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.createAlertDialog(PersonalDataActivity.this).show();
            }
        });
        showLog("修改个人资料数据："+mineInfoBean.getSex()+"-"+mineInfoBean.getNickname()+"-"+mineInfoBean.getPhone());
        Glide.with(this)
            .load(mineInfoBean.getImage()+"")
            .apply(new RequestOptions().transform(new CircleCrop())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.logo))
            .into(mPersonalDataImg);
        mPersonalDataYingfuTv.setText(mineInfoBean.getNickname()+"");
        mPersonalDataXingbieTv.setText(mineInfoBean.getSex()+"");
    }

    private void postAddData() {
        if (url == null) {
            ToastUtil.showMessage("请选择上传的经文头像");
            return;
        }
        String token = SPUtils.getString(Constance.TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("api_name","modifyImg");
        doUploadSubmit(Config.INTERFACE_MINE, map, new File(url), "image");
    }

    private void initTitleBar() {
        mTitleBar.leftExit(this);
    }

    @OnClick({ R.id.personal_data_yingfu_rl, R.id.personal_data_xingbie_rl })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //用户名
            case R.id.personal_data_yingfu_rl:
                Intent intent = new Intent(this, UserNameActivety.class);
                startActivityForResult(intent,1);
                break;
            //性别
            case R.id.personal_data_xingbie_rl:
                initShowDialog();
                break;
        }
    }

    //姓名修改的回调
    String url  = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            String name = data.getStringExtra("name");
            mPersonalDataYingfuTv.setText(name);
        }
        switch (requestCode) {
            /**
             * 照相返回的图片
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    url = SPUtils.getString(Constance.PHOTOADDRESS);
                    Glide.with(getActivity())
                            .load(url+"")
                            .apply(new RequestOptions().transform(new CircleCrop())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.mipmap.logo))
                            .into(mPersonalDataImg);
                    postAddData();
                } else {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 本地返回的图片
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
                if (data == null) {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri uri = data.getData();
                    new CropPhotoUtil(this).startPhotoZoom(uri);
                } else {
                    Toast.makeText(this, "照片获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 裁剪修改的头像
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
                if (data == null) {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    url = new CropPhotoUtil(this).getCropPath(data);
                    Glide.with(getActivity())
                            .load(url+"")
                            .apply(new RequestOptions().transform(new CircleCrop())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.mipmap.logo))
                            .into(mPersonalDataImg);
                    postAddData();
                }
                break;
        }
    }

    String sex[] =new String[]{"1","2"};
    String man = "男";
    private void initShowDialog() {
        final BottomDialog dialog = new BottomDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setBottomDialogListener(new BottomDialog.BottomDialogListener() {
            @Override
            public void succeed(String s) {
                if (s==null){
                    showOneToast("请选择性别");
                }else {
                    mPersonalDataXingbieTv.setText(s);
                    Map<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("api_name", "modifySex");
                    dataMap.put("token", Config.token);
                    if (s.equals(man)){
                        dataMap.put("sex", sex[0]);
                    }else {
                        dataMap.put("sex", sex[1]);
                    }
                    doApiPost(Config.INTERFACE_MINE, dataMap);
                    dialog.dismiss();
                }
                
            }

            @Override
            public void calloff() {
                showOneToast("取消");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
