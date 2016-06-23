package com.frame.base.utl.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * log工具
 * @author YANGQIYUN on 15/7/20
 */
public final class DebugLog {

  public static boolean isPrintLog = true;

  public static void setLogSwitcher(boolean open) {
    isPrintLog = open;
  }

  /**
   * 可变参数的debug log。 第一个是tag，其他的会帮助拼接
   *
   * @param msgArray 可变参数
   */
  public static void d(String... msgArray) {
    if (isPrintLog && msgArray.length > 2) {
      Log.d(msgArray[0], getString(msgArray));
    }
  }

  /**
   * 拼接可变参数
   */
  private static String getString(String[] msgArray) {
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < msgArray.length; i++) {
      sb.append(msgArray[i]);
    }
    return sb.toString();
  }

  /**
   * 普通的debug log
   */
  public static void d(String tag, String msg) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
      return;
    }
    if (isPrintLog) {
      Log.d(tag, msg);
    }
  }

  /**
   * 可变参数的error log。 第一个就是tag，其他的会帮助拼接
   */
  public static void e(String... msgArray) {
    if (isPrintLog && msgArray.length > 2) {
      Log.e(msgArray[0], getString(msgArray));
    }
  }

  /**
   * 普通的error log
   */
  public static void e(String tag, String msg) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
      return;
    }
    if (isPrintLog) {
      Log.e(tag, msg);
    }
  }

  /**
   * 带抛出异常的error log
   */
  public static void e(String tag, String msg, Throwable tr) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
      return;
    }
    if (isPrintLog) {
      Log.e(tag, msg, tr);
    }
  }

  /**
   * 可变参数的info log。 第一个就是tag，其他的会帮助拼接
   */
  public static void i(String... msgArray) {
    if (isPrintLog && msgArray.length > 2) {
      Log.i(msgArray[0], getString(msgArray));
    }
  }

  /**
   * 普通的info log
   */
  public static void i(String tag, String msg) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
      return;
    }
    if (isPrintLog) {
      Log.i(tag, msg);
    }
  }

  /**
   * 普通的verbose log
   */
  public static void v(String tag, String msg) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
      return;
    }
    if (isPrintLog) {
      Log.v(tag, msg);
    }
  }


  /**
   * 可变参数的warn log。 第一个就是tag，其他的会帮助拼接
   */
  public static void w(String... msgArray) {
    if (isPrintLog && msgArray.length > 2) {
      Log.w(msgArray[0], getString(msgArray));
    }
  }

  /**
   * 普通的warn log
   */
  public static void w(String tag, String msg) {
    if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
      return;
    }
    if (isPrintLog) {
      Log.w(tag, msg);
    }
  }
}
