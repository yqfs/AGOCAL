package com.frame.base.utl.util.local;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.frame.base.utl.log.DebugLog;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * 海购仓帮助类，封装一些通用的帮助方法
 */
public class APPHelper {

  public static final String TAG = APPHelper.class.getSimpleName();

  // app 默认的版本号
  public static final Integer APP_DEFAUL_VERSION_CODE = 0;

  public static int listImageDownLoadId = 0;

  /**
   * TODO 这个方法基本上没用了，暂时留着，ttid是淘系的做法
   */
  static public String getTtid() {
    String ttid = "";
//    if (HaiApplication.ttid == null || HaiApplication.ttid.equals("") || HaiApplication.ttid.equals("nottid")) {
//    } else {
//      try {
//        String platForm = "";
//        if (HaiApplication.api_env.equals("daily")) {
//          platForm = "etao_androidtest_";
//
//        } else {
//          platForm = "etao_android_";
//        }
//        ttid = HaiApplication.ttid + "@" + platForm + getVersionName(HaiApplication.context);
//      } catch (Exception e) {
//
//      }
//    }
    return ttid;
  }

  /**
   * 获取 app 版本号
   *
   * @param context context
   * @return app 版本号
   */
  public static Integer getVersionCode(Context context) {
    Integer versionName = APP_DEFAUL_VERSION_CODE;
    try {
      versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      DebugLog.e(TAG, "version name not found!");
    }
    return versionName;
  }

  /**
   * 获取当前应用的版本号
   */
  public static String getVersionName(Context context){
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo = null;
    try {
      packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return "0.0.0";
    }
    String versionName = packageInfo.versionName;
    return versionName;
  }

  public static String sign(String in) {

    String result = null;
    if (in == null) {
      return result;
    }
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      result = byte2hex(md.digest(in.getBytes("gbk")));
    } catch (Exception ex) {
      throw new RuntimeException("sign error !");
    }
    return result.toLowerCase();
  }

  public static String sign(TreeMap<String, String> params, String secret) {

    String result = null;
    if (params == null) {
      return result;
    }
    Iterator<String> iter = params.keySet().iterator();
    StringBuffer orgin = new StringBuffer(secret);
    while (iter.hasNext()) {
      String name = iter.next();
      orgin.append(name).append(params.get(name));
    }
    orgin.append(secret);
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
    } catch (Exception ex) {
      throw new RuntimeException("sign error !");
    }
    return result;
  }

  private static String byte2hex(byte[] b) {

    StringBuffer hs = new StringBuffer();
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = (Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1) {
        hs.append("0").append(stmp);
      } else {
        hs.append(stmp);
      }
    }
    return hs.toString().toUpperCase();
  }
}
