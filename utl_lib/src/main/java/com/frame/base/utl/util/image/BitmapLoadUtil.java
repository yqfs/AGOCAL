package com.frame.base.utl.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.frame.base.utl.application.BaseApplication;

/**
 * 本地图片加载工具
 * Created by cangfei.hgy on 2014/5/26.
 */
public class BitmapLoadUtil {

  /**
   * 根据resId解析资源
   */
  public static Bitmap decodeResource(int resId, int sampleSize) {
    BitmapFactory.Options opts = null;
    if (sampleSize > 0) {
      opts = new BitmapFactory.Options();
      opts.inSampleSize = sampleSize;
    }

    Bitmap bmp;

    try {
      if (opts != null) {
        bmp = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), resId, opts);
      } else {
        bmp = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), resId);
      }
    } catch (Exception e) {
      bmp = null;
    }

    return bmp;
  }
}
