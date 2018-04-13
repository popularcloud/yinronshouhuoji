package cn.dlc.yinrongshouhuoji.home.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import cn.dlc.yinrongshouhuoji.util.ActivityForResultUtil;
import cn.dlc.yinrongshouhuoji.util.Constance;
import cn.dlc.yinrongshouhuoji.util.CropPhotoUtil;
import cn.dlc.yinrongshouhuoji.util.SPUtils;

/**
 * 提示类
 */
public class DialogHelper {
    
    /**
     * 头像拍照提示框
     *
     * @return
     */
    public static AlertDialog createAlertDialog(final Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle("请选择")
                .setItems(new String[]{"拍照", "从相册中选取"},
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = null;
                                switch (which) {
                                    case 0:
                                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        String address = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
                                        File dir = new CropPhotoUtil(activity).getCachePath();
                                        if (!dir.exists()) {
                                            dir.mkdirs();
                                        }
                                        Log.e("拍照图片路径", "" + dir);
                                        String url = new CropPhotoUtil(activity)
                                                .getCachePath()
                                                + "/"
                                                + UUID.randomUUID().toString()
                                                + ".png";
                                        SPUtils.putString(Constance.PHOTOADDRESS, url);
                                        File file = new File(url);
                                        if (!file.exists()) {
                                            try {
                                                file.createNewFile();
                                            } catch (IOException e) {

                                            }
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(file));

                                        activity.startActivityForResult(
                                                intent,
                                                ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA);
                                        break;

                                    case 1:
                                        intent = new Intent(Intent.ACTION_PICK,
                                                null);
                                        intent.setDataAndType(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                "image/*");

                                        activity.startActivityForResult(
                                                intent,
                                                ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);
                                        break;
                                }
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }

}
