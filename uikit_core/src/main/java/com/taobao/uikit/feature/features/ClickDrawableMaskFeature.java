package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by yanpei on 4-16-14.
 */
public class ClickDrawableMaskFeature extends AbsFeature<ImageView> implements CanvasCallback, TouchEventCallback
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
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickDrawableMaskFeature);
            if (null != a)
            {
                mClickMaskColor = a.getColor(R.styleable.ClickDrawableMaskFeature_uik_clickMaskColor, mClickMaskColor);
                mClickMaskEnable = a.getBoolean(R.styleable.ClickDrawableMaskFeature_uik_clickMaskEnable, true);
                a.recycle();
            }
        }
    }

    @Override
    public void beforeDraw(Canvas canvas)
    {
        if (mClickMaskEnable)
        {
            Drawable drawable = getHost().getDrawable();
            if (drawable != null)
            {
                drawable.setCallback(null);
                if (mIsPressed)
                {
                    drawable.setColorFilter(mClickMaskColor, PorterDuff.Mode.SRC_ATOP);
                }
                else
                {
                    drawable.clearColorFilter();
                }
            }
        }
    }

    @Override
    public void afterDraw(Canvas canvas)
    {
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
