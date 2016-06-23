package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ScrollCallback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by xiekang.xiekang on 2014/5/22.
 */
public class ParallaxScrollFeature extends AbsFeature<ScrollView> implements ScrollCallback
{

    static private boolean isAPI11 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    private int mParallaxNum = 1;

    private float mInnerParallaxFactor = 1.8f;

    private float mParallaxFactor = 1.8f;

    private ArrayList<ParallaxView> mParallaxViews = new ArrayList<ParallaxView>();

    @Override public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxScrollFeature, defStyle, 0);
            if (null != a)
            {
                mParallaxFactor = a.getFloat(R.styleable.ParallaxScrollFeature_uik_parallaxFactor, mParallaxFactor);
                mInnerParallaxFactor = a.getFloat(R.styleable.ParallaxScrollFeature_uik_innerParallaxFactor, mInnerParallaxFactor);
                mParallaxNum = a.getInt(R.styleable.ParallaxScrollFeature_uik_parallaxNum, mParallaxNum);
                a.recycle();
            }
        }
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
        float factor = mParallaxFactor;
        for (ParallaxView view : mParallaxViews)
        {
            view.setOffset((float) t / factor);
            factor *= mInnerParallaxFactor;
        }
    }

    @Override public void setHost(ScrollView host)
    {
        super.setHost(host);
        getHost().setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener()
        {
            @Override public void onChildViewAdded(View parent, View child)
            {
                makeViewsParallax();
            }

            @Override public void onChildViewRemoved(View parent, View child)
            {
                makeViewsParallax();
            }
        });
    }

    private void makeViewsParallax()
    {
        mParallaxViews.clear();
        if (getHost().getChildCount() > 0 && getHost().getChildAt(0) instanceof ViewGroup)
        {
            ViewGroup viewsHolder = (ViewGroup) (getHost().getChildAt(0));
            int numOfParallaxViews = Math.min(this.mParallaxNum, viewsHolder.getChildCount());
            for (int i = 0; i < numOfParallaxViews; i++)
            {
                ParallaxView parallaxedView = new ParallaxView(viewsHolder.getChildAt(i));
                mParallaxViews.add(parallaxedView);
            }
        }
    }

    public void setParallaxNum(int num)
    {
        mParallaxNum = num;
        makeViewsParallax();
    }

    public void setParallaxFactor(float factor)
    {
        mParallaxFactor = factor;
    }

    public void setInnerParallaxFactor(float innerFactor)
    {
        mInnerParallaxFactor = innerFactor;
    }

    class ParallaxView
    {

        protected WeakReference<View> view;

        protected int lastOffset;

        public ParallaxView(View view)
        {
            this.lastOffset = 0;
            this.view = new WeakReference<View>(view);
        }

        @SuppressLint("NewApi")
        public void setOffset(float offset)
        {
            View view = this.view.get();
            if (view != null)
            {
                if (isAPI11)
                {
                    view.setTranslationY(offset);
                }
                else
                {
                    translatePreICS(view, offset);
                }
            }
        }

        protected void translatePreICS(View view, float offset)
        {
            view.offsetTopAndBottom((int) offset - lastOffset);
            lastOffset = (int) offset;
        }
    }
}
