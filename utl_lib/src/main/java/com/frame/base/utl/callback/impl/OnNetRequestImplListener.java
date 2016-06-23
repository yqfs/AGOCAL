package com.frame.base.utl.callback.impl;


import android.util.Log;

import com.frame.base.utl.callback.OnNetRequestListener;
import com.frame.base.utl.util.other.ToastUtil;

/**
 * Created by YANGQIYUN on 2016/5/4.
 */
public class OnNetRequestImplListener<T> implements OnNetRequestListener<T> {
    @Override
    public void onStart() {
        Log.d("yqy","onStart");
    }

    @Override
    public void onFinish() {
        Log.d("yqy","onFinish");
    }

    @Override
    public void onSuccess(T data) {
        Log.d("yqy","onSuccess");
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("yqy","onFailure  " + t);
    }
}
