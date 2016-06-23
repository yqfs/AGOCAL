package com.frame.base.utl.entity;

import android.app.Application;

/**
 * Created by YANGQIYUN on 2016/5/11.
 */
public class AppInfo {

    /**
     * 域名
     */
    private String API = "http://t-apichebao.bishe.com";
    //application下上文
    private Application instance;
    //缓存路径
    private String cacheDir;

    // 系统版本
    private String systemVersion;
    /**
     * 屏幕参数
     */
    private float ScreenDensity = 0;
    private int ScreenWidth = 480;
    private int ScreenHeight = 800;
    // 导航栏item宽度
    private int naviItemWidth = 0;

    private boolean userChanged;
    // 环境识别
    private String api_env;
    // 是否自动切换图片质量
    private boolean autoSwitchPic;
    // 是否切换到了高质量图片
    private boolean switch2high;

    public AppInfo(){

    }
    public void init(Application instance){
        this.instance = instance;
        //初始化屏幕变量
        new ScreenManager();
        //初始化配置信息
        new DebugManager();
    }

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public boolean isSwitch2high() {
        return switch2high;
    }

    public void setSwitch2high(boolean switch2high) {
        this.switch2high = switch2high;
    }

    public boolean isAutoSwitchPic() {
        return autoSwitchPic;
    }

    public void setAutoSwitchPic(boolean autoSwitchPic) {
        this.autoSwitchPic = autoSwitchPic;
    }

    public String getApi_env() {
        return api_env;
    }

    public void setApi_env(String api_env) {
        this.api_env = api_env;
    }

    public boolean isUserChanged() {
        return userChanged;
    }

    public void setUserChanged(boolean userChanged) {
        this.userChanged = userChanged;
    }

    public int getNaviItemWidth() {
        return naviItemWidth;
    }

    public void setNaviItemWidth(int naviItemWidth) {
        this.naviItemWidth = naviItemWidth;
    }

    public int getScreenHeight() {
        return ScreenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        ScreenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return ScreenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        ScreenWidth = screenWidth;
    }

    public float getScreenDensity() {
        return ScreenDensity;
    }

    public void setScreenDensity(float screenDensity) {
        ScreenDensity = screenDensity;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getCacheDir() {
        if(cacheDir == null || cacheDir.length() == 0){
            /**
             * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
             */
            if (instance.getExternalCacheDir() != null && isExistSDCard()) {
                cacheDir = instance.getExternalCacheDir().toString();

            }
            else {
                cacheDir = instance.getCacheDir().toString();
            }
        }
        return cacheDir;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public Application getInstance() {
        return instance;
    }

    public void setInstance(Application instance) {
        this.instance = instance;
    }

    /**
     * 检查是否存在sd卡
     */
    private boolean isExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
