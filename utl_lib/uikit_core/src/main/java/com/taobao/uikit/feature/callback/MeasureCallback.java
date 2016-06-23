package com.taobao.uikit.feature.callback;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface MeasureCallback
{
    void beforeOnMeasure(int widthMeasureSpec, int heightMeasureSpec);

    void afterOnMeasure(int widthMeasureSpec, int heightMeasureSpec);
}
