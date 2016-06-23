package com.frame.base.utl.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.base.utl.R;
import com.frame.base.utl.cache.CacheCenterModule;
import com.frame.base.utl.constant.Constants;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by YANGQIYUN on 2016/5/9.
 */
public class DebugControllBoard extends CommonBaseSafeDialog{

    private Holder holder;
    //切换完成，重启app监听
    private OnFinshListener onFinshListener;

    // api 默认线上环境
    private String mApiEnvTag = "2";

    // 静态 h5 页面默认线上环境
    private String mStaticH5EnvTag = "1";

    // https 证书验证默认开启
    private String mHttpsValidateSwicthTag = "1";

    public DebugControllBoard(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_controll_board);
        initUI();
    }

    private void initUI() {
        initApiEnvSwitch();
        initStaticH5EnvSwitch();
        initHttpsValidateSwitch();
        initConfirmBtn();
    }

    /**
     * 【取消】和【确认】按钮
     */
    private void initConfirmBtn() {
        TextView leftButton = (TextView) findViewById(R.id.tv_debug_left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugControllBoard.this.dismiss();
            }
        });
        TextView rightButton = (TextView) findViewById(R.id.tv_debug_right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheCenterModule.writeObjectToCache(Constants.API_ENV_CACHE_KEY, mApiEnvTag);
                CacheCenterModule.writeObjectToCache(Constants.STATIC_H5_ENV_CACHE_KEY, mStaticH5EnvTag);
                CacheCenterModule.writeObjectToCache(Constants.HTTPS_VALIDATE_CERTIFICATE_CACHE_KEY, mHttpsValidateSwicthTag);
                DebugControllBoard.this.dismiss();
                if(onFinshListener != null){
                    onFinshListener.onFinsh(mApiEnvTag,mStaticH5EnvTag,mHttpsValidateSwicthTag);
                }
            }
        });
    }

    /**
     * https 证书验证开关 check box 组
     */
    private void initHttpsValidateSwitch() {
        RadioGroup httpsValidateSwitch = (RadioGroup) findViewById(R.id.tv_debug_https_validate_certificate_switch);
        httpsValidateSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                mHttpsValidateSwicthTag = rb.getTag().toString();
            }
        });

        String cacheHttpsValidateSwicth =
                CacheCenterModule.readFromCache(Constants.HTTPS_VALIDATE_CERTIFICATE_CACHE_KEY, String.class);
        if (TextUtils.isEmpty(cacheHttpsValidateSwicth)) {
            // 没有缓存变量，则默认是开启状态
            cacheHttpsValidateSwicth = "1";
        }
        if ("0".equals(cacheHttpsValidateSwicth)) {
            // 关闭
            httpsValidateSwitch.check(httpsValidateSwitch.getChildAt(0).getId());
        } else if ("1".equals(cacheHttpsValidateSwicth)) {
            // 开启
            httpsValidateSwitch.check(httpsValidateSwitch.getChildAt(1).getId());
        }
    }

    /**
     * 静态h5页面环境切换 check box 组
     */
    private void initStaticH5EnvSwitch() {
        RadioGroup staticH5EnvSwitch = (RadioGroup) findViewById(R.id.tv_debug_static_h5_env_switch);
        staticH5EnvSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                mStaticH5EnvTag = rb.getTag().toString();
            }
        });

        String cacheStaticH5Env = CacheCenterModule.readFromCache(Constants.STATIC_H5_ENV_CACHE_KEY, String.class);
        if (TextUtils.isEmpty(cacheStaticH5Env)) {
            // 没有缓存变量，则默认是线上环境
            cacheStaticH5Env = "1";
        }
        if ("0".equals(cacheStaticH5Env)) {
            // 日常
            staticH5EnvSwitch.check(staticH5EnvSwitch.getChildAt(0).getId());
        } else if ("1".equals(cacheStaticH5Env)) {
            // 线上
            staticH5EnvSwitch.check(staticH5EnvSwitch.getChildAt(1).getId());
        }
    }

    /**
     * api 环境切换 check box 组
     */
    private void initApiEnvSwitch() {
        RadioGroup apiEnvSwitch = (RadioGroup) findViewById(R.id.tv_debug_env_switch);
        apiEnvSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                mApiEnvTag = rb.getTag().toString();
            }
        });

        String cacheApiEnv = CacheCenterModule.readFromCache(Constants.API_ENV_CACHE_KEY, String.class);
        if (TextUtils.isEmpty(cacheApiEnv)) {
            // 没有缓存变量，则默认是线上环境
            cacheApiEnv = "2";
        }
        if ("0".equals(cacheApiEnv)) {
            // 日常
            apiEnvSwitch.check(apiEnvSwitch.getChildAt(0).getId());
        } else if ("1".equals(cacheApiEnv)) {
            // 预发
            apiEnvSwitch.check(apiEnvSwitch.getChildAt(1).getId());
        } else if ("2".equals(cacheApiEnv)) {
            // 线上
            apiEnvSwitch.check(apiEnvSwitch.getChildAt(2).getId());
        }
    }
    /**
     * 切换环境监听
     */
    public interface OnFinshListener{
        void onFinsh(String apiEnvTag, String staticH5EnvTag, String httpsValidateSwicthTag);
    }
    public void setOnFinshListener(OnFinshListener onFinshListener){
        this.onFinshListener = onFinshListener;
    }
}
