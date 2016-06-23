package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.RecyclerAdapterCallback;
import com.taobao.uikit.feature.features.cellanimator.RecyclerCellAnimatorAdapter;
import com.taobao.uikit.feature.features.cellanimator.RecyclerCellAnimatorController;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tongcao on 14/12/8.
 */
public class RecyclerCellAnimatorFeature extends AbsFeature<RecyclerView> implements RecyclerAdapterCallback
{

    /**
     * The delay in millis before the first animator starts.
     */
    private int mInitialDelayMillis = 100;

    /**
     * The delay in millis between view animators.
     */
    private int mAnimatorDelayMillis = 100;

    /**
     * The duration in millis of the animators.
     */
    private int mAnimatorDurationMillis = 400;

    private boolean mIsAnimatorEnable = true;

    private AnimatorAdapter mAnimatorAdapter;

    public interface CustomAnimatorFactory
    {

        Animator[] generateAnimators(ViewGroup parent, View view);
    }

    private CustomAnimatorFactory mCustomAnimatorFactory;

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CellAnimatorFeature, defStyle, 0);
            if (null != a)
            {
                mInitialDelayMillis = a.getInt(R.styleable.CellAnimatorFeature_uik_initialDelay, mInitialDelayMillis);
                mAnimatorDelayMillis = a.getInt(R.styleable.CellAnimatorFeature_uik_animatorDelay, mAnimatorDelayMillis);
                mAnimatorDurationMillis = a.getInt(R.styleable.CellAnimatorFeature_uik_animatorDuration, mAnimatorDurationMillis);
                a.recycle();
            }
        }
    }


    @Override public void beforeSetAdapter(Adapter adapter)
    {
    }

    @Override public void afterSetAdapter(Adapter adapter)
    {
        if ((adapter != null) && !(adapter instanceof AnimatorAdapter))
        {
            RecyclerCellAnimatorController animator = new RecyclerCellAnimatorController(getHost());
            animator.setInitialDelayMillis(mInitialDelayMillis);
            animator.setAnimationDelayMillis(mAnimatorDelayMillis);
            animator.setAnimationDurationMillis(mAnimatorDurationMillis);

            mAnimatorAdapter = new AnimatorAdapter(adapter);
            mAnimatorAdapter.setRecyclerCellAnimatorController(animator);
            mAnimatorAdapter.setAnimatorEnable(mIsAnimatorEnable);
            getHost().setAdapter(mAnimatorAdapter);
        }
    }

    /**
     * Call this method to reset animation status on all views. The next time {@link # android.widget.BaseAdapter notifyDataSetChanged()} is called on the base adapter, all views will animate again.
     */
    public void resetAnimators()
    {
        if (null != getHost() && null != mAnimatorAdapter)
        {
            mAnimatorAdapter.reset();
        }
    }

    public void setAnimatorEnable(boolean enable)
    {
        mIsAnimatorEnable = enable;
        if (null != getHost() && null != mAnimatorAdapter)
        {
            mAnimatorAdapter.setAnimatorEnable(enable);
        }
    }

    public boolean isAnimatorEnable()
    {
        return mIsAnimatorEnable;
    }

    public void setCustomAnimatorFactory(CustomAnimatorFactory animatorFactory)
    {
        mCustomAnimatorFactory = animatorFactory;
    }

    public int getInitialDelayMillis()
    {
        return mInitialDelayMillis;
    }

    public void setInitialDelayMillis(int initialDelayMillis)
    {
        mInitialDelayMillis = initialDelayMillis;
    }

    public int getAnimatorDelayMillis()
    {
        return mAnimatorDelayMillis;
    }

    public void setAnimatorDelayMillis(int animatorDelayMillis)
    {
        mAnimatorDelayMillis = animatorDelayMillis;
    }

    public int getAnimatorDurationMillis()
    {
        return mAnimatorDurationMillis;
    }

    public void setAnimatorDurationMillis(int animatorDurationMillis)
    {
        mAnimatorDurationMillis = animatorDurationMillis;
    }

    private class AnimatorAdapter extends RecyclerCellAnimatorAdapter
    {

        protected AnimatorAdapter(Adapter baseAdapter)
        {
            super(baseAdapter);
        }

        @Override public Animator[] getAnimators(ViewGroup parent, View view)
        {
            if (null == mCustomAnimatorFactory)
            {
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationY", 400, 0), ObjectAnimator.ofFloat(view, "rotationX", 15, 0)};
            }
            else
            {
                return mCustomAnimatorFactory.generateAnimators(parent, view);
            }
        }
    }
}
