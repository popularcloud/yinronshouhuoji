package cn.dlc.yinrongshouhuoji.pad.comm;

import android.content.res.Resources;
import android.serialport.SerialPortFinder;
import cn.dlc.commonlibrary.utils.PrefUtil;
import cn.dlc.commonlibrary.utils.ResUtil;
import cn.dlc.yinrongshouhuoji.pad.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by John on 2018/3/29.
 */

public class ConfigHelper {

    public static class Config {

        int deviceIndex = 0;
        int baudrateIndex = 0;
        boolean inited = false;
        String deviceId = "";
        String devicePwd = "";
    }

    private static final String CONFIG = "serial_config";

    private static ConfigHelper sInstance = new ConfigHelper();

    private Gson mGson;
    private Config mConfig;
    // 设备路径列表
    private String[] mDevicePaths;
    // 波特率列表
    private int[] mBaudrates;
    // 波特率字符串列表
    private String[] mBaudrateStrs;

    private ConfigHelper() {
        mGson = new GsonBuilder().serializeNulls().create();
        loadConfigFromFile();
        if (mConfig == null) {
            mConfig = new Config();
        }

        Resources resources = ResUtil.getResources();
        mBaudrates = resources.getIntArray(R.array.baudrates);
        mBaudrateStrs = new String[mBaudrates.length];
        for (int i = 0; i < mBaudrateStrs.length; i++) {
            mBaudrateStrs[i] = String.valueOf(mBaudrates[i]);
        }

        SerialPortFinder serialPortFinder = new SerialPortFinder();
        // 设备
        mDevicePaths = serialPortFinder.getAllDevicesPath();
        if (mDevicePaths.length == 0) {
            mDevicePaths = new String[] { resources.getString(R.string.no_serial_device) };
        }

        mConfig.deviceIndex = mConfig.deviceIndex >= mDevicePaths.length ? 0 : mConfig.deviceIndex;
    }

    public static ConfigHelper get() {
        return sInstance;
    }

    private void saveConfigToFile() {
        String json = mGson.toJson(mConfig);
        PrefUtil.getDefault().putString(CONFIG, json).apply();
    }

    private void loadConfigFromFile() {

        String json = PrefUtil.getDefault().getString(CONFIG, "");
        try {
            Config config = mGson.fromJson(json, Config.class);
            if (config != null) {
                mConfig = config;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新配置
     *
     * @param deviceId
     */
    public void updateDeviceId(String deviceId,String devicePwd) {
        mConfig.deviceId = deviceId;
        mConfig.devicePwd = devicePwd;
        saveConfigToFile();
    }

    /**
     * 保存设备
     *
     * @param deviceIndex
     * @param baudrateIndex
     */
    public void updateSerialConfig(int deviceIndex, int baudrateIndex) {
        mConfig.deviceIndex = deviceIndex;
        mConfig.baudrateIndex = baudrateIndex;
        mConfig.inited = true;
        saveConfigToFile();
    }

    /**
     * 获取设备列表
     *
     * @return
     */
    public String[] getDevicePaths() {
        return mDevicePaths;
    }

    /**
     * 获取波特率列表
     *
     * @return
     */
    public int[] getBaudrates() {
        return mBaudrates;
    }

    /**
     * 获取波特率字符串列表
     *
     * @return
     */
    public String[] getBaudrateStrs() {
        return mBaudrateStrs;
    }

    public int getDeviceIndex() {
        return mConfig.deviceIndex;
    }

    public int getBaudrateIndex() {
        return mConfig.baudrateIndex;
    }

    public String getSelectedDevicePath() {
        return mDevicePaths[mConfig.deviceIndex];
    }

    public int getSelectedBaudrate() {
        return mBaudrates[mConfig.baudrateIndex];
    }

    public String getDeviceId() {
        return mConfig.deviceId;
    }

    public String getDevicePwd() {
        return mConfig.devicePwd;
    }

    /**
     * 是否已经配置过设备
     *
     * @return
     */
    public boolean isDeviceInited() {
        return mConfig.inited;
    }
}

