package com.frame.base.utl.util.cache;


import android.content.Context;

/**
 * SharedPreferences 读取
 * Created by lailiao.fll on 2014/7/9.
 */
public class DBUtil {

  private static final String MODULE_KEY = "ETAO_APP";
  private static final String SUB_MODULE_KEY = "COMMON_";

  /**
   * 设置SharedPreferences
   */
  public static void setString(Context context,String key, String value) {
    // 更新持久化
    context.getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE).edit()
        .putString(SUB_MODULE_KEY.concat(key), value).commit();
  }

  /**
   * 取SharedPreferences
   */
  public static String getString(Context context,String key, String defaultValue) {
    return context.getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE)
        .getString(SUB_MODULE_KEY.concat(key), defaultValue);
  }

  /**
   * 设置SharedPreferences
   */
  public static void setInt(Context context,String key, int value) {
    // 更新持久化
    context.getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE).edit()
        .putInt(SUB_MODULE_KEY.concat(key), value).commit();
  }

  /**
   * 取SharedPreferences
   */
  public static int getInt(Context context,String key, int defaultValue) {
    return context.getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE)
        .getInt(SUB_MODULE_KEY.concat(key), defaultValue);
  }

  /**
   * 设置SharedPreferences
   */
  public static void setLong(Context context,String key, long value) {
    // 更新持久化
    context.getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE).edit()
        .putLong(SUB_MODULE_KEY.concat(key), value).commit();
  }

  /**
   * 取SharedPreferences
   */
  public static long getLong(Context context,String key, long defaultValue) {
    return context.getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE)
        .getLong(SUB_MODULE_KEY.concat(key), defaultValue);
  }
}
