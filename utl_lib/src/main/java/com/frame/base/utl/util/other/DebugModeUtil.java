package com.frame.base.utl.util.other;

/**
 * Created by YANGQIYUN on 2016/5/9.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class DebugModeUtil {

    // 当前的包是否处于 debug 模式
    public static boolean DBG = false;

    /**
     * 在 AndroidMainifest.xml 中最好不设置 android:debuggable 属性，而是由打包方式来决定其值，避免 debug 模式取值的偏差
     */
    public static void init(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            DBG = ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
