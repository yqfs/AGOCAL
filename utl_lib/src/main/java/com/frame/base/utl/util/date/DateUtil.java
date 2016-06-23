package com.frame.base.utl.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

  //SimpleDateFormat 有线程安全的问题 by lailiao.fll
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
  private static SimpleDateFormat signSdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
  private static SimpleDateFormat shortTime = new SimpleDateFormat("HH:mm", Locale.CHINA);

  public static Date convertStrToDate(String str) {
    try {
      return sdf.parse(str);
    } catch (ParseException e) {
      e.printStackTrace();

    }
    return null;
  }

  /**
   * 专用于 temptime 请求参数的格式化
   *
   * @param date 时间戳
   */
  public static String signFormat(long date) {
    return signSdf.format(date);
  }

  public static String format(long date) {
    return sdf.format(date);
  }

  public static String format(String pattern, Date date) {
    try {
      SimpleDateFormat format = new SimpleDateFormat(pattern);
      if (date != null) {
        return format.format(date);
      }
      return format.format(new Date());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String format(String pattern, long time) {
    try {
      SimpleDateFormat format = new SimpleDateFormat(pattern);
      return format.format(new Date(time));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 取时间差
   */
  public static long diffDate(Date begin, Date end, int type) {
    long between = end.getTime() - begin.getTime();
    long s = (between / 1000);
    if (type == 1) {//天
      return between / (24 * 60 * 60 * 1000);
    } else if (type == 2) {//小时
      return (between / (60 * 60 * 1000));
    } else if (type == 3) {//分钟
      return (between / (60 * 1000));
    } else if (type == 4) {//秒
      return s;
    } else if (type == 5) {//毫秒
      return between;
    }
    return between / (24 * 60 * 60 * 1000);
  }

  /**
   * 格式化显示时间
   *
   * @return 60分钟内:n分钟前
   * >60分钟:hh24:mi
   */
  public static String timeTips(Date date) {
    //取服务端当前时间
    Date now = new Date(TimeStampUtil.getInstance().getCurrentTime());
    long diff = diffDate(date, now, 3);
    String result = "";
    if (diff <= 0) {
      result = "刚刚";
    } else if (diff > 0 && diff < 60) {
      result = String.valueOf(diff).concat("分钟前");
    } else {
      synchronized (sdf) {
        result = shortTime.format(date);
      }
    }
    return result;
  }

  /**
   * 判断date是否服务器当前时间同一天
   */
  public static boolean isSameDay(Date date) {
    Date now = new Date(TimeStampUtil.getInstance().getCurrentTime());
    return isSameDay(date, now);
  }

  /**
   * 判断两日期是否同一天
   */
  public static boolean isSameDay(Date dateA, Date dateB) {
    Calendar calDateA = Calendar.getInstance();
    calDateA.setTime(dateA);

    Calendar calDateB = Calendar.getInstance();
    calDateB.setTime(dateB);

    return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && calDateA.get(Calendar.MONTH) == calDateB
        .get(Calendar.MONTH) && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * @param tailDate 后一个时间
   * @param preDate  前一个时间
   */
  public static String buildTimeDistance(Date tailDate, Date preDate, String defaultString) {
    long diffTime = tailDate.getTime() - preDate.getTime();
    if (diffTime > 0) {
      int seconds = (int) (diffTime / 1000);
      if (seconds <= 0) {
        return "";
      }
      if (seconds < 60) {
        return seconds + "秒前";
      }

      int minutes = (int) (diffTime / 1000 / 60);
      if (minutes <= 0) {
        return "";
      }

      if (minutes > 60) {
        int hours = minutes / 60;
        if (hours > 24) {
          return sdf.format(preDate);
        } else {
          return hours + "小时前";
        }

      } else {
        return minutes + "分钟前";
      }
    }
    return defaultString;
  }
}
