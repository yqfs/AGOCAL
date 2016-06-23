package com.frame.base.utl.cache;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.frame.base.utl.log.DebugLog;
import com.frame.base.utl.util.other.DiskFileUtils;
import com.frame.base.utl.util.other.FastJsonUtil;

import java.io.File;

/**
 * 海购仓缓存模块，基于 ACache 再做一层包装，采用硬件缓存的方式进行存储，代替SharedPreferences处理所有和 app 持久化相关的缓存
 *
 * @author WilliamChik on 2015/8/21
 */
public class CacheCenterModule {

  private static final String TAG = CacheCenterModule.class.getSimpleName();

  /**
   * STABLE最小的缓存文件大小
   */
  private static final long STABLE_CACHE_FILE_SIZE = 8 * 1024 * 1024;

  /**
   * STABLE缓存文件大小FACTOR
   */
  private static final int STABLE_CACHE_FILE_FACTOR = 2;

  /**
   * STABLE缓存文件名
   */
  private static final String STABLE_CACHE_FILE_NAME = "persistance_cache";

  /**
   * ACache 对象，属于硬件缓存
   */
  private static ACache mACache;

  public static void init(Context context) {
    initPersistenceCache(context, STABLE_CACHE_FILE_NAME, STABLE_CACHE_FILE_FACTOR, STABLE_CACHE_FILE_SIZE);
  }

  /**
   * 根据缓存文件的路径和大小参数，初始化持久化缓存
   * 优先使用 External Storage，当 External Storage 无法使用时，使用 internal cache dir
   *
   * @param context    context
   * @param fileName   缓存文件名
   * @param sizeFactor 缓存大小因子，代表每一个G的文件系统，使用多少M来作为缓存；例如 factor 为 5，如果 external storage 总容量为8G,则使用 8*5=40M 的大小作为缓存
   * @param minSize    最小的缓存大小，如果计算出来的默认 size 小于这个值，则以这个值作为缓存大小
   */
  private static void initPersistenceCache(Context context, String fileName, int sizeFactor, long minSize) {

    // external cache
    try {
      String state = android.os.Environment.getExternalStorageState();
      // 判断SD卡是否存在，并且是否具有读写权限
      if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
        initACache(DiskFileUtils.getExternalCacheDir(context), fileName, sizeFactor, minSize);
      }
    } catch (Exception e) {
      DebugLog.e(TAG, "get external cache dir error ", e);
    }

    // internal cache
    try {
      if (context.getCacheDir() != null) {
        // 内部缓存容量减半
        initACache(context.getCacheDir(), fileName, sizeFactor / 2, minSize / 2);
      }
    } catch (Exception e) {
      Log.e(TAG, "get internal cache dir error ", e);
    }

  }

  /**
   * 根据缓存文件的路径和大小参数，初始化持久化缓存
   *
   * @param fileDir    sd上的缓存目录，正常返回的情况下是 : /storage/sdcard0/Android/data/com.srain.sdk/cache
   * @param fileName   缓存文件名
   * @param sizeFactor 缓存大小因子，代表每一个G的文件系统，使用多少M来作为缓存；例如 factor 为5，如果 external storage 总容量为8G,则使用 8*5=40M 的大小作为缓存
   * @param minSize    最小的缓存大小，如果计算出来的缓存 size 小于这个值，则以这个值作为缓存大小
   */
  private static void initACache(File fileDir, String fileName, int sizeFactor, long minSize) {
    //计算存储区域大小
    long size = 0;

    StatFs stat = new StatFs(fileDir.getPath());
    if (Build.VERSION.SDK_INT >= 18) {
      size = stat.getBlockSizeLong() * stat.getBlockCountLong();
    } else {
      size = (long) stat.getBlockSize() * (long) stat.getBlockCount();
    }

    size = (size * sizeFactor) / 1024;

    // 最小缓存空间检查
    if (size < minSize) {
      size = minSize;
    }

    //存储文件path
    String path = fileDir + File.separator + fileName;

    DebugLog.i(TAG, "cache file parameters - size: " + size + " path: " + path);

    if (size > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("maxSize exceeds the ACache size limit!");
    }

    initACache(path, size);
  }

  /**
   * 初始化ACache
   *
   * @param cacheFilePath 缓存文件 Path
   * @param maxSize       最大的缓存文件大小，单位 byte
   */
  private static void initACache(String cacheFilePath, long maxSize) {

    if (maxSize > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("maxSize exceeds the ACache size limit!");
    }

    try {
      mACache = ACache.get(new File(cacheFilePath), maxSize, Integer.MAX_VALUE);
      //  开启先进先出 的缓存策略
//      mACache.set2FIFO(true);
      if (mACache == null) {
        DebugLog.e(TAG, "ACache open failed " + cacheFilePath + " " + maxSize);
      } else if (DebugLog.isPrintLog) {
        DebugLog.d(TAG, "ACache open success " + cacheFilePath + " " + maxSize);
      }
    } catch (Exception e) {
      DebugLog.e(TAG, "ACache open error " + cacheFilePath + " " + maxSize, e);
    }
  }

  public static boolean writeObjectToCache(String key, Object value) {
    return writeObjectToCache(key, value, 0);
  }

  /**
   * 保存 Object 数据到缓存中
   *
   * @param key      保存的 key
   * @param value    保存的 Object 数据
   * @param saveTime 保存的时间，单位是秒
   * @return true 保存成功 | false 保存失败
   */
  public static boolean writeObjectToCache(String key, Object value, int saveTime) {

    if (mACache == null || TextUtils.isEmpty(key) || value == null) {
      return false;
    }

    try {
      boolean writeSuccess;
      if (saveTime > 0) {
        writeSuccess = mACache.put(key, JSON.toJSONString(value), saveTime);
      } else {
        writeSuccess = mACache.put(key, JSON.toJSONString(value));
      }

      if (writeSuccess) {
        if (DebugLog.isPrintLog) {
          DebugLog.d(TAG, key + " write ACache success");
        }

        return true;
      } else {
        DebugLog.e(TAG, key + " write ACache failed");
      }
    } catch (Throwable e) {
      DebugLog.e(TAG, key + " write ACache exception ", e);
    }

    return false;
  }

  /**
   * 获取 key 存储的对应类型的数据
   *
   * @param key 缓存 key
   * @param cls 缓存的对象类型，如果是泛型数据，则指定为 TypeReference，如果是普通的 Class，则指定为 Class
   * @param <T> 泛型
   * @return key 对应的缓存值，如果出错则返回 null
   */
  public static <T> T readFromCache(String key, Object cls) {
    if (mACache == null || TextUtils.isEmpty(key) || cls == null) {
      return null;
    }

    String cacheString = mACache.getAsString(key);
    if (cacheString == null || cacheString.length() == 0) {
      return null;
    }

    if (cls instanceof TypeReference) {
      // 解析泛型
      return (T) FastJsonUtil.parseObject(cacheString, (TypeReference) cls);
    } else {
      // 解析普通 Class
      return (T) FastJsonUtil.parseObject(cacheString, (Class) cls);
    }
  }

  public static void clear() {
    if (mACache != null) {
      mACache.clear();
    }
  }

}
