package com.taobao.uikit.feature.features.internal.pullrefresh;


import com.taobao.uikit.utils.Config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class RefreshHeadViewManager
{

    private static final String TAG = "RefreshHeadViewManager";

    public static final int TYPE_HEADER = RefreshHeadView.TYPE_HEADER;

    public static final int TYPE_FOOTER = RefreshHeadView.TYPE_FOOTER;

    //private LayoutInflater inflater;
    private RefreshHeadView mHeadView;

    private TextView mTipsTextview;

    //private TextView lastUpdatedTextView;
    private ImageView mArrowImageView;

    //	private View mProgressBar;
    private CustomProgressBar mProgressBar;

    private int mHeadContentWidth;

    private int mHeadContentHeight;

    private int mType;

    private RotateAnimation mAnimation;

    private RotateAnimation mReverseAnimation;

    private ScaleAnimation mScaleAnimation;

    private AlphaAnimation mAlphaAnimation;

    private AnimationSet mFadeAnimationSet;

    //private int mViewType ;
    private String[] mTipArray;

    private boolean mIsFinish = false;

    //private boolean mIsHeadViewHeightContainLogoImage = true;
    private int mLogoImageHeight = 0;

    public RefreshHeadViewManager(Context context, Drawable drawable, View progressBar, View custom, int type)
    {
        //inflater = LayoutInflater.from(context);
        mHeadView = new RefreshHeadView(context, drawable, progressBar, custom);
        //headView.setType(type);

        mArrowImageView = mHeadView.getArrow();

        this.mProgressBar = mHeadView.getProgressbar();
        mTipsTextview = mHeadView.getrefreshStateText();
        //lastUpdatedTextView = headView.getLastRefreshText();

        measureView(mHeadView);
        mHeadContentHeight = mHeadView.getMeasuredHeight();
        mHeadContentWidth = mHeadView.getMeasuredWidth();
        //		设置ProcessBar 下拉刷新时的距离
        mHeadView.setPullDownDistance(mHeadContentHeight);
        mType = type;
        if (type == TYPE_HEADER)
        {
            mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
        }
        else
        {
            mHeadView.setPadding(0, 0, 0, -1 * mHeadContentHeight);
        }

        mHeadView.invalidate();

        mAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setDuration(250);
        mAnimation.setFillAfter(true);

        //设置箭头消失动画
        mScaleAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation = new AlphaAnimation(1, 0);
        mFadeAnimationSet = new AnimationSet(true);
        mFadeAnimationSet.addAnimation(mScaleAnimation);
        mFadeAnimationSet.addAnimation(mAlphaAnimation);
        mFadeAnimationSet.setDuration(200);

        mReverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseAnimation.setInterpolator(new LinearInterpolator());
        mReverseAnimation.setDuration(200);
        mReverseAnimation.setFillAfter(true);

        if (Config.Debug)
        {
            Log.d("size", "Type = " + type + "width:" + mHeadContentWidth + " height:" + mHeadContentHeight);
        }
        //        if(type==TYPE_HEADER)
        //        	isHeadViewHeightContainImage(false);
    }

    public void isHeadViewHeightContainImage(boolean isContained)
    {
        //mIsHeadViewHeightContainLogoImage = isContained;
        if (!isContained)
        {
            //			Log.i(TAG, "debug mLogoImage :" + mHeadView.getChildAt(0).getMeasuredHeight());
            View view = mHeadView.getChildAt(1);
            if (view != null)
            {
                mLogoImageHeight = view.getMeasuredHeight();
            }
            Log.i(TAG, "debug ProgressBar Height :" + mLogoImageHeight);
            mHeadView.setPullDownDistance(mLogoImageHeight);
            Log.i(TAG, "debug mHeadContentHeight :" + mHeadContentHeight);
        }
    }


    public void setViewPadding(boolean resetPadding)
    {
        if (mType == TYPE_FOOTER && resetPadding)
        {
            mHeadView.setPadding(0, 0, 0, 0);
        }
    }

    public void setProgressBarColor(int color)
    {
        if (mHeadView != null)
        {
            mHeadView.setProgressBarColor(color);
        }
    }

    public void setProgressBarInitState(boolean isShow)
    {
        if (mHeadView != null)
        {
            mHeadView.setProgressBarInitState(isShow);
        }
    }

    public void setTimeVisible(boolean flag)
    {
        if (mHeadView != null)
        {
            mHeadView.getLastRefreshText().setVisibility(View.GONE);
        }
    }

    /**
     * 设置提示内容
     *
     * @param tips 共3句提示内容 分别是 滑动未达到刷新状态 滑动达到刷新状态 正在刷新状态 完成加载状态
     */
    public void setTipArray(String[] tips)
    {
        mTipArray = tips;
    }

    public View getView()
    {
        return mHeadView;
    }

    public void measureView(View child)
    {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0)
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }
        else
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setPadding(int top, int left, int right, int bottom)
    {
        mHeadView.setPadding(top, left, right, bottom);
    }

    public int getPaddingTop()
    {
        return mHeadView.getPaddingTop();
    }

    public int getPaddingBottom()
    {
        return mHeadView.getPaddingBottom();
    }

    public int getHeight()
    {
        return mHeadContentHeight;
    }

    public int getWidth()
    {
        return mHeadContentWidth;
    }

    public int getImageHeight()
    {
        return mLogoImageHeight;
    }

    public void setUpdatedTextView(String text)
    {
        //lastUpdatedTextView.setText(text);
    }

    public void setFinish(boolean flag)
    {
        mIsFinish = flag;
        if (mIsFinish)
        {
            mArrowImageView.setVisibility(View.VISIBLE);
            mTipsTextview.setVisibility(View.VISIBLE);
            mProgressBar.stopLoadingAnimation();
            //lastUpdatedTextView.setVisibility(View.GONE);
            mArrowImageView.clearAnimation();
            mTipsTextview.setText(mTipArray == null ? "加载完成" : mTipArray[3]);
        }
    }

    // 当状态改变时候，调用该方法，以更新界面
    public void changeHeaderViewByState(int state, boolean isBack)
    {
        if (mIsFinish)
        {
            return;
        }
        switch (state)
        {
            case RefreshController.RELEASE_TO_REFRESH:
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mTipsTextview.setVisibility(View.VISIBLE);
                //lastUpdatedTextView.setVisibility(View.VISIBLE);
                //
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mFadeAnimationSet);

                mTipsTextview.setText(mTipArray == null || mTipArray.length < 2 ? "松开刷新" : mTipArray[1]);
                if (Config.Debug)
                {
                    Log.v(TAG, "当前状态，松开刷新");
                }
                break;
            case RefreshController.PULL_TO_REFRESH:
                mTipsTextview.setVisibility(View.VISIBLE);
                //lastUpdatedTextView.setVisibility(View.VISIBLE);
                //			mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                //			if (isBack) {
                ////				mArrowImageView.clearAnimation();
                ////				mArrowImageView.startAnimation(mReverseAnimation);
                //
                //				mTipsTextview.setText(mTipArray==null ||  mTipArray.length < 1? "下拉刷新" : mTipArray[0]);
                //			} else {
                //			}
                mTipsTextview.setText(mTipArray == null || mTipArray.length < 1 ? "下拉刷新" : mTipArray[0]);
                if (Config.Debug)
                {
                    Log.v(TAG, "当前状态，下拉刷新");
                }
                break;
            case RefreshController.REFRESHING:

                //			mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.startLoadingAnimaton();
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
                mTipsTextview.setText(mTipArray == null || mTipArray.length < 3 ? "正在刷新..." : mTipArray[2]);
                //lastUpdatedTextView.setVisibility(View.VISIBLE);
                if (Config.Debug)
                {
                    Log.v(TAG, "当前状态,正在刷新...");
                }
                break;
            case RefreshController.DONE:
                //headView.setPadding(0, -1 * headContentHeight, 0, 0);
                //			mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.stopLoadingAnimation();
                //			mArrowImageView.clearAnimation();
                //arrowImageView.setImageResource(R.drawable.arrow);
                mTipsTextview.setText(mTipArray == null || mTipArray.length < 3 ? "数据加载完毕" : mTipArray[3]);
                //			mTipsTextview.setText(mTipArray == null || mTipArray.length < 1? "下拉刷新" : mTipArray[0]);
                //lastUpdatedTextView.setVisibility(View.VISIBLE);
                if (Config.Debug)
                {
                    Log.v(TAG, "当前状态，done");
                }
                break;
        }
    }

    public void changeProgressBarState(int distance)
    {
        //		distance
        mProgressBar.changeProgressBarState(distance);
    }

    //	public void startProgressBarAnimation() {
    //		if(mProgressBar != null) {
    //			mProgressBar.startLoadingAnimaton();
    //		}
    //	}
}
