package com.frame.base.utl.callback.impl;

import android.util.Log;

import com.frame.base.utl.callback.OnNetRequestListener;
import com.frame.base.utl.callback.entity.ShowApiResponse;
import com.frame.base.utl.util.other.ToastUtil;

import rx.Subscriber;

/**
 * Created by YANGQIYUN on 2016/5/4.
 */
public class ChildSubscriber<T extends ShowApiResponse> extends Subscriber<T> {

    protected OnNetRequestListener listener;

    protected ChildSubscriber(OnNetRequestListener listener){
        this.listener = listener;
    }

    @Override
    public void onCompleted() {
        if(listener != null){
            //仅成功后会回调
            listener.onFinish();
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d("yqy"," onError  " + e.toString());
        ToastUtil.showToast("网络异常");
        if(listener != null){
            listener.onFailure(e);
            listener.onFinish();
        }
    }

    @Override
    public void onNext(T o) {

        if(listener != null){
            listener.onFinish();
            if("200".equals(o.code)){
                listener.onSuccess(o.result);
            }else{
                ToastUtil.showToast(o.msg);
            }
        }
    }
}
