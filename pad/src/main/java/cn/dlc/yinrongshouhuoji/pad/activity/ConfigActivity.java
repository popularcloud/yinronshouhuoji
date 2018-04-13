package cn.dlc.yinrongshouhuoji.pad.activity;

import android.os.Bundle;
import android.serialport.SerialPort;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.okgo.callback.Bean01Callback;
import cn.dlc.commonlibrary.ui.widget.TitleBar;
import cn.dlc.yinrongshouhuoji.pad.R;
import cn.dlc.yinrongshouhuoji.pad.comm.ConfigHelper;
import cn.dlc.yinrongshouhuoji.pad.comm.DoorManager;
import cn.dlc.yinrongshouhuoji.pad.comm.Protocol;
import cn.dlc.yinrongshouhuoji.pad.comm.http.HttpApi;
import cn.dlc.yinrongshouhuoji.pad.comm.http.bean.BaseBean;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.SerialManager;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LightCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LightingLightCmd;
import cn.dlc.yinrongshouhuoji.pad.comm.serial.command.LockOpenCmd;
import cn.dlc.yinrongshouhuoji.pad.utils.CustomToast;

/**
 * Created by John on 2018/3/29.
 */

public class ConfigActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.spinner_devices)
    Spinner mSpinnerDevices;
    @BindView(R.id.spinner_baudrate)
    Spinner mSpinnerBaudrate;
    @BindView(R.id.btn_select_device)
    Button mBtnSelectDevice;
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.spinner_doors)
    Spinner mSpinnerDoors;
    @BindView(R.id.spinner_lights)
    Spinner mSpinnerLights;
    @BindView(R.id.btn_open_door)
    Button mBtnOpenDoor;
    @BindView(R.id.btn_open_lighting_light)
    Button mBtnOpenLightingLight;
    @BindView(R.id.btn_open_door_light)
    Button mBtnOpenDoorLight;
    @BindView(R.id.et_device_id)
    EditText mEtDeviceId;
    @BindView(R.id.et_device_pwd)
    EditText mEtDevicePwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_query_door_lock)
    Button mBtnQueryDoorLock;
    @BindView(R.id.btn_query_goods)
    Button mBtnQueryGoods;

    private ConfigHelper mConfigHelper;

    private int mDeviceIndex;
    private int mBaudrateIndex;
    private SerialManager mSerialManager;

    private int mDoorNum;
    private int mLightNum;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_config;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConfigHelper = ConfigHelper.get();
        mSerialManager = SerialManager.get();

        mTitleBar.leftExit(this);

        mEtDeviceId.setText(mConfigHelper.getDeviceId());
        mEtDevicePwd.setText(mConfigHelper.getDevicePwd());

        initSerialSpinners();
        initDoorSpinners();
    }

    /**
     * 初始化下拉选项
     */
    private void initSerialSpinners() {

        String[] devicePaths = mConfigHelper.getDevicePaths();
        String[] baudrateStrs = mConfigHelper.getBaudrateStrs();

        ArrayAdapter<String> deviceAdapter =
            new ArrayAdapter<>(this, R.layout.spinner_default_item, devicePaths);
        deviceAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinnerDevices.setAdapter(deviceAdapter);
        mSpinnerDevices.setOnItemSelectedListener(this);

        ArrayAdapter<String> baudrateAdapter =
            new ArrayAdapter<>(this, R.layout.spinner_default_item, baudrateStrs);
        baudrateAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinnerBaudrate.setAdapter(baudrateAdapter);
        mSpinnerBaudrate.setOnItemSelectedListener(this);

        mDeviceIndex = mConfigHelper.getDeviceIndex();
        mBaudrateIndex = mConfigHelper.getBaudrateIndex();

        mSpinnerDevices.setSelection(mDeviceIndex);
        mSpinnerBaudrate.setSelection(mBaudrateIndex);
    }

    /**
     * 初始化仓口和仓口灯下拉选项
     */
    private void initDoorSpinners() {

        String[] doors = getResources().getStringArray(R.array.door_nums);
        String[] lights = getResources().getStringArray(R.array.light_types_str);

        ArrayAdapter<String> doorAdapter =
            new ArrayAdapter<>(this, R.layout.spinner_default_item, doors);
        doorAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinnerDoors.setAdapter(doorAdapter);
        mSpinnerDoors.setOnItemSelectedListener(this);

        ArrayAdapter<String> lightAdapter =
            new ArrayAdapter<>(this, R.layout.spinner_default_item, lights);
        lightAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinnerLights.setAdapter(lightAdapter);
        mSpinnerLights.setOnItemSelectedListener(this);

        mSpinnerDoors.setSelection(0);
        mSpinnerLights.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Spinner 选择监听
        switch (parent.getId()) {
            case R.id.spinner_devices:
                mDeviceIndex = position;
                break;
            case R.id.spinner_baudrate:
                mBaudrateIndex = position;
                break;
            case R.id.spinner_doors:
                if (position == 0) {
                    mDoorNum = Protocol.DOOR_ALL;
                } else {
                    mDoorNum = position;
                }
                break;
            case R.id.spinner_lights:
                mLightNum = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 
    }

    @OnClick({
        R.id.btn_select_device, R.id.btn_open_door, R.id.btn_open_lighting_light,
        R.id.btn_open_door_light, R.id.btn_login, R.id.btn_query_door_lock, R.id.btn_query_goods,
        R.id.btn_open_some_lights, R.id.btn_open_query, R.id.btn_query_lock_goods
    })
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_select_device:
                openDevice();
                break;
            case R.id.btn_open_door:
                LockOpenCmd lockOpenCmd = new LockOpenCmd(mDoorNum);
                mSerialManager.sendCommand(lockOpenCmd);
                break;
            case R.id.btn_open_lighting_light:
                LightCmd lightCmd = new LightingLightCmd(mDoorNum, mLightNum);
                mSerialManager.sendCommand(lightCmd);
                break;
            case R.id.btn_open_door_light:
                LightCmd lightCmd1 = new LightingLightCmd(mDoorNum, mLightNum);
                mSerialManager.sendCommand(lightCmd1);
                break;
            case R.id.btn_open_some_lights: {
                for (int i = 1; i <= 4; i++) {
                    LightCmd cmd = new LightingLightCmd(i, i);
                    mSerialManager.sendCommand(cmd);
                }
                break;
            }
            case R.id.btn_query_door_lock: {

                mSerialManager.queryLock(mDoorNum, new Runnable() {
                    @Override
                    public void run() {
                        DoorManager.get().printExistDoors();
                    }
                });
                break;
            }
            case R.id.btn_query_goods: {
                mSerialManager.queryGoods(mDoorNum, new Runnable() {
                    @Override
                    public void run() {
                        DoorManager.get().printExistDoors();
                    }
                });
                break;
            }
            case R.id.btn_query_lock_goods: {
                mSerialManager.queryLockAndGoods(mDoorNum, new Runnable() {
                    @Override
                    public void run() {
                        DoorManager.get().printExistDoors();
                    }
                });
                break;
            }
            case R.id.btn_open_query: {
                mSerialManager.openAndQueryLockGoods(mDoorNum, new Runnable() {
                    @Override
                    public void run() {
                        DoorManager.get().printExistDoors();
                    }
                });
                break;
            }
            case R.id.btn_login:
                loginDevice();
                break;
        }
    }

    /**
     * 登记设备
     */
    private void loginDevice() {
        final String deviceId = mEtDeviceId.getText().toString().trim();
        final String devicePwd = mEtDevicePwd.getText().toString().trim();
        if (TextUtils.isEmpty(deviceId)) {
            showOneToast("设备编号不能为空！");
            return;
        }

        if (TextUtils.isEmpty(devicePwd)) {
            showOneToast("设备密码不能为空!");
            return;
        }

        showWaitingDialog(R.string.zhengzaidenglu, false);
        showWaitingDialog("登记设备", false);
        HttpApi.get().login(deviceId, devicePwd, new Bean01Callback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                ConfigHelper.get().updateDeviceId(deviceId, devicePwd);
                dismissWaitingDialog();
                showOneToast("登记成功");
            }

            @Override
            public void onFailure(String message, Throwable tr) {
                dismissWaitingDialog();
                showOneToast(message);
            }
        });
    }

    /**
     * 打开串口
     */
    private void openDevice() {

        mConfigHelper.updateSerialConfig(mDeviceIndex, mBaudrateIndex);
        mSerialManager.closeSerial();
        SerialPort device = mSerialManager.openSelectedDevice();
        if (device == null) {
            CustomToast.show(this, "打开串口失败");
        }
    }
}
