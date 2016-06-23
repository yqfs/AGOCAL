package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;

/**
 * Created by yanpei on 4-16-14.
 */
public class ClickViewMaskFeature extends AbsFeature<View> implements CanvasCallback, TouchEventCallback
{

    private boolean mClickMaskEnable;

    private boolean mIsPressed;

    private int mClickMaskColor;

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        mClickMaskEnable = true;
        mIsPressed = false;
        mClickMaskColor = 0x77000000;
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickViewMaskFeature);
            if (null != a)
            {
                mClickMaskColor = a.getColor(R.styleable.ClickViewMaskFeature_uik_clickMaskColor, mClickMaskColor);
                mClickMaskEnable = a.getBoolean(R.styleable.ClickViewMaskFeature_uik_clickMaskEnable, true);
                a.recycle();
            }
        }
    }

    @Override
    public void beforeDraw(Canvas canvas)
    {
    }

    @Override
    public void afterDraw(Canvas canvas)
    {
        if (mClickMaskEnable && mIsPressed)
        {
            canvas.drawColor(mClickMaskColor);
        }
    }

    @Override
    public void beforeDispatchDraw(Canvas canvas)
    {
    }

    @Override
    public void afterDispatchDraw(Canvas canvas)
    {
    }

    @Override
    public void beforeOnDraw(Canvas canvas)
    {
    }

    @Override
    public void afterOnDraw(Canvas canvas)
    {
    }

    @Override public void beforeOnTouchEvent(MotionEvent event)
    {
        if (mClickMaskEnable)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mIsPressed = true;
                    requestLayoutHost();
                    invalidateHost();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mIsPressed = false;
                    requestLayoutHost();
                    invalidateHost();
                    break;
            }
        }
    }

    @Override public void afterOnTouchEvent(MotionEvent event)
    {
    }

    @Override public void beforeDispatchTouchEvent(MotionEvent event)
    {
    }

    @Override public void afterDispatchTouchEvent(MotionEvent event)
    {
    }

    /**
     * Sets the mask color for this feature when it is clicked.
     */
    public void setClickMaskColor(int color)
    {
        mClickMaskColor = color;
        invalidateHost();
    }

    /**
     * Set the enabled state of this feature.
     */
    public void setClickMaskEnable(boolean enable)
    {
        mClickMaskEnable = enable;
        invalidateHost();
    }

    private void invalidateHost()
    {
        if (null != mHost)
        {
            mHost.invalidate();
        }
    }

    private void requestLayoutHost()
    {
        if (null != mHost)
        {
            mHost.requestLayout();
        }
    }
}
