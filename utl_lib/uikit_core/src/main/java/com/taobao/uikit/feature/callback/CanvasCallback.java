package com.taobao.uikit.feature.callback;

import android.graphics.Canvas;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface CanvasCallback
{
    void beforeDraw(Canvas canvas);

    void afterDraw(Canvas canvas);

    void beforeDispatchDraw(Canvas canvas);

    void afterDispatchDraw(Canvas canvas);

    void beforeOnDraw(Canvas canvas);

    void afterOnDraw(Canvas canvas);
}
