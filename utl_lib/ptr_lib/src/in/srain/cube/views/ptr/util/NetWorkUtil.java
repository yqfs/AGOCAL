package in.srain.cube.views.ptr.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络类型判断工具类
 *
 * @author WilliamChik on 15/7/20
 */
public class NetWorkUtil {

  /**
   * 未知网络类别
   */
  public static final int NETWORK_CLASS_UNKNOWN = 0;
  public static final String NETWORK_CLASS_UNKNOWN_NAME = "UNKNOWN";

  /**
   * 2G网络
   */
  public static final int NETWORK_CLASS_2G = 1;
  public static final String NETWORK_CLASS_2G_NAME = "2G";

  /**
   * 3G网络
   */
  public static final int NETWORK_CLASS_3G = 2;
  public static final String NETWORK_CLASS_3G_NAME = "3G";

  /**
   * 4G网络
   */
  public static final int NETWORK_CLASS_4G = 3;
  public static final String NETWORK_CLASS_4G_NAME = "4G";

  /**
   * WIFI网络
   */
  public static final int NETWORK_CLASS_WIFI = 999;
  public static final String NETWORK_CLASS_WIFI_NAME = "WIFI";

  /**
   * 判断网络连接有效
   *
   * @param context Context对象
   * @return 网络是否处于连接状态（mobile or wifi)
   */
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager != null) {
      NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

      if (activeNetworkInfo != null) {
        return activeNetworkInfo.isAvailable();
      }
    }

    return false;
  }

  /**
   * 获取网络类别   2G/3G/4G/WIFI/未知
   *
   * @return 网络类别   2G/3G/4G/WIFI/未知
   */
  public static int getNetworkType(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) {
      return NETWORK_CLASS_UNKNOWN;
    }

    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    if (activeNetworkInfo == null) {
      return NETWORK_CLASS_UNKNOWN;
    }

    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
      // wifi 网络
      return NETWORK_CLASS_WIFI;
    } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
      // mobile 网络
      return getMobileNetworkClass(activeNetworkInfo.getSubtype());
    } else {
      // 未知
      return NETWORK_CLASS_UNKNOWN;
    }
  }

  /**
   * 获取网络类别名称   2G/3G/4G/WIFI/未知
   *
   * @return 网络类别名称   2G/3G/4G/WIFI/未知
   */
  public static String getNetworkTypeName(Context context) {
    switch (getNetworkType(context)) {
      case NETWORK_CLASS_WIFI:
        return NETWORK_CLASS_WIFI_NAME;
      case NETWORK_CLASS_2G:
        return NETWORK_CLASS_2G_NAME;
      case NETWORK_CLASS_3G:
        return NETWORK_CLASS_3G_NAME;
      case NETWORK_CLASS_4G:
        return NETWORK_CLASS_4G_NAME;
      case NETWORK_CLASS_UNKNOWN:
        return NETWORK_CLASS_UNKNOWN_NAME;
      default:
        return NETWORK_CLASS_UNKNOWN_NAME;
    }
  }

  /**
   * 获取移动网络类别，2G/3G/4G/未知，参见 TelephonyManager的getNetworkClass方法
   */
  private static int getMobileNetworkClass(int networkType) {
    switch (networkType) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
      case TelephonyManager.NETWORK_TYPE_EDGE:
      case TelephonyManager.NETWORK_TYPE_CDMA:
      case TelephonyManager.NETWORK_TYPE_1xRTT:
      case TelephonyManager.NETWORK_TYPE_IDEN:
        return NETWORK_CLASS_2G;
      case TelephonyManager.NETWORK_TYPE_UMTS:
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
      case TelephonyManager.NETWORK_TYPE_HSDPA:
      case TelephonyManager.NETWORK_TYPE_HSUPA:
      case TelephonyManager.NETWORK_TYPE_HSPA:
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
      case TelephonyManager.NETWORK_TYPE_EHRPD:
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return NETWORK_CLASS_3G;
      case TelephonyManager.NETWORK_TYPE_LTE:
        return NETWORK_CLASS_4G;
      default:
        return NETWORK_CLASS_UNKNOWN;
    }
  }

  /**
   * 是否在WIFI环境下
   */
  public static boolean isWifi(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) {
      return false;
    }

    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    if (networkInfo == null) {
      return false;
    }

    return networkInfo.isConnectedOrConnecting();
  }

  /**
   * 是否在2G环境下
   */
  public static boolean is2G(Context context) {
    return getNetworkType(context) == NETWORK_CLASS_2G;
  }
}
