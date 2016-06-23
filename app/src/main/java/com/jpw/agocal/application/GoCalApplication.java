package com.jpw.agocal.application;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.baidu.location.service.LocationService;
import com.frame.base.utl.application.BaseApplication;
import com.hyphenate.easeui.controller.EaseUI;
import com.jpw.agocal.common.PanelFormManager;

/**
 * Created by YANGQIYUN on 2016/5/23.
 */
public class GoCalApplication extends BaseApplication {

    public static LocationService locationService;
    public Vibrator mVibrator;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
//        EaseUI.getInstance().init(this,null);
        MultiDex.install(this);
        new PanelFormManager().getInstance();

        info.setAPI("http://t-apichebao.bishe.com");

        /***
         * 初始化定位sdk，建议在Application中创建
         */
//        locationService = new LocationService(getApplicationContext());
//        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(getApplicationContext());

        sp = getSharedPreferences("userInfo", 0);

        Log.d("yqy",sp.getString("user_id","null"));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static LocationService getLocationService(){
        return locationService;
    }
}
