package com.frame.base.utl.util.cache;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.util.Log;
import com.frame.base.utl.application.BaseApplication;

public class MemoryUtil {

  public static long getSystemAvailableMemory() {
    try {
      ActivityManager activityManager = (ActivityManager) BaseApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
      MemoryInfo memoryInfo = new MemoryInfo();
      activityManager.getMemoryInfo(memoryInfo);
      return memoryInfo.availMem;
    } catch (Exception e) {
      Log.e(MemoryUtil.class.getName(), "获取系统可用内存失败", e);
    }
    return 0;
  }

}
