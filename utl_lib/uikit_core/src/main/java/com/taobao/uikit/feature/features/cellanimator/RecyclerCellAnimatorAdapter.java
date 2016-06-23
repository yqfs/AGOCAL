package com.taobao.uikit.feature.features.cellanimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link android.widget.BaseAdapter} class which applies multiple {@link android.animation.Animator}s at once to views when they are first shown. The Animators applied include the animations
 * specified in {@link #getAnimators(android.view.ViewGroup, android.view.View)}, plus an alpha transition.
 */
public abstract class RecyclerCellAnimatorAdapter extends Adapter
{

    /**
     * Saved instance state key for the ViewAnimator
     */
    private static final String SAVEDINSTANCESTATE_VIEWANIMATOR = "savedinstancestate_viewanimator";

    /**
     * Alpha property
     */
    private static final String ALPHA = "alpha";

    /**
     * The ViewAnimator responsible for animating the Views.
     */
    private RecyclerCellAnimatorController mCellAnimatorController;

    /**
     * Whether this instance is the root AnimationAdapter. When this is set to false, animation is not applied to the views, since the wrapper AnimationAdapter will take care of that.
     */
    private boolean mIsRootAdapter;

    private Adapter mDelegateAdapter;

    /**
     * Creates a new AnimationAdapter, wrapping given BaseAdapter.
     *
     * @param baseAdapter the BaseAdapter to wrap.
     */
    protected RecyclerCellAnimatorAdapter(final Adapter baseAdapter)
    {
        mDelegateAdapter = baseAdapter;
        mIsRootAdapter = true;

        if (baseAdapter instanceof RecyclerCellAnimatorAdapter)
        {
            ((RecyclerCellAnimatorAdapter) baseAdapter).mIsRootAdapter = false;
        }
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return mDelegateAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        mDelegateAdapter.onBindViewHolder(holder, position);
        if (mIsRootAdapter)
        {
            animateViewIfNecessary(position, holder.itemView, null);
        }
    }

    @Override public int getItemViewType(int position)
    {
        return mDelegateAdapter.getItemViewType(position);
    }

    @Override public void setHasStableIds(boolean hasStableIds)
    {
        mDelegateAdapter.setHasStableIds(hasStableIds);
    }

    @Override public long getItemId(int position)
    {
        return mDelegateAdapter.getItemId(position);
    }

    @Override public int getItemCount()
    {
        return mDelegateAdapter.getItemCount();
    }

    @Override public void onViewRecycled(RecyclerView.ViewHolder holder)
    {
        mDelegateAdapter.onViewRecycled(holder);
    }

    @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mDelegateAdapter.onViewAttachedToWindow(holder);
    }

    @Override public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder)
    {
        mDelegateAdapter.onViewDetachedFromWindow(holder);
    }

    @Override public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer)
    {
        mDelegateAdapter.registerAdapterDataObserver(observer);
    }

    @Override public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer)
    {
        mDelegateAdapter.unregisterAdapterDataObserver(observer);
    }

    /**
     * Call this method to reset animation status on all views. The next time {@link #notifyDataSetChanged()} is called on the base adapter, all views will animate again.
     */
    public void reset()
    {
        assert mCellAnimatorController != null;
        mCellAnimatorController.reset();

        if (mDelegateAdapter instanceof RecyclerCellAnimatorAdapter)
        {
            ((RecyclerCellAnimatorAdapter) mDelegateAdapter).reset();
        }
    }

    public void setAnimatorEnable(boolean enable)
    {
        if (null != mCellAnimatorController)
        {
            mCellAnimatorController.setAnimatorEnable(enable);
        }
    }

    /**
     * Returns the {@link com.taobao.uikit.feature.features.cellanimator.CellAnimatorController} responsible for animating the Views in this adapter.
     */

    public RecyclerCellAnimatorController getRecyclerCellAnimatorController()
    {
        return mCellAnimatorController;
    }

    public void setRecyclerCellAnimatorController(RecyclerCellAnimatorController cellAnimatorController)
    {
        mCellAnimatorController = cellAnimatorController;
    }

    /**
     * Animates given View if necessary.
     *
     * @param position the position of the item the View represents.
     * @param view     the View that should be animated.
     * @param parent   the parent the View is hosted in.
     */
    private void animateViewIfNecessary(final int position, final View view, final ViewGroup parent)
    {
        assert mCellAnimatorController != null;

        Animator[] childAnimators;
        if (mDelegateAdapter instanceof RecyclerCellAnimatorAdapter)
        {
            childAnimators = ((RecyclerCellAnimatorAdapter) mDelegateAdapter).getAnimators(parent, view);
        }
        else
        {
            childAnimators = null;
        }
        Animator[] animators = getAnimators(parent, view);
        if (null != animators || null != childAnimators)
        {
            Animator alphaAnimator = ObjectAnimator.ofFloat(view, ALPHA, 0, 1);
            Animator[] concatAnimators = concatAnimators(childAnimators, animators, alphaAnimator);
            mCellAnimatorController.animateViewIfNecessary(position, view, concatAnimators);
        }
    }

    /**
     * Merges given Animators into one array.
     */

    public static Animator[] concatAnimators(final Animator[] childAnimators, final Animator[] animators, final Animator alphaAnimator)
    {
        int childAnimatorsLength = childAnimators == null ? 0 : childAnimators.length;
        int animatorsLength = null == animators ? 0 : animators.length;
        Animator[] allAnimators = new Animator[childAnimatorsLength + animatorsLength + 1];

        if (null != childAnimators && 0 < childAnimatorsLength)
        {
            System.arraycopy(childAnimators, 0, allAnimators, 0, childAnimatorsLength);
        }

        if (null != animators && 0 < animatorsLength)
        {
            System.arraycopy(animators, 0, allAnimators, childAnimatorsLength, animatorsLength);
        }

        allAnimators[allAnimators.length - 1] = alphaAnimator;
        return allAnimators;
    }

    /**
     * Returns the Animators to apply to the views. In addition to the returned Animators, an alpha transition will be applied to the view.
     *
     * @param parent The parent of the view
     * @param view   The view that will be animated, as retrieved by getView().
     */

    public abstract Animator[] getAnimators(ViewGroup parent, View view);

    /**
     * Returns a Parcelable object containing the AnimationAdapter's current dynamic state.
     */

    public Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();

        if (mCellAnimatorController != null)
        {
            bundle.putParcelable(SAVEDINSTANCESTATE_VIEWANIMATOR, mCellAnimatorController.onSaveInstanceState());
        }

        return bundle;
    }

    /**
     * Restores this AnimationAdapter's state.
     *
     * @param parcelable the Parcelable object previously returned by {@link #onSaveInstanceState()}.
     */
    public void onRestoreInstanceState(final Parcelable parcelable)
    {
        if (parcelable instanceof Bundle)
        {
            Bundle bundle = (Bundle) parcelable;
            if (mCellAnimatorController != null)
            {
                mCellAnimatorController.onRestoreInstanceState(bundle.getParcelable(SAVEDINSTANCESTATE_VIEWANIMATOR));
            }
        }
    }
}
