package com.taobao.uikit.feature.callback;

/**
 * Created by kangyong.lt on 14-4-29.
 */
public interface ScrollCallback
{
    void beforeComputeScroll();
    void afterComputeScroll();
    
    void beforeOnScrollChanged(int l, int t, int oldl, int oldt);
    void afterOnScrollChanged(int l, int t, int oldl, int oldt);
}
