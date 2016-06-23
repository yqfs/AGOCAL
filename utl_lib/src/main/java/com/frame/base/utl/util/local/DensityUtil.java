package com.frame.base.utl.util.local;


import com.frame.base.utl.application.BaseApplication;

/**
 * 手机屏幕密度工具类
 *
 * @author WilliamChik on 2015/7/22
 */
public class DensityUtil {

  /**
   * dp转px
   */
  public static int dp2px(float dpValue) {
    if (BaseApplication.info.getScreenDensity() == 0.0f) {
      BaseApplication.info.setScreenDensity(BaseApplication.getContext().getResources().getDisplayMetrics().density);
    }
    final float scale = BaseApplication.info.getScreenDensity();
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * px转dp
   */
  public static int px2dp(float pxValue) {
    if (BaseApplication.info.getScreenDensity() == 0.0f) {
      BaseApplication.info.setScreenDensity(BaseApplication.getContext().getResources().getDisplayMetrics().density);
    }
    final float scale = BaseApplication.info.getScreenDensity();
    int result = (int) (pxValue / scale + 0.5f);
    return result;
  }

  /**
   * 获取图片高度像素
   */
  public static int getImageHeightPx() {
    int screenWidth = BaseApplication.info.getScreenWidth();
    int space = dp2px(12);
    return (screenWidth - space * 3) / 2;
  }

  public static int getRecommendItemHeightPx() {
    int screenWidth = BaseApplication.info.getScreenWidth();
    int space = dp2px(10);
    int padding = dp2px(12);
    return (screenWidth - space * 2 - padding * 2) / 3;
  }
}
