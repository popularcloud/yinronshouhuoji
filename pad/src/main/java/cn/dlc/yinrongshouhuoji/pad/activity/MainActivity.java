package cn.dlc.yinrongshouhuoji.pad.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.dlc.commonlibrary.okgo.callback.Bean01Callback;
import cn.dlc.commonlibrary.utils.rx.RxUtil;
import cn.dlc.yinrongshouhuoji.pad.R;
import cn.dlc.yinrongshouhuoji.pad.comm.CommService;
import cn.dlc.yinrongshouhuoji.pad.comm.http.HttpApi;
import cn.dlc.yinrongshouhuoji.pad.comm.http.bean.AdvertisementBean;
import cn.dlc.yinrongshouhuoji.pad.utils.BannerHelper;
import cn.dlc.yinrongshouhuoji.pad.comm.Initializer;
import cn.dlc.yinrongshouhuoji.pad.utils.rx.NextObserver;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.youth.banner.Banner;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import java.util.concurrent.Callable;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class MainActivity extends BaseServiceActivity {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.iv_qr_code)
    ImageView mIvQrCode;
    @BindView(R.id.btn_config)
    Button mBtnConfig;
    private Initializer mInitializer;
    private BannerHelper<String> mBannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBanner();
        loadQrCode("吔屎非凡");

        // 初始化器
        mInitializer = new Initializer(this);
        mInitializer.setCallback(mInitCallback);

        connectCarService(true, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 检查各种东西
        mInitializer.check();
    }

    @Override
    protected void onDestroy() {
        unbindCommService();
        super.onDestroy();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {

        mBannerHelper = new BannerHelper<>(this);
        mBannerHelper.bindBanner(mBanner, new BannerHelper.DefaultBannerWork<String>() {
            @Override
            public String transformBannerUrl(String item) {
                return item;
            }
        });
    }

    /**
     * 加载二维码
     *
     * @param qrcode
     */
    private void loadQrCode(final String qrcode) {

        final int qrsize = getResources().getDimensionPixelSize(R.dimen.qr_size);
        final int qrColor = ContextCompat.getColor(this, R.color.foregroundColor);

        Observable.defer(new Callable<ObservableSource<Bitmap>>() {
            @Override
            public ObservableSource<Bitmap> call() throws Exception {

                Bitmap bitmap =
                    QRCodeEncoder.syncEncodeQRCode(qrcode, qrsize, qrColor, Color.TRANSPARENT,
                        null);
                return Observable.just(bitmap);
            }
        })
            .compose(RxUtil.<Bitmap>rxIoMain())
            .compose(this.<Bitmap>bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(new NextObserver<Bitmap>() {
                @Override
                public void onNext(Bitmap bitmap) {
                    mIvQrCode.setImageBitmap(bitmap);
                }
            });
    }

    /**
     * 初始化器回调
     */
    private Initializer.InitCallback mInitCallback = new Initializer.InitCallback() {
        @Override
        public void noDeviceId() {
            // 未登录
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("未配置商户信息，点击确定进入配置页")
                .setCancelable(false)
                .setPositiveButton("配置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(ConfigActivity.class);
                    }
                })
                .show();
        }

        @Override
        public void deviceIdOk() {

            // TODO: 2018/4/9  
            if (false) {
                return;
            }

            // 登录成功的
            HttpApi.get().advertisement(new Bean01Callback<AdvertisementBean>() {
                @Override
                public void onSuccess(AdvertisementBean advertisementBean) {
                    mBannerHelper.updateBanner(advertisementBean.data);
                }

                @Override
                public void onFailure(String message, Throwable tr) {

                }
            });
        }

        @Override
        public void noSerialDeviceInit() {

            // TODO: 2018/4/9  
            if (false) {
                return;
            }

            // 未配置串口设备
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("未配置串口设备，点击确定进入配置页")
                .setCancelable(false)
                .setPositiveButton("配置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(ConfigActivity.class);
                    }
                })
                .show();
        }

        @Override
        public void noSerialDeviceConnect() {

            // TODO: 2018/4/9 
            if (false) {
                return;
            }

            // 串口未连接
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("未打开串口设备，点击确定进入配置页")
                .setCancelable(false)
                .setPositiveButton("配置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(ConfigActivity.class);
                    }
                })
                .show();
        }
    };

    @OnClick({ R.id.iv_qr_code, R.id.btn_config })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_qr_code:
                break;
            case R.id.btn_config:
                startActivity(ConfigActivity.class);
                break;
        }
    }

    @Override
    protected void onServiceConnected(CommService commService) {
        // TODO: 2018/4/9  
    }

    @Override
    protected void onServiceDisconnected() {
        // TODO: 2018/4/9  
    }
}
