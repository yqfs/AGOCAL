package com.frame.base.utl.entity;

import android.util.DisplayMetrics;

import com.frame.base.utl.application.BaseApplication;

/**
 * 屏幕配置管理类
 * Created by YANGQIYUN on 2016/5/10.
 */
public class ScreenManager {

    public ScreenManager(){
        DisplayMetrics metrics = BaseApplication.getContext().getResources().getDisplayMetrics();
        BaseApplication.info.setScreenDensity(metrics.density);
        if (metrics.widthPixels > metrics.heightPixels) {
            BaseApplication.info.setScreenWidth(metrics.heightPixels);
            BaseApplication.info.setScreenHeight(metrics.widthPixels);
        } else {
            BaseApplication.info.setScreenWidth(metrics.widthPixels);
            BaseApplication.info.setScreenHeight(metrics.heightPixels);
        }

//        int naviBarSize = NavigationBar.navigationBarItems.length;
        int naviBarSize = 40;
        float screenWidth = BaseApplication.info.getScreenWidth();
        float minNaviItemWidth = BaseApplication.info.getScreenDensity() * 59;
        BaseApplication.info.setNaviItemWidth((int)minNaviItemWidth);
        if (screenWidth <= minNaviItemWidth * naviBarSize) {
            // 导航条总长度 = 每个导航项最小宽度 * 导航项个数，
            // 如果屏幕宽度 <= 导航条总长度，屏幕只能放下(screenWidth / minNaviItemWidth)个导航项，
            // 放下(screenWidth / minNaviItemWidth)个导航项后，将屏幕余下的宽度平均再分给各个导航项，即是各个导航项的实际宽度。
            BaseApplication.info.setNaviItemWidth((int) (minNaviItemWidth +
                    (screenWidth % minNaviItemWidth) / ((int) screenWidth / minNaviItemWidth)));
        } else {
            // 如果屏幕 > 导航条总长度，则导航条的宽度就是屏幕宽度的均分值
            BaseApplication.info.setNaviItemWidth((int) (screenWidth / naviBarSize));
        }
    }
}
