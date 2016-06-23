package com.frame.base.utl.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.frame.base.utl.activity.SwipeBackActivity;
import com.frame.base.utl.andfix.AndfixManager;
import com.frame.base.utl.cache.CacheCenterModule;
import com.frame.base.utl.entity.AppInfo;
import com.frame.base.utl.log.CrashHandler;
import com.frame.base.utl.util.other.DebugModeUtil;
import com.squareup.leakcanary.LeakCanary;

import java.util.Enumeration;
import java.util.Hashtable;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by YANGQIYUN on 2016/5/11.
 */
public class BaseApplication extends Application{

    public static AppInfo info;
    // Activity 栈，保存打开过的所有 Activity
    public static Hashtable<Activity, Activity> hashActivity = new Hashtable<Activity, Activity>();
    // 当前activity
    public static SwipeBackActivity activeActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        //配置参数
        info = new AppInfo();
        info.init(this);
        //阿里在线修复andfix
//        new AndfixManager(this).initAndfix("1.0");

        //LeakCanary检测OOM
        LeakCanary.install(this);

        DebugModeUtil.init(this);

        CacheCenterModule.init(this);

        //初始化日志输出工具
        CrashHandler.init(new CrashHandler(getApplicationContext()));

    }
    // 获取ApplicationContext
    public static Context getContext() {
        return info.getInstance();
    }

    /**
     * 严苛模式 线程警告
     */
    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll()  //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }
    /**
     * app退出的相关操作
     */
    public static void doAppExit() {
        // 强制关闭所有页面
        Enumeration<Activity> emu = hashActivity.elements();
        while (emu.hasMoreElements()) {
            Activity ac = emu.nextElement();
            ac.finish();
        }
        // 清除已执行和正在执行的页面列表
        hashActivity.clear();
        activeActivity = null;

    }
    /**
     * activity 启动时调用
     */
    public static void onAllActivityCreate(Activity activity) {
        hashActivity.put(activity, activity);
    }

    /**
     * activity 销毁时调用
     */
    public static void onAllActivityDestroy(Activity activity) {
        hashActivity.remove(activity);
    }

    /**
     * app重启
     */
    public static void doAppRestart() {
        System.exit(0);
    }
}
