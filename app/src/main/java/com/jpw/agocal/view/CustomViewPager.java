package com.jpw.agocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jpw.agocal.R;

/**
 * 自定义可以选择滑动的viewpager
 * Created by YANGQIYUN on 2016/6/20.
 */
public class CustomViewPager extends ViewPager{

    private boolean enabled;

    public CustomViewPager(Context context) {
        super(context, null);
        init();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomViewPager, 0, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomViewPager_viewpager_enabled:
                    this.enabled = a.getBoolean(attr,true);
                    break;
            }
        }
        a.recycle();
        init();
    }

    private void init(){
        this.enabled = false;
    }
    //触摸没有反应就可以了
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
