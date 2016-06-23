package com.frame.base.utl.util.date;

import android.os.SystemClock;

import com.frame.base.utl.log.DebugLog;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 时间戳工具
 *
 * @author cangfei.hgy 2013-9-10
 */
public class TimeStampUtil {

  private static TimeStampUtil instance;

  /**
   * 服务端基准时间
   */
  private long baseServerTime;

  /**
   * 服务端基准时间返回后的消逝时长
   */
  private long baseTimeElapsed;

  private boolean taskRunning;

  /**
   * 三八节活动开始时间
   */
  private long beginTime;

  /**
   * 三八节活动结束时间
   */
  private long endTime;

  private TimeStampUtil() {
  }

  public static TimeStampUtil getInstance() {
    if (instance == null) {
      instance = new TimeStampUtil();
    }
    return instance;
  }

//    /**
//     * 获取时间差值
//     */
//    private void getOffsetTime() {
//        if (taskRunning) {
//            return;
//        }
//        new Thread() {
//
//            @Override
//            public void run() {
//                doGetOffsetTime();
//            }
//
//        }.start();
//    }

//    /**
//     * 获取时间差值
//     */
//    public void doGetOffsetTime() {
//        if (taskRunning) {
//            return;
//        }
//        taskRunning = true;
//        EtaoMtopResult<TimeStampData> result = getTimeFromServer();
//        if (result != null && result.isSuccess() && result.getData() != null) {
//            Log.i("doGetOffsetTime", "同步时间成功！！！");
//            saveTime(result.getData().getTime());
//        }
//        get38Time();
//        taskRunning = false;
//    }

  /**
   * 获取三八节活动时间
   */
  private void get38Time() {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(2014, Calendar.MARCH, 5, 0, 0, 1);
    beginTime = calendar.getTimeInMillis();
    calendar.set(2014, Calendar.MARCH, 31, 23, 59, 59);
    endTime = calendar.getTimeInMillis();
  }

//    /**
//     * 从服务器获取时间
//     *
//     * @return
//     */
//    private EtaoMtopResult<TimeStampData> getTimeFromServer() {
//        EtaoMtopResult<TimeStampData> result = new HaiRequestManager(MtopApiInfo.TIME_STAMP).syncRequest(new HashMap<String, String>());
//        return result;
//    }

  /**
   * 计算时间差值
   *
   * @param serverTime 服务端时间
   */
  private void saveTime(long serverTime) {
    // 保存服务器时间
    baseServerTime = serverTime;
    DebugLog.d("Init Server Time", String.valueOf(serverTime));
    // 记录此时的系统启动后时间
    baseTimeElapsed = SystemClock.elapsedRealtime();
  }

  /**
   * 获取请求时间（秒）
   */
  public long getRequestTime() {
    long currentTime = getCurrentTime();

    return currentTime / 1000;
  }

  /**
   * 获取当前时间（毫秒）
   */
  public long getCurrentTime() {
    // 默认使用手机本地时间
    long currentTime = System.currentTimeMillis();

    // 如果服务器基准时间与开机时长均存在，计算时间
    if (baseServerTime > 0 && baseTimeElapsed > 0) {
      // 当前时间 = 服务端基准时间 + 当前消逝时长 - 服务端基准时间返回后的消逝时长
      currentTime = baseServerTime + SystemClock.elapsedRealtime() - baseTimeElapsed;
    } else {
//      // 同步服务器时间
//      getOffsetTime();
    }

    return currentTime;
  }

  public long getBeginTime() {
    return beginTime;
  }

  public long getEndTime() {
    return endTime;
  }
}
