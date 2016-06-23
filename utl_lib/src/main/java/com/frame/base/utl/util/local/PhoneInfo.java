package com.frame.base.utl.util.local;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * 获取手机信息，例如imei、imsi、mac地址等等
 * Created by cangfei.hgy on 2014/5/21.
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PhoneInfo {

    public static final String IMEI = "imei";
    public static final String IMSI = "imsi";
    public static final String MACADDRESS = "mac_address";

    private static String phone_imei = "";
    private static String phone_imsi = "";
    private static String phone_wifiaddr = "";

    private static String generateImei() {

        StringBuffer imei = new StringBuffer();

        // 添加当前秒数 毫秒数 5位
        long time = System.currentTimeMillis();
        String currentTime = Long.toString(time);
        imei.append(currentTime.substring(currentTime.length() - 5));

        // 手机型号 6位
        StringBuffer model = new StringBuffer();
        model.append(Build.MODEL.replaceAll(" ", ""));
        while (model.length() < 6) {
            model.append('0');
        }
        imei.append(model.substring(0, 6));

        // 随机数 4位
        Random random = new Random(time);
        long tmp = 0;
        while (tmp < 0x1000) {
            tmp = random.nextLong();
        }

        imei.append(Long.toHexString(tmp).substring(0, 4));

        return imei.toString();

    }

    /**
     * 获取imei，如果系统不能获取，则将动态产生一个唯一标识并保存
     *
     * @param context Context实例
     * @return imsi字串
     */
    public static String getImei(Context context) {

        if (!TextUtils.isEmpty(phone_imei)) {
            return phone_imei;
        }

        String imei = null;
        SharedPreferences sp = context.getSharedPreferences(IMEI, Context.MODE_PRIVATE);
        imei = sp.getString(IMEI, null);
        if (imei == null || imei.length() == 0) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            imei = tm.getDeviceId(); // 获取imei的方法修改
            if (imei == null || imei.length() == 0) {
                imei = generateImei();
            }
            imei = imei.replaceAll(" ", "").trim();
            // imei 小于15位补全 jiuwan
            while (imei.length() < 15) {
                imei = "0" + imei;
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.putString(IMEI, imei);
            if (Build.VERSION.SDK_INT >= 9) {
                editor.apply();
            } else {
                editor.commit();
            }
        }
        phone_imei = imei.trim();
        return phone_imei;
    }

    /**
     * 获取imsi，如果系统不能获取，则将动态产生一个唯一标识并保存
     *
     * @param context ： Context实例
     * @return imsi字串
     */
    static public String getImsi(Context context) {

        if (!TextUtils.isEmpty(phone_imsi)) {
            return phone_imsi;
        }

        String imsi = null;
        SharedPreferences sp = context.getSharedPreferences(IMEI, Context.MODE_PRIVATE);
        imsi = sp.getString(IMSI, null);
        if (imsi == null || imsi.length() == 0) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
            if (imsi == null || imsi.length() == 0) {
                imsi = generateImei();
            }
            imsi = imsi.replaceAll(" ", "").trim();
            // imei 小于15位补全 jiuwan
            while (imsi.length() < 15) {
                imsi = "0" + imsi;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(IMSI, imsi);
            if (Build.VERSION.SDK_INT >= 9) {
                editor.apply();
            } else {
                editor.commit();
            }
        }

        phone_imsi = imsi;
        return phone_imsi;
    }

    /**
     * 获取wifi 模块mac地址
     *
     * @param context ： Context实例
     * @return wifi模块mac地址
     */
    public static String getLocalMacAddress(Context context) {

        if (!TextUtils.isEmpty(phone_wifiaddr)) {
            return phone_wifiaddr;
        }

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiaddr = info.getMacAddress();

        if (wifiaddr == null || "".equals(wifiaddr)) {
            SharedPreferences sp = context.getSharedPreferences(MACADDRESS, Context.MODE_PRIVATE);
            wifiaddr = sp.getString(MACADDRESS, "");
        } else {
            SharedPreferences sp = context.getSharedPreferences(MACADDRESS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(MACADDRESS, wifiaddr);
            if (Build.VERSION.SDK_INT >= 9) {
                editor.apply();
            } else {
                editor.commit();
            }
        }

        phone_wifiaddr = wifiaddr;
        return phone_wifiaddr;
    }

    public static String getSerialNum() {
        String serialnum = null;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }

        return serialnum;
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }
}
