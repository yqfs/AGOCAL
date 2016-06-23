package com.frame.base.utl.entity;

import android.os.Build;

import com.frame.base.utl.R;
import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.cache.CacheCenterModule;
import com.frame.base.utl.constant.Constants;
import com.frame.base.utl.util.other.DebugModeUtil;

/**
 * 环境管理类
 * Created by YANGQIYUN on 2016/5/9.
 */
public class DebugManager {

    public DebugManager(){
        // log开关初始化
        if ("0".equals(BaseApplication.getContext().getResources().getString(R.string.isPrintHaiLog))) {
//            HaiLog.setLogSwitcher(false);
        } else if ("1".equals(BaseApplication.getContext().getResources().getString(R.string.isPrintHaiLog))) {
//            HaiLog.setLogSwitcher(true);
        }

        BaseApplication.info.setSystemVersion(Build.VERSION.RELEASE);

        // 服务端 host 初始化
        String apiEnv = "2";
        if (DebugModeUtil.DBG) {
            // debug 模式下读取 debug 工具的缓存变量
            apiEnv = CacheCenterModule.readFromCache(Constants.API_ENV_CACHE_KEY, String.class);
        }
        if ("0".equals(apiEnv)) {
            // 日常环境
            changeToDaily();
        } else if ("1".equals(apiEnv)) {
            // 预发环境
            changeToPre();
        } else {
            // 生产环境
            changeToOnline();
        }

        // H5 静态页面 host 初始化
        String staticH5Env = "1";
        if (DebugModeUtil.DBG) {
            // debug 模式下读取 debug 工具的缓存变量
            staticH5Env = CacheCenterModule.readFromCache(Constants.STATIC_H5_ENV_CACHE_KEY, String.class);
        }
        if ("0".equals(staticH5Env)) {
            // 测试环境
            BaseApplication.info.setAPI(BaseApplication.getContext().getString(R.string.daily_h5_base_host));
        } else {
            // 线上环境
            BaseApplication.info.setAPI(BaseApplication.getContext().getString(R.string.online_h5_base_host));
        }

        // TODO 初始化各类开关、跟环境无关的url和登陆特征等，这些参数需要配置在config.xml中。

        // 初始化版本号
//    initVersion();
    }
    /**
     * 切换到日常环境
     * TODO 主要是做一些request url的改变，{@link #changeToPre()}和{@link #changeToOnline()}类似
     */
    public static void changeToDaily() {
        BaseApplication.info.setApi_env("daily");
        BaseApplication.info.setAPI(BaseApplication.getContext().getString(R.string.daily_api_base_host));
    }

    /**
     * 切换到预发环境
     */
    public static void changeToPre() {
        BaseApplication.info.setApi_env("pre");
        BaseApplication.info.setAPI(BaseApplication.getContext().getString(R.string.pre_api_base_host));
    }

    /**
     * 切换到线上环境
     */
    public static void changeToOnline() {
        BaseApplication.info.setApi_env("online");
        BaseApplication.info.setAPI(BaseApplication.getContext().getString(R.string.online_api_base_host));
    }
}
