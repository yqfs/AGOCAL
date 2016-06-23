package com.taobao.uikit.feature.callback;

import android.view.MotionEvent;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface TouchEventCallback
{
    void beforeOnTouchEvent(MotionEvent event);

    void afterOnTouchEvent(MotionEvent event);

    void beforeDispatchTouchEvent(MotionEvent event);

    void afterDispatchTouchEvent(MotionEvent event);
    
}
