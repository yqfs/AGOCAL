package com.taobao.uikit.feature.callback;

import android.view.KeyEvent;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface OtherCallback
{
    void beforeOnSizeChanged(int w, int h, int oldw, int oldh);

    void afterOnSizeChanged(int w, int h, int oldw, int oldh);

    void beforeOnKeyDown(int keyCode, KeyEvent event);

    void afterOnKeyDown(int keyCode, KeyEvent event);

    void beforeOnKeyUp(int keyCode, KeyEvent event);

    void afterOnKeyUp(int keyCode, KeyEvent event);

 

    void beforeOnAttachedToWindow();

    void afterOnAttachedToWindow();

    void beforeOnDetachedFromWindow();

    void afterOnDetachedFromWindow();

    void beforeOnWindowVisibilityChanged(int visibility);

    void afterOnWindowVisibilityChanged(int visibility);

}
