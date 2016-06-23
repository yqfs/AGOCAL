package com.frame.base.utl.util.other;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.Log;

import com.frame.base.utl.R;
import com.frame.base.utl.application.BaseApplication;

/**
 * Created by YANGQIYUN on 2014/7/9.
 */
public class ThemeUtil {

  public static final String THEME_KEY = "CURRENT_THEME";
  public static int CURRENT_THEME = getInt(THEME_KEY, ThemeModel.DAILY.themeId);
  private static final String MODULE_KEY = "ETAO_APP";
  private static final String SUB_MODULE_KEY = "THEME_MODEL";


  /**
   * Set the theme of the Activity, and restart it by creating a new Activity
   * of the same type.
   */
  public static void changeToTheme(Activity activity, int theme) {
    changeToTheme(activity, theme, null);
  }

  public static void changeToTheme(Activity activity, int theme, OnThemeChangedListener themeChangedListener) {
    CURRENT_THEME = theme;
    setInt(THEME_KEY, theme);
    if (themeChangedListener != null) {
      themeChangedListener.onThemeChanged(theme);
    }
    //activity.recreate();
        /*
                activity.finish();
		activity.startActivity(new Intent(activity, activity.getClass()));
		*/
  }

  /**
   * Set the theme of the activity, according to the configuration.
   */
  public static void onActivityCreateSetTheme(Activity activity) {
    if (CURRENT_THEME == ThemeModel.DAILY.themeId) {
      activity.setTheme(ThemeModel.DAILY.themeId);
    } else if (CURRENT_THEME == ThemeModel.NIGHT.themeId) {
      activity.setTheme(ThemeModel.NIGHT.themeId);
    } else {
      activity.setTheme(ThemeModel.DAILY.themeId);
    }
  }

  /**
   * 日间/夜间切换
   */
  public static ThemeModel toggleTheme(Activity activity) {
    if (ThemeUtil.CURRENT_THEME == ThemeModel.DAILY.themeId) {
      ThemeUtil.changeToTheme(activity, ThemeModel.NIGHT.themeId);
      return ThemeModel.NIGHT;
    } else {
      ThemeUtil.changeToTheme(activity, ThemeModel.DAILY.themeId);
      return ThemeModel.DAILY;
    }
  }

  /**
   * 日间/夜间切换
   */
  public static ThemeModel toggleTheme(Activity activity, OnThemeChangedListener themeChangedListener) {
    if (ThemeUtil.CURRENT_THEME == ThemeModel.DAILY.themeId) {
      ThemeUtil.changeToTheme(activity, ThemeModel.NIGHT.themeId, themeChangedListener);
      return ThemeModel.NIGHT;
    } else {
      ThemeUtil.changeToTheme(activity, ThemeModel.DAILY.themeId, themeChangedListener);
      return ThemeModel.DAILY;
    }
  }

  /**
   * 重新加载
   */
  public static void reload(Activity activity) {
    Intent intent = activity.getIntent();
    activity.overridePendingTransition(0, 0);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    activity.finish();
    activity.overridePendingTransition(0, 0);
    activity.startActivity(intent);
  }

  /**
   * 设置SharedPreferences
   */
  public static void setString(String key, String value) {
    // 更新持久化
    BaseApplication.getContext().getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE).edit()
        .putString(SUB_MODULE_KEY.concat(key), value).commit();
  }

  /**
   * 取SharedPreferences
   */
  public static String getString(String key, String defaultValue) {
    return BaseApplication.getContext().getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE)
        .getString(SUB_MODULE_KEY.concat(key), defaultValue);
  }

  /**
   * 设置SharedPreferences
   */
  public static void setInt(String key, int value) {
    // 更新持久化
    BaseApplication.getContext().getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE).edit()
        .putInt(SUB_MODULE_KEY.concat(key), value).commit();
  }

  /**
   * 取SharedPreferences
   */
  public static int getInt(String key, int defaultValue) {
    return BaseApplication.getContext().getSharedPreferences(MODULE_KEY, Context.MODE_PRIVATE)
        .getInt(SUB_MODULE_KEY.concat(key), defaultValue);
  }

  /**
   * 动态取attr rid,defaultRid 不可省略
   */
  public static int getAttribute(Context context, int styleableId, int defaultRid) {
    long start = System.currentTimeMillis();
    int value = -1;
    try {
      //getTheme().obtainStyledAttributes(R.styleable.EtaoSkin);//.applyStyle();
      TypedArray typedArray = context.obtainStyledAttributes(R.styleable.EtaoSkin);
      value = typedArray.getResourceId(styleableId, defaultRid);
      typedArray.recycle();
    } catch (Exception ex) {
    }
    Log.v("getAttribute", "times:" + (System.currentTimeMillis() - start));
    return value;

  }

  /**
   * @return
   */
    /**/
  public static int getAttribute(Context context, int styleableId) {
    return getAttribute(context, styleableId, -1);
  }

  public static void setAttribute(Context context, int styleableId) {
    try {
      context.getTheme().applyStyle(styleableId, true);//.applyStyle();
      //TypedArray typedArray = context.obtainStyledAttributes(R.styleable.EtaoSkin);
      //value = typedArray.getResourceId(styleableId,-1);
      //typedArray.recycle();
    } catch (Exception ex) {
    }
  }

  public enum ThemeModel {
    DAILY(R.style.Theme_Base, "日间模式"),
    NIGHT(R.style.Theme_Base_Night, "夜间模式");

    public int themeId;
    public String themeName;

    ThemeModel(int themeId, String modelName) {
      this.themeId = themeId;
      this.themeName = modelName;
    }

    static ThemeModel mapIntToValue(final int themeId) {
      for (ThemeModel value : ThemeModel.values()) {
        if (themeId == value.themeId) {
          return value;
        }
      }
      // If not, return default
      return getDefault();
    }

    static ThemeModel getDefault() {
      return DAILY;
    }
  }

  /**
   * theme变更时回调
   */
  public interface OnThemeChangedListener {

    //public void onChanged(String newThemePackageName);
    void onThemeChanged(int themeId);
  }
}
