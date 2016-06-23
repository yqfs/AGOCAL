package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by xiekang.xiekang on 2014/5/22.
 */
public class BounceScrollFeature extends AbsFeature<ScrollView> implements ScrollCallback, MeasureCallback, TouchEventCallback
{

    public static final String BOUNCE_TAG = "bounce";

    private float mMaxRatio = 1.0f;

    private Scroller mScroller;

    private float mLastY;

    private View mBounceView;

    private boolean mIsFirstMove = true;

    @Override public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BounceScrollFeature, defStyle, 0);
            if (null != a)
            {
                mMaxRatio = a.getFloat(R.styleable.BounceScrollFeature_uik_maxRatio, mMaxRatio);
                a.recycle();
            }
        }
    }

    @Override public void beforeComputeScroll()
    {

    }

    @Override public void afterComputeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            int y = mScroller.getCurrY();
            ViewGroup.LayoutParams layoutParams = mBounceView.getLayoutParams();
            layoutParams.height = y;
            mBounceView.setLayoutParams(layoutParams);
            if (mOnBounceHeightChangeListener != null)
            	mOnBounceHeightChangeListener.onHeightChanged(y);

            getHost().invalidate();
        }
    }

    @Override public void beforeOnScrollChanged(int l, int t, int oldl, int oldt)
    {
    }

    @Override public void afterOnScrollChanged(int l, int t, int oldl, int oldt)
    {
    }

    @Override public void setHost(ScrollView host)
    {
        super.setHost(host);
        mScroller = new Scroller(host.getContext());
        getHost().setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener()
        {
            @Override public void onChildViewAdded(View parent, View child)
            {
                findBounceView(child);
            }

            @Override public void onChildViewRemoved(View parent, View child)
            {
                findBounceView(child);
            }
        });
    }

    private void findBounceView(View v)
    {
        if (v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++)
            {
                String tag = (String) vg.getChildAt(i).getContentDescription();
                if (tag != null && tag.contains(BOUNCE_TAG))
                {
                    mBounceView = vg.getChildAt(i);
                    break;
                }
                else if (vg.getChildAt(i) instanceof ViewGroup)
                {
                    findBounceView(vg.getChildAt(i));
                }
            }
        }
        else
        {
            String tag = (String) v.getContentDescription();
            if (tag != null && tag.contains(BOUNCE_TAG))
            {
                mBounceView = v;
            }
        }
    }

    public void setMaxRatio(float ratio)
    {
        mMaxRatio = ratio;
        getHost().requestLayout();
    }

    private int mMaxHeadHeight = 0;

    private int mInitHeadHeight = 0;


    @Override public void beforeOnMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

    }

    @Override public void afterOnMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mMaxHeadHeight = (int) (getHost().getMeasuredWidth() * mMaxRatio);
        if (0 == mInitHeadHeight)
        {
            mInitHeadHeight = null == mBounceView ? 0 : mBounceView.getMeasuredHeight();
        }
    }

    @Override public void beforeOnTouchEvent(MotionEvent event)
    {
        int actMasked = event.getAction() & MotionEvent.ACTION_MASK;
        if (actMasked == MotionEvent.ACTION_UP || actMasked == MotionEvent.ACTION_CANCEL || actMasked == MotionEvent.ACTION_OUTSIDE)
        {
            if (null != mBounceView)
            {
                ViewGroup.LayoutParams layoutParams = mBounceView.getLayoutParams();
                mScroller.startScroll(0, layoutParams.height, 0, mInitHeadHeight - layoutParams.height, 300);
                getHost().computeScroll();
            }
        }

        if (null != mBounceView && !mIsFirstMove && actMasked == MotionEvent.ACTION_MOVE)
        {
            if (0 < mLastY)
            {
                final float scrollY = mLastY - event.getY();
                if (0 >= getHost().getScrollY() && 0 > scrollY)
                {
                    changeHeight(scrollY);
                }
                else if (0 <= scrollY)
                {
                    if (changeHeight(scrollY))
                    {
                        getHost().scrollBy(0, (int) -scrollY);
                    }
                }
            }
        }

        if (actMasked == MotionEvent.ACTION_MOVE && mIsFirstMove)
        {
            mIsFirstMove = false;
        }
        else if (actMasked != MotionEvent.ACTION_MOVE)
        {
            mIsFirstMove = true;
        }

        mLastY = event.getY();
    }

    private boolean changeHeight(float scrollY)
    {
        boolean adjust = false;
        ViewGroup.LayoutParams layoutParams = mBounceView.getLayoutParams();
        if ((layoutParams.height - scrollY) <= mInitHeadHeight)
        {
            layoutParams.height = mInitHeadHeight;
        }
        else if ((layoutParams.height - scrollY) >= mMaxHeadHeight)
        {
            layoutParams.height = mMaxHeadHeight;
        }
        else
        {
            layoutParams.height -= scrollY;
            adjust = true;
            
        }
        mBounceView.setLayoutParams(layoutParams);
        if (mOnBounceHeightChangeListener != null)
        	mOnBounceHeightChangeListener.onHeightChanged(layoutParams.height);
        return adjust;
    }
    
    private OnBounceHeightChangeListener mOnBounceHeightChangeListener;
    public void setOnBounceHeightChangeListener(OnBounceHeightChangeListener listener) {
    	this.mOnBounceHeightChangeListener = listener;
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
    
    public interface OnBounceHeightChangeListener {
    	void onHeightChanged(float height);
    }
}
