package com.frame.base.utl.util.assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.frame.base.utl.util.other.IoUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取 assets 资源的工具类
 *
 * @author YANGQIYUN on 15/12/21.
 */
public class AssetsUtil {

  /**
   * 获取引导图片
   *
   * @param imgDirName    图片目录名
   * @param imgFileSuffix 图片文件的前缀，如"guide_"
   * @param imgPrefix     图片文件的后缀名，如".png"
   */
  public static Bitmap[] getBitmapArr(Context context,String imgDirName, String imgPrefix, String imgFileSuffix) {
    String[] picNames = null;
    try {
      picNames = context.getAssets().list(imgDirName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (picNames == null) {
      return null;
    }

    Bitmap[] bitmaps = new Bitmap[picNames.length];
    String imgFileName = imgDirName + File.separator + imgPrefix;
    for (int i = 0; i < picNames.length; i++) {
      try {
        bitmaps[i] = getBitmap(context,imgFileName + i + imgFileSuffix, 1);
      } catch (OutOfMemoryError error) {
        // 如果发现OOM
        // 先把之前的 bitmap 回收
        for (int j = 0; j < picNames.length; j++) {
          if (bitmaps[j] != null) {
            bitmaps[j].recycle();
            bitmaps = null;
          }
        }

        // 再把全部图片压缩一半进行显示
        for (int k = 0; k < picNames.length; k++) {
          bitmaps[k] = getBitmap(context,imgFileName + i + imgFileSuffix, 2);
        }
        break;
      }
    }

    return bitmaps;
  }

  /**
   * 根据地址获取指定图片的 bitmap
   *
   * @param imgFileName  图片路径
   * @param inSampleSize 缩小的倍数，例如 2 表示缩小到原来的一半
   */
  public static Bitmap getBitmap(Context context,String imgFileName, int inSampleSize) {
    InputStream fis = null;
    Bitmap bm = null;
    try {
      fis = context.getAssets().open(imgFileName);
      BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = inSampleSize;
      bm = BitmapFactory.decodeStream(fis, null, o2);
    } catch (Exception e1) {
      e1.printStackTrace();
      bm = null;
    } finally {
      IoUtil.closeCloseable(fis);
    }

    return bm;
  }

}
