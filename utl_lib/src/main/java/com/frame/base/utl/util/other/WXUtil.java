package com.frame.base.utl.util.other;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author YANGQIYUN on 2015/11/2.
 */
public class WXUtil {

  /**
   * 检测是否已安装微信
   * @param context
   * @return
   */
  public static boolean isWxInstalled(Context context) {
    PackageInfo packageInfo;
    try {
      packageInfo = context.getPackageManager().getPackageInfo(
          "com.tencent.mm", 0);
    } catch (PackageManager.NameNotFoundException e) {
      packageInfo = null;
      e.printStackTrace();
    }
    return packageInfo != null;
  }




}
