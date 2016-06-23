package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.SetClipToPaddingCallBack;
import com.taobao.uikit.feature.callback.TouchEventCallback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by xiekang.xiekang on 2014/5/7.
 */
public class StickyScrollFeature extends AbsFeature<ScrollView> implements CanvasCallback, TouchEventCallback, LayoutCallback, ScrollCallback, SetClipToPaddingCallBack
{

    /**
     * Tag for views that should stick and have constant drawing. e.g. TextViews, ImageViews etc
     */
    public static final String STICKY_TAG = "sticky";

    /**
     * Flag for views that should stick and have non-constant drawing. e.g. Buttons, ProgressBars etc
     */
    public static final String FLAG_NONCONSTANT = "-nonconstant";

    /**
     * Flag for views that have aren't fully opaque
     */
    public static final String FLAG_HASTRANSPARANCY = "-hastransparancy";

    /**
     * Default height of the shadow peeking out below the stuck view.
     */
    private static final int DEFAULT_SHADOW_HEIGHT = 10; // dp;

    private ArrayList<View> mStickyViews;

    private View mCurrentlyStickingView;

    private float mStickyViewTopOffset;

    private int mStickyViewLeftOffset;

    private boolean mRedirectTouchesToStickyView;

    private boolean mClipToPadding;

    private boolean mClipToPaddingHasBeenSet;

    private int mShadowHeight;

    private Drawable mShadowDrawable;

    private boolean mHasNotDoneActionDown = true;

    private int mInvalidateCount = 0;

    private final Runnable mInvalidateRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (mCurrentlyStickingView != null)
            {
                int l = getLeftForViewRelativeOnlyChild(mCurrentlyStickingView);
                int t = getBottomForViewRelativeOnlyChild(mCurrentlyStickingView);
                int r = getRightForViewRelativeOnlyChild(mCurrentlyStickingView);
                int b = (int) (getHost().getScrollY() + (mCurrentlyStickingView.getHeight() + mStickyViewTopOffset));
                getHost().invalidate(l, t, r, b);
            }

            if (mInvalidateCount < 20)
            {
                mInvalidateCount++;
                getHost().postDelayed(this, 16);
            }
        }
    };

    @Override public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StickyScrollFeature, defStyle, 0);
            if (null != a)
            {
                final float density = context.getResources().getDisplayMetrics().density;
                int defaultShadowHeightInPix = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);
                mShadowHeight = a.getDimensionPixelSize(R.styleable.StickyScrollFeature_uik_shadowHeight, defaultShadowHeightInPix);
                mShadowDrawable = a.getDrawable(R.styleable.StickyScrollFeature_uik_shadowDrawable);
                a.recycle();
            }
        }
    }

    @Override public void setHost(ScrollView host)
    {
        super.setHost(host);
        setup();
        getHost().setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener()
        {
            @Override public void onChildViewAdded(View parent, View child)
            {
                findStickyViews(child);
            }

            @Override public void onChildViewRemoved(View parent, View child)
            {

            }
        });
    }

    @Override public void beforeDraw(Canvas canvas)
    {

    }

    @Override public void afterDraw(Canvas canvas)
    {

    }

    @Override public void beforeDispatchDraw(Canvas canvas)
    {

    }

    @Override public void afterDispatchDraw(Canvas canvas)
    {
        if (mCurrentlyStickingView != null)
        {
            canvas.save();
            canvas.translate(getHost().getPaddingLeft() + mStickyViewLeftOffset, getHost().getScrollY() + mStickyViewTopOffset + (mClipToPadding ? getHost().getPaddingTop() : 0));

            canvas.clipRect(0, (mClipToPadding ? -mStickyViewTopOffset : 0), getHost().getWidth() - mStickyViewLeftOffset, mCurrentlyStickingView.getHeight() + mShadowHeight + 1);

            if (mShadowDrawable != null)
            {
                int left = 0;
                int right = mCurrentlyStickingView.getWidth();
                int top = mCurrentlyStickingView.getHeight();
                int bottom = mCurrentlyStickingView.getHeight() + mShadowHeight;
                mShadowDrawable.setBounds(left, top, right, bottom);
                mShadowDrawable.draw(canvas);
            }

            canvas.clipRect(0, (mClipToPadding ? -mStickyViewTopOffset : 0), getHost().getWidth(), mCurrentlyStickingView.getHeight());
            if (((String) mCurrentlyStickingView.getContentDescription()).contains(FLAG_HASTRANSPARANCY))
            {
                showView(mCurrentlyStickingView);
                mCurrentlyStickingView.draw(canvas);
                hideView(mCurrentlyStickingView);
            }
            else
            {
                mCurrentlyStickingView.draw(canvas);
            }
            canvas.restore();
        }
    }

    @Override public void beforeOnDraw(Canvas canvas)
    {

    }

    @Override public void afterOnDraw(Canvas canvas)
    {

    }

    @Override public void beforeComputeScroll()
    {

    }

    @Override public void afterComputeScroll()
    {

    }

    @Override public void beforeOnScrollChanged(int l, int t, int oldl, int oldt)
    {

    }

    @Override public void afterOnScrollChanged(int l, int t, int oldl, int oldt)
    {
        doTheStickyThing();
    }

    @Override public void beforeSetClipToPadding(boolean clipToPadding)
    {

    }

    @Override public void afterSetClipToPadding(boolean clipToPadding)
    {
        mClipToPadding = clipToPadding;
        mClipToPaddingHasBeenSet = true;
    }

    @Override public void beforeOnTouchEvent(MotionEvent ev)
    {
        if (mRedirectTouchesToStickyView)
        {
            ev.offsetLocation(0, ((getHost().getScrollY() + mStickyViewTopOffset) - getTopForViewRelativeOnlyChild(mCurrentlyStickingView)));
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            mHasNotDoneActionDown = false;
        }

        if (mHasNotDoneActionDown)
        {
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            mHasNotDoneActionDown = false;
            return;
        }

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)
        {
            mHasNotDoneActionDown = true;
        }
    }

    @Override public void afterOnTouchEvent(MotionEvent event)
    {

    }

    @Override public void beforeDispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            mRedirectTouchesToStickyView = true;
        }

        if (mRedirectTouchesToStickyView)
        {
            mRedirectTouchesToStickyView = mCurrentlyStickingView != null;
            if (mRedirectTouchesToStickyView)
            {
                mRedirectTouchesToStickyView = ev.getY() <= (mCurrentlyStickingView.getHeight() + mStickyViewTopOffset) &&
                                               ev.getX() >= getLeftForViewRelativeOnlyChild(mCurrentlyStickingView) &&
                                               ev.getX() <= getRightForViewRelativeOnlyChild(mCurrentlyStickingView);
            }
        }
        else if (mCurrentlyStickingView == null)
        {
            mRedirectTouchesToStickyView = false;
        }

        if (mRedirectTouchesToStickyView)
        {
            ev.offsetLocation(0, -1 * ((getHost().getScrollY() + mStickyViewTopOffset) - getTopForViewRelativeOnlyChild(mCurrentlyStickingView)));
            if (((String) mCurrentlyStickingView.getContentDescription()).contains(FLAG_NONCONSTANT))
            {
                mInvalidateCount = 0;
                getHost().post(mInvalidateRunnable);
            }
        }
    }

    @Override public void afterDispatchTouchEvent(MotionEvent event)
    {
    }

    @Override public void beforeOnLayout(boolean changed, int left, int top, int right, int bottom)
    {

    }

    @Override public void afterOnLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if (!mClipToPaddingHasBeenSet)
        {
            mClipToPadding = true;
        }
        notifyHierarchyChanged();
    }

    @SuppressLint("NewApi")
    public void setup()
    {
        mStickyViews = new ArrayList<View>();
        if (11 <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT < 18)
        {
            getHost().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void findStickyViews(View v)
    {
        if (v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++)
            {
                String tag = (String) vg.getChildAt(i).getContentDescription();
                if (tag != null && tag.contains(STICKY_TAG))
                {
                    mStickyViews.add(vg.getChildAt(i));
                }
                else if (vg.getChildAt(i) instanceof ViewGroup)
                {
                    findStickyViews(vg.getChildAt(i));
                }
            }
        }
        else
        {
            String tag = (String) v.getContentDescription();
            if (tag != null && tag.contains(STICKY_TAG))
            {
                mStickyViews.add(v);
            }
        }
    }

    private void hideView(View v)
    {
        if (Build.VERSION.SDK_INT >= 11)
        {
            v.setAlpha(0);
        }
        else
        {
            AlphaAnimation anim = new AlphaAnimation(1, 0);
            anim.setDuration(0);
            anim.setFillAfter(true);
            v.startAnimation(anim);
        }
    }

    private void showView(View v)
    {
        if (Build.VERSION.SDK_INT >= 11)
        {
            v.setAlpha(1);
        }
        else
        {
            AlphaAnimation anim = new AlphaAnimation(0, 1);
            anim.setDuration(0);
            anim.setFillAfter(true);
            v.startAnimation(anim);
        }
    }

    private int getLeftForViewRelativeOnlyChild(View v)
    {
        int left = v.getLeft();
        while (v.getParent() != getHost().getChildAt(0))
        {
            v = (View) v.getParent();
            left += v.getLeft();
        }
        return left;
    }

    private int getTopForViewRelativeOnlyChild(View v)
    {
        int top = v.getTop();
        while (v.getParent() != getHost().getChildAt(0))
        {
            v = (View) v.getParent();
            top += v.getTop();
        }
        return top;
    }

    private int getRightForViewRelativeOnlyChild(View v)
    {
        int right = v.getRight();
        while (v.getParent() != getHost().getChildAt(0))
        {
            v = (View) v.getParent();
            right += v.getRight();
        }
        return right;
    }

    private int getBottomForViewRelativeOnlyChild(View v)
    {
        int bottom = v.getBottom();
        while (v.getParent() != getHost().getChildAt(0))
        {
            v = (View) v.getParent();
            bottom += v.getBottom();
        }
        return bottom;
    }

    private void doTheStickyThing()
    {
        View viewThatShouldStick = null;
        View approachingView = null;
        for (View v : mStickyViews)
        {
            int viewTop = getTopForViewRelativeOnlyChild(v) - getHost().getScrollY() + (mClipToPadding ? 0 : getHost().getPaddingTop());
            if (viewTop <= 0)
            {
                if (viewThatShouldStick == null || viewTop > (getTopForViewRelativeOnlyChild(viewThatShouldStick) - getHost().getScrollY() + (mClipToPadding ? 0 : getHost().getPaddingTop())))
                {
                    viewThatShouldStick = v;
                }
            }
            else
            {
                if (approachingView == null || viewTop < (getTopForViewRelativeOnlyChild(approachingView) - getHost().getScrollY() + (mClipToPadding ? 0 : getHost().getPaddingTop())))
                {
                    approachingView = v;
                }
            }
        }
        if (viewThatShouldStick != null)
        {
            mStickyViewTopOffset = approachingView == null ? 0 : Math.min(0, getTopForViewRelativeOnlyChild(approachingView) - getHost().getScrollY() +
                                                                             (mClipToPadding ? 0 : getHost().getPaddingTop()) - viewThatShouldStick.getHeight());
            if (viewThatShouldStick != mCurrentlyStickingView)
            {
                if (mCurrentlyStickingView != null)
                {
                    stopStickingCurrentlyStickingView();
                }
                // only compute the left offset when we start sticking.
                mStickyViewLeftOffset = getLeftForViewRelativeOnlyChild(viewThatShouldStick);
                startStickingView(viewThatShouldStick);
            }
        }
        else if (mCurrentlyStickingView != null)
        {
            stopStickingCurrentlyStickingView();
        }
    }

    private void startStickingView(View viewThatShouldStick)
    {
        mCurrentlyStickingView = viewThatShouldStick;
        if (((String) mCurrentlyStickingView.getContentDescription()).contains(FLAG_HASTRANSPARANCY))
        {
            hideView(mCurrentlyStickingView);
        }
    }

    private void stopStickingCurrentlyStickingView()
    {
        if (((String) mCurrentlyStickingView.getContentDescription()).contains(FLAG_HASTRANSPARANCY))
        {
            showView(mCurrentlyStickingView);
        }
        mCurrentlyStickingView = null;
    }

    private void notifyHierarchyChanged()
    {
        if (mCurrentlyStickingView != null)
        {
            stopStickingCurrentlyStickingView();
        }
        mStickyViews.clear();
        findStickyViews(getHost().getChildAt(0));
        doTheStickyThing();
        getHost().invalidate();
    }

    /**
     * Sets the height of the shadow drawable in pixels.
     */
    public void setShadowHeight(int height)
    {
        mShadowHeight = height;
    }
}
