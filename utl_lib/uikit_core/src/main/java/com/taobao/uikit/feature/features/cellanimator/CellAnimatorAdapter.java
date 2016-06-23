package com.taobao.uikit.feature.features.cellanimator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * A {@link BaseAdapter} class which applies multiple {@link android.animation.Animator}s at once to views when they are first shown. The Animators applied include the animations specified
 * in {@link #getAnimators(android.view.ViewGroup, android.view.View)}, plus an alpha transition.
 */
public abstract class CellAnimatorAdapter extends BaseAdapter
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
    private CellAnimatorController mCellAnimatorController;

    /**
     * Whether this instance is the root AnimationAdapter. When this is set to false, animation is not applied to the views, since the wrapper AnimationAdapter will take care of
     * that.
     */
    private boolean mIsRootAdapter;

    private BaseAdapter mDelegateAdapter;

    /**
     * Creates a new AnimationAdapter, wrapping given BaseAdapter.
     *
     * @param baseAdapter the BaseAdapter to wrap.
     */
    protected CellAnimatorAdapter(final BaseAdapter baseAdapter) {
        mDelegateAdapter = baseAdapter;
        mIsRootAdapter = true;

        if (baseAdapter instanceof CellAnimatorAdapter) {
            ((CellAnimatorAdapter) baseAdapter).mIsRootAdapter = false;
        }
    }

    @Override
    public int getCount() {
        return mDelegateAdapter.getCount();
    }

    @Override
    public Object getItem(final int position) {
        return mDelegateAdapter.getItem(position);
    }

    @Override
    public long getItemId(final int position) {
        return mDelegateAdapter.getItemId(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mDelegateAdapter.areAllItemsEnabled();
    }

    @Override
    public int getItemViewType(final int position) {
        return mDelegateAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mDelegateAdapter.getViewTypeCount();
    }

    @Override
    public boolean hasStableIds() {
        return mDelegateAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return mDelegateAdapter.isEmpty();
    }

    @Override
    public boolean isEnabled(final int position) {
        return mDelegateAdapter.isEnabled(position);
    }

    @Override
    public final View getView(final int position,  final View convertView,  final ViewGroup parent) {
//        if (mIsRootAdapter) {
//            assert mCellAnimatorController != null;
//            if (convertView != null) {
//                mCellAnimatorController.cancelExistingAnimation(convertView);
//            }
//        }

        View itemView = mDelegateAdapter.getView(position, convertView, parent);

        if (mIsRootAdapter) {
            animateViewIfNecessary(position, itemView, parent);
        }
        return itemView;
    }

    @Override
    public void notifyDataSetChanged() {
        mDelegateAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        mDelegateAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void registerDataSetObserver( final DataSetObserver observer) {
        mDelegateAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver( final DataSetObserver observer) {
        mDelegateAdapter.unregisterDataSetObserver(observer);
    }

    /**
     * Call this method to reset animation status on all views. The next time {@link #notifyDataSetChanged()} is called on the base adapter, all views will animate again.
     */
    public void reset() {
        assert mCellAnimatorController != null;
        mCellAnimatorController.reset();

        if (mDelegateAdapter instanceof CellAnimatorAdapter) {
            ((CellAnimatorAdapter) mDelegateAdapter).reset();
        }
    }

    public void setAnimatorEnable(boolean enable) {
        if(null != mCellAnimatorController) {
            mCellAnimatorController.setAnimatorEnable(enable);
        }
    }
    /**
     * Returns the {@link CellAnimatorController} responsible for animating the Views in this adapter.
     */
    
    public CellAnimatorController getCellAnimatorController() {
        return mCellAnimatorController;
    }

    public void setCellAnimatorController(CellAnimatorController cellAnimatorController) {
        mCellAnimatorController = cellAnimatorController;
    }

    /**
     * Animates given View if necessary.
     *
     * @param position the position of the item the View represents.
     * @param view     the View that should be animated.
     * @param parent   the parent the View is hosted in.
     */
    private void animateViewIfNecessary(final int position,  final View view,  final ViewGroup parent) {
        assert mCellAnimatorController != null;

        Animator[] childAnimators;
        if (mDelegateAdapter instanceof CellAnimatorAdapter) {
            childAnimators = ((CellAnimatorAdapter) mDelegateAdapter).getAnimators(parent, view);
        } else {
            childAnimators = null;
        }
        Animator[] animators = getAnimators(parent, view);
        if(null != animators || null != childAnimators) {
            Animator alphaAnimator = ObjectAnimator.ofFloat(view, ALPHA, 0, 1);
            Animator[] concatAnimators = concatAnimators(childAnimators, animators, alphaAnimator);
            mCellAnimatorController.animateViewIfNecessary(position, view, concatAnimators);
        }
    }

    /**
     * Merges given Animators into one array.
     */

    public static Animator[] concatAnimators( final Animator[] childAnimators,  final Animator[] animators,  final Animator alphaAnimator) {
        int childAnimatorsLength = childAnimators == null ? 0 : childAnimators.length;
        int animatorsLength = null == animators ? 0 : animators.length;
        Animator[] allAnimators = new Animator[childAnimatorsLength + animatorsLength + 1];

        if (null != childAnimators && 0 < childAnimatorsLength) {
            System.arraycopy(childAnimators, 0, allAnimators, 0, childAnimatorsLength);
        }

        if(null != animators && 0 < animatorsLength) {
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
    
    public abstract Animator[] getAnimators( ViewGroup parent,  View view);

    /**
     * Returns a Parcelable object containing the AnimationAdapter's current dynamic state.
     */
    
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        if (mCellAnimatorController != null) {
            bundle.putParcelable(SAVEDINSTANCESTATE_VIEWANIMATOR, mCellAnimatorController.onSaveInstanceState());
        }

        return bundle;
    }

    /**
     * Restores this AnimationAdapter's state.
     *
     * @param parcelable the Parcelable object previously returned by {@link #onSaveInstanceState()}.
     */
    public void onRestoreInstanceState( final Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            if (mCellAnimatorController != null) {
                mCellAnimatorController.onRestoreInstanceState(bundle.getParcelable(SAVEDINSTANCESTATE_VIEWANIMATOR));
            }
        }
    }
}
