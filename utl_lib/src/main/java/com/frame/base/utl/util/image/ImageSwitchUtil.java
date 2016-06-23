package com.frame.base.utl.util.image;

import android.content.Context;

import com.frame.base.utl.log.DebugLog;
import com.frame.base.utl.util.net.NetWorkUtil;

import java.util.LinkedList;

/**
 * 图片切换工具，统计wifi下载速度，用于自动在webp与jpg之间切换
 * Created by cangfei.hgy on 2014/6/13.
 */
public class ImageSwitchUtil {

  private static final String LOG_TAG = "image switch";
  /**
   * 每多少张图片切换一次，10张图片检查一次
   */
  private static final int SWITCH_INTERVAL = 10;
  /**
   * 维持的数据队列大小
   */
  private static final int SPEED_HISTORY_SIZE = 50;
  /**
   * 可以使用jpg的最低速度，512kb每秒
   */
  private static final long STANDARD_SPEED = 512;
  private static ImageSwitchUtil instance = new ImageSwitchUtil();
  private int mSwitchCount;

  private long mTotalSpeed;

  private LinkedList<Double> speedHistoryList = new LinkedList<Double>();

  private ImageSwitchUtil() {
  }

  public static ImageSwitchUtil getInstance() {
    return instance;
  }

  public synchronized void completeImage(Context context,long traffic, long time) {

    // 不是wifi环境，清空wifi 速度数据
    if (!NetWorkUtil.isWifi(context)) {
      speedHistoryList.clear();
      mTotalSpeed = 0;
      mSwitchCount = 0;

      return;
    }

    // 计算请求下载速度
    double speed = traffic / time;
    speedHistoryList.addFirst(speed);
    mTotalSpeed += speed;

    if (DebugLog.isPrintLog) {
      DebugLog.d(LOG_TAG, speed + "kb/s");
    }

    // 移除老数据
    if (speedHistoryList.size() > SPEED_HISTORY_SIZE) {
      mTotalSpeed -= speedHistoryList.removeLast();
    }

    // 计算平均速度，决定是否切换webp
    mSwitchCount++;

    if (DebugLog.isPrintLog) {
      DebugLog.d(LOG_TAG, "当前下载第" + mSwitchCount + "张图片");
    }

    if (mSwitchCount % SWITCH_INTERVAL == 0) {

      long averageSpeed = mTotalSpeed / speedHistoryList.size();

      // 图片平均下载速度较快并且网络类型为WIFI，切换为jpg，否则切换为webp
      if (averageSpeed > STANDARD_SPEED) {
        ImageUtil.setWebpDynamicSwitch(false);

        if (DebugLog.isPrintLog) {
          DebugLog.d(LOG_TAG, "图片格式切换为jpg " + "平均下载速度：" + averageSpeed + "kb/s");
        }
      } else {
        ImageUtil.setWebpDynamicSwitch(true);

        if (DebugLog.isPrintLog) {
          DebugLog.d(LOG_TAG, "图片格式切换为webp " + "平均下载速度：" + averageSpeed + "kb/s");
        }
      }

      mSwitchCount = 0;
    }
  }
}
