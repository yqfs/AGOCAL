package com.frame.base.utl.util.cache;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class MemoryMgr {

  public static final int ERROR = -1;

  public static long getSDSize() {
    File path = Environment.getExternalStorageDirectory();
    StatFs statFs = new StatFs(path.getPath());
    long blockSize = statFs.getBlockSize();

    long availableBlocks = statFs.getAvailableBlocks();
    return availableBlocks * blockSize;
  }

  public static boolean checkSDCard() {
    String state = Environment.getExternalStorageState();
    return (state != null) && (state.equals("mounted"));

  }

  public static long getAvailableInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    return availableBlocks * blockSize;
  }

  public static long getAvailableExternalMemorySize() {
    if (externalMemoryAvailable()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs stat = new StatFs(path.getPath());
      long blockSize = stat.getBlockSize();
      long availableBlocks = stat.getAvailableBlocks();
      return availableBlocks * blockSize;
    }
    return -1L;
  }

  public static boolean externalMemoryAvailable() {
    return Environment.getExternalStorageState().equals("mounted");
  }
}