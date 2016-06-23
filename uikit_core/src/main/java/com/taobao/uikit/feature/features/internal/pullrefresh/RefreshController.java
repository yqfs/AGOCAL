package com.taobao.uikit.feature.features.internal.pullrefresh;

import com.taobao.uikit.feature.features.DragToRefreshFeature.OnDragToRefreshListener;
import com.taobao.uikit.feature.features.PullToRefreshFeature.OnPullToRefreshListener;
import com.taobao.uikit.utils.Config;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Scroller;

import java.util.Date;

/**
 * @author guanjie.yjf 下拉刷新的控制管理
 */
public class RefreshController
{


    private final static int RATIO = 3;   //padding的距离与界面上偏移距离的比例 屏幕滑动3个单位  上面head偏移一个单位

    private static final String TAG = "DownRefreshControler";

    public final static int RELEASE_TO_REFRESH = 0;   //松手即可刷新状态

    public final static int PULL_TO_REFRESH = 1;      //提示下拉刷新状态

    public final static int REFRESHING = 2;           //正在刷新状态

    public final static int DONE = 3;                 //不可见状态

    public final static int DOWN_PULL = 4;

    public final static int UP_PULL = 5;

    private static final int INVALID_POINTER = -1;

    private float mLastMotionY;

    protected int mActivePointerId = INVALID_POINTER;


    private RefreshHeadViewManager mHeaderViewManager;

    private RefreshHeadViewManager mFooterViewManager;

    private int mState;

    private boolean mIsBack;             // 是否从松手刷新往回推且要变成下拉刷新状态

    private int mStartY;

    private boolean mIsRefreshable;

    private boolean mIsRecored;

    private OnPullToRefreshListener mPullRefreshListener;

    private OnDragToRefreshListener mDragRefreshListener;

    private OnPullDownRefreshCancle mCancle;

    private Scroller mScroller;

    private boolean mIsScrolling = false;

    private IViewEdgeJudge mEdgeJudge;

    private Context mContext;

    private int mPullDirection;

    private boolean mUpPullFinish = false;

    private boolean mDownPullFinish = false;

    private boolean mIsNeedPullUpToRefresh = true;

    private boolean mIsAutoLoading = false;

    private boolean mIsHeadViewHeightContainLogoImage = true;

    private int mDistance;

    private int mPreDistance;

    private boolean mIsMultiPointer = false;

    private int mPositionY = 0;

    private int mPrePositionY = 0;

    private int mPreActivePointerId = INVALID_POINTER;

    //记录当前下拉刷新时拖动的距离，主要是为了支持首页下拉出现彩蛋的业务
    private int mPullDownDistance = 0;

    public interface OnPullDownRefreshCancle
    {

        void onRefreshCancle();
    }

    public RefreshController(IViewEdgeJudge edgeJudger, Context context, Scroller scroller)
    {
        mEdgeJudge = edgeJudger;
        mScroller = scroller;
        mContext = context;
        mState = DONE;
        mIsRefreshable = true;
    }

    public void setRefreshCancle(OnPullDownRefreshCancle cancle)
    {
        mCancle = cancle;
    }

    public void setDownRefreshTips(String[] array)
    {
        if (mHeaderViewManager != null)
        {
            mHeaderViewManager.setTipArray(array);
        }
    }

    public void setUpRefreshTips(String[] array)
    {
        if (mFooterViewManager != null)
        {
            mFooterViewManager.setTipArray(array);
        }
    }

    public void setTimeVisible(boolean flag)
    {
        if (mFooterViewManager != null)
        {
            mFooterViewManager.setTimeVisible(flag);
        }
    }

    @SuppressWarnings("deprecation")
    public void enablePullDownRefresh(boolean enable, int drawableID, int progressViewID, View custom)
    {
        if (enable)
        {
            if (custom == null)
            {
                ImageView view = new ImageView(mContext);
                //		        view.setImageResource(R.drawable.uik_list_logo);
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                custom = view;
            }
            //			mHeaderViewManager = new RefreshHeadViewManager(mContext,mContext.getResources().getDrawable(drawableID), LayoutInflater.from(mContext).inflate(progressViewID, null),view,
            // RefreshHeadViewManager.TYPE_HEADER);
            //自定义ProgressBar，不支持接口设置个性化的ProgressBar
            mHeaderViewManager = new RefreshHeadViewManager(mContext, mContext.getResources().getDrawable(drawableID), null, custom, RefreshHeadViewManager.TYPE_HEADER);
            mHeaderViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
        }
        else
        {
            mHeaderViewManager = null;
        }
        //mEdgeJudge.setHeadView(headerViewManager.getView());
    }

    /**
     * 该方法的调用必须在{@link #enablePullDownRefresh }方法之后调用
     *
     * @param isContained 是否让把LogoImage的图片高度算在下拉的阀值内
     */
    public void isHeadViewHeightContainImage(boolean isContained)
    {
        if (mHeaderViewManager != null)
        {
            mHeaderViewManager.isHeadViewHeightContainImage(isContained);
        }
        mIsHeadViewHeightContainLogoImage = isContained;
    }

    public void setProgressBarColor(int color)
    {
        if (mHeaderViewManager != null)
        {
            mHeaderViewManager.setProgressBarColor(color);
        }
        if (mFooterViewManager != null)
        {
            mFooterViewManager.setProgressBarColor(color);
        }
    }

    public void addHeaderView()
    {
        if (mHeaderViewManager != null)
        {
            mEdgeJudge.setHeadView(mHeaderViewManager.getView());
        }
    }

    @SuppressWarnings("deprecation")
    public void enablePullUpRefresh(boolean enable, int drawableID, int progressViewID, View custom)
    {
        if (enable)
        {
            //			mFooterViewManager = new RefreshHeadViewManager(mContext,mContext.getResources().getDrawable(drawableID), LayoutInflater.from(mContext).inflate(progressViewID, null), custom,
            // RefreshHeadViewManager.TYPE_FOOTER);
            mFooterViewManager = new RefreshHeadViewManager(mContext, mContext.getResources().getDrawable(drawableID), null, custom, RefreshHeadViewManager.TYPE_FOOTER);
            mFooterViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
        }
        else
        {
            mFooterViewManager = null;
        }
        //mEdgeJudge.setFooterView(footerViewManager.getView());
    }

    public void addFooterView()
    {
        if (mFooterViewManager != null)
        {
            mEdgeJudge.setFooterView(mFooterViewManager.getView());
        }
    }

    public void setPullUpRefreshAuto(boolean isAuto)
    {
        //		mFooterViewManager.setViewPadding(isAuto);
        mIsNeedPullUpToRefresh = !isAuto;
        mFooterViewManager.setProgressBarInitState(isAuto);
        mFooterViewManager.changeHeaderViewByState(RefreshController.PULL_TO_REFRESH, false);
    }

    public void autoLoadingData()
    {
        if (!mIsAutoLoading && !mUpPullFinish)
        {
            mIsAutoLoading = true;
            mPullDirection = UP_PULL;
            mFooterViewManager.changeHeaderViewByState(RefreshController.REFRESHING, false);
            if (null != mPullRefreshListener)
            {
                mPullRefreshListener.onPullUpToRefresh();
            }
            if (null != mDragRefreshListener)
            {
                mDragRefreshListener.onDragNegative();
            }
            mFooterViewManager.setViewPadding(true);
        }
    }

    public boolean isScrollStop()
    {
        return mScroller.isFinished();
    }

    private int getTopRealScrollY(int distanceY)
    {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        if (mHeaderViewManager != null)
        {
            int paddingTop = mHeaderViewManager.getPaddingTop();
            int diffY;
            if (mIsHeadViewHeightContainLogoImage)
            {
                diffY = mHeaderViewManager.getHeight() + paddingTop;
            }
            else
            {
                diffY = mHeaderViewManager.getImageHeight() + paddingTop;
            }
            float percent = (float) ((((float) displayMetrics.heightPixels) / (displayMetrics.heightPixels + diffY) / 1.3));
            return (int) (percent * distanceY);
        }
        else
        {
            return distanceY;
        }
    }

    /**
     * 是否到达可松开刷新的位置
     */
    private boolean JudgeArrivedRecoredEdge(MotionEvent event)
    {
        if (mEdgeJudge != null)
        {
            if (mEdgeJudge.hasArrivedTopEdge() && !mIsRecored)
            {
                mIsRecored = true;
                mStartY = (int) event.getY();
                int index = MotionEventCompat.getActionIndex(event);
                if (mActivePointerId == INVALID_POINTER)
                {
                    mActivePointerId = MotionEventCompat.getPointerId(event, index);
                    mLastMotionY = event.getY();
                    mPreActivePointerId = mActivePointerId;
                }
                return true;
            }
            else if (mEdgeJudge.hasArrivedBottomEdge() && !mIsRecored)
            {
                mIsRecored = true;
                mStartY = (int) event.getY();
                int index = MotionEventCompat.getActionIndex(event);
                if (mActivePointerId == INVALID_POINTER)
                {
                    mActivePointerId = MotionEventCompat.getPointerId(event, index);
                    mLastMotionY = event.getY();
                    mPreActivePointerId = mActivePointerId;
                }
                return true;
            }
        }
        return false;
    }

    private void processActionMove(int distance, int tempY)
    {
        if (mState == RELEASE_TO_REFRESH)                                 //松开刷新相邻的状态只可能是下拉刷新
        {
            if (mPullDirection == DOWN_PULL && mHeaderViewManager != null)
            {
                //当超过一屏幕且head可见时 ，往上推动时候，除了附加的padding改变，
                //列表本身在向下滑动，会导致数值出错，此时通过setpadding禁止列表滑动
                mEdgeJudge.keepTop();
                //往下拉 未到达可刷新区域
                int height;
                if (mIsHeadViewHeightContainLogoImage)
                {
                    height = mHeaderViewManager.getHeight();
                }
                else
                {
                    height = mHeaderViewManager.getImageHeight();
                }
                if ((getTopRealScrollY(distance) < height)         //head 没有完全露出
                    && (tempY - mStartY) > 0)
                {
                    mState = PULL_TO_REFRESH;
                    changeHeaderViewByState();
                }
            }
            else if (mPullDirection == UP_PULL && mFooterViewManager != null)
            {
                mEdgeJudge.keepBottom();
                //往上拉 为到达可刷新区域
                if ((Math.abs(distance / RATIO) < mFooterViewManager.getHeight())         //head 没有完全露出
                    && (tempY - mStartY) < 0)
                {
                    mState = PULL_TO_REFRESH;
                    if (mIsNeedPullUpToRefresh)
                    {
                        changeFooterViewByState();
                    }
                }
            }

        }
        else if (mState == PULL_TO_REFRESH)                              //下拉刷新相邻的状态可能是松开刷新 可能是完全盖住的down状态
        {
            //	mListView.setSelection(0);
            if (mPullDirection == DOWN_PULL && mHeaderViewManager != null)
            {
                mEdgeJudge.keepTop();
                int height;
                if (mIsHeadViewHeightContainLogoImage)
                {
                    height = mHeaderViewManager.getHeight();
                }
                else
                {
                    height = mHeaderViewManager.getImageHeight();
                }
                if (getTopRealScrollY(distance) >= height)         //head 完全露出
                {
                    mState = RELEASE_TO_REFRESH;
                    mIsBack = true;
                }
                else if (tempY - mStartY <= 0)                                //head 完全盖住
                {
                    mState = DONE;
                }
                changeHeaderViewByState();
                changeHeaderProgressBarState(getTopRealScrollY(distance));
            }
            else if (mPullDirection == UP_PULL && mFooterViewManager != null)
            {
                mEdgeJudge.keepBottom();
                if (distance / RATIO <= -1 * mFooterViewManager.getHeight())         //head 完全露出
                {
                    mState = RELEASE_TO_REFRESH;
                    mIsBack = true;
                    //					changeFooterViewByState();
                }
                else if (tempY - mStartY >= 0)                                //head 完全盖住
                {
                    mState = DONE;
                }
                if (mIsNeedPullUpToRefresh)
                {
                    changeFooterViewByState();
                    changeFooderProgressBarState(-distance / RATIO);
                }
            }
        }
        else if (mState == DONE)                                          //从完全盖住的done状态的相邻状态只可能是下拉刷新
        {
            if (distance > 0 && mEdgeJudge.hasArrivedTopEdge())
            {
                mPullDirection = DOWN_PULL;
                mState = PULL_TO_REFRESH;
                changeHeaderViewByState();
            }
            else if (distance < 0 && mEdgeJudge.hasArrivedBottomEdge())
            {
                mPullDirection = UP_PULL;
                mState = PULL_TO_REFRESH;
                if (mIsNeedPullUpToRefresh)
                {
                    changeFooterViewByState();
                }
            }
        }

        if (mState == PULL_TO_REFRESH || mState == RELEASE_TO_REFRESH)        // 更新headView的padding位置
        {
            if (mPullDirection == DOWN_PULL && mHeaderViewManager != null)
            {
                mHeaderViewManager.setPadding(0, -1 * mHeaderViewManager.getHeight() + getTopRealScrollY(distance), 0, 0);
            }
            else if (mPullDirection == UP_PULL && mFooterViewManager != null && !mUpPullFinish)
            {
                if (mIsNeedPullUpToRefresh)
                {
                    mFooterViewManager.setPadding(0, 0, 0, -1 * mFooterViewManager.getHeight() - distance / RATIO);
                }
            }
        }
    }

    public void onTouchEvent(MotionEvent event)
    {
        if (mIsRefreshable && !mIsScrolling)
        {
            switch (event.getAction() & MotionEventCompat.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    JudgeArrivedRecoredEdge(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    if (mState != REFRESHING)
                    {
                        if (mPullDirection == DOWN_PULL)
                        {
                            if (mState == PULL_TO_REFRESH)
                            {
                                mState = DONE;
                                changeHeaderViewByState();
                                if (mCancle != null)
                                {
                                    mCancle.onRefreshCancle();
                                }
                            }
                            if (mState == RELEASE_TO_REFRESH)
                            {
                                mState = REFRESHING;
                                changeHeaderViewByState();
                                onRefresh();
                            }
                        }
                        else if (mPullDirection == UP_PULL)
                        {
                            if (mState == PULL_TO_REFRESH)
                            {
                                mState = DONE;
                                if (mIsNeedPullUpToRefresh)
                                {
                                    changeFooterViewByState();
                                }

                            }
                            if (mState == RELEASE_TO_REFRESH)
                            {
                                mState = REFRESHING;
                                if (mIsNeedPullUpToRefresh)
                                {
                                    changeFooterViewByState();
                                }
                                onRefresh();
                            }
                        }
                    }
                    mIsRecored = false;
                    mIsBack = false;
                    mIsMultiPointer = false;
                    mDistance = 0;
                    mPositionY = 0;
                    mActivePointerId = INVALID_POINTER;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    int indexx = MotionEventCompat.getActionIndex(event);
                    //				mDistance = mDistance + (int) (MotionEventCompat.getY(event, indexx) - mLastMotionY);
                    //				Log.i(TAG, "ACTION_POINTER_DOWN ：" + "mDistance : " + mDistance);
                    Log.i(TAG, "ACTION_POINTER_DOWN : mActivePointerId " + mActivePointerId + "  position : " + mLastMotionY);
                    mLastMotionY = MotionEventCompat.getY(event, indexx);
                    mActivePointerId = MotionEventCompat.getPointerId(event, indexx);
                    mIsMultiPointer = true;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    ///**
                    int pointerIndex = MotionEventCompat.getActionIndex(event);
                    int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                    if (pointerId == mActivePointerId)
                    {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastMotionY = MotionEventCompat.getY(event, newPointerIndex);
                        mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                    }
                    pointerIndex = getPointerIndex(event, mActivePointerId);
                    if (mActivePointerId == INVALID_POINTER)
                    {
                        break;
                    }
                    mLastMotionY = MotionEventCompat.getY(event, pointerIndex);
                    Log.i(TAG, "ACTION_POINTER_UP : mActivePointerId " + mActivePointerId + " mLastMotionY position : " + mLastMotionY);
                    //**/
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mActivePointerId == INVALID_POINTER)
                    {
                        int index = MotionEventCompat.getActionIndex(event);
                        mActivePointerId = MotionEventCompat.getPointerId(event, index);
                        mLastMotionY = event.getY();
                        mPreActivePointerId = mActivePointerId;
                    }
                    int activePointerIndex = 0;
                    int tempY = 0;
                    try
                    {
                        activePointerIndex = getPointerIndex(event, mActivePointerId);
                        tempY = (int) MotionEventCompat.getY(event, activePointerIndex);
                    }
                    catch (IllegalArgumentException e)
                    {
                        // TODO: handle exception
                        e.printStackTrace();
                        break;
                    }
                    //				int tempY = (int) event.getY();
                    JudgeArrivedRecoredEdge(event);
                    //				mDistance = tempY - mStartY;
                    int distance;

                    if (mIsMultiPointer)
                    { //如果已经发生了多点触控
                        if (mPreActivePointerId == mActivePointerId)
                        {
                            distance = (int) (mDistance + (tempY - mLastMotionY));
                            tempY = (int) (mPositionY + (tempY - mLastMotionY));
                            mPreDistance = distance;
                            mPrePositionY = tempY;
                        }
                        else
                        {
                            distance = (int) (mPreDistance + (tempY - mLastMotionY));
                            tempY = (int) (mPrePositionY + (tempY - mLastMotionY));
                            mPreActivePointerId = mActivePointerId;
                            mDistance = mPreDistance;
                            mPositionY = mPrePositionY;
                        }
                    }
                    else
                    {
                        mPreDistance = mDistance = distance = tempY - mStartY;
                        mPrePositionY = mPositionY = tempY;
                    }
                    //				int distance = (int) (tempY - mLastMotionY);
                    if (Config.Debug)
                    {
                        Log.d(TAG, mDistance + "");
                    }
                    //                Log.i(TAG, "distance : " + distance + " tempY : " + tempY + "  mPreDistance : " + mPreDistance + "  mPrePositionY : " + mPrePositionY);
                    if (mState != REFRESHING && mIsRecored)
                    {
                        //如果在下拉刷新状态
                        //processActionMove(mDistance,tempY);
                        processActionMove(distance, tempY);
                        mPullDownDistance = distance;
                    }

                    break;
            }
        }
    }

    public int getPullDownDistance()
    {
        return mPullDownDistance;
    }

    private int getPointerIndex(MotionEvent ev, int id)
    {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1)
        {
            mActivePointerId = INVALID_POINTER;
        }
        return activePointerIndex;
    }

    private void changeHeaderProgressBarState(int distance)
    {
        if (mHeaderViewManager != null)
        {
            mHeaderViewManager.changeProgressBarState(distance);
        }
    }

    private void changeFooderProgressBarState(int distance)
    {
        if (mFooterViewManager != null)
        {
            mFooterViewManager.changeProgressBarState(distance);
        }
    }

    public void onScrollerStateChanged(int y, boolean isScrolling)
    {
        if (mPullDirection == DOWN_PULL)
        {
            if (mIsScrolling)
            {
                if (isScrolling && mHeaderViewManager != null)
                {
                    mHeaderViewManager.setPadding(0, y, 0, 0);
                }
                else
                {
                    mIsScrolling = false;
                }
            }
            else
            {
                if (mState == REFRESHING)
                {
                    //onRefresh();
                }
            }
        }
        else if (mPullDirection == UP_PULL)
        {
            if (mIsScrolling)
            {
                if (isScrolling && mFooterViewManager != null)
                {
                    mFooterViewManager.setPadding(0, 0, 0, y);
                }
                else
                {
                    mIsScrolling = false;
                }
            }
            else
            {
                if (mState == REFRESHING)
                {
                    //onRefresh();
                }
            }
        }
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState()
    {
        if (mHeaderViewManager == null)
        {
            return;
        }

        mHeaderViewManager.changeHeaderViewByState(mState, mIsBack);
        if (mState == PULL_TO_REFRESH && mIsBack)
        {
            mIsBack = false;
        }
        else if (mState == RefreshController.REFRESHING)
        {
            if (Config.Debug)
            {
                Log.v(TAG, "刷新造成scroll");
            }
            resetHeadViewPadding(mState);
        }
        else if (mState == RefreshController.DONE)
        {
            if (Config.Debug)
            {
                Log.v(TAG, "不需要刷新或者刷新完成造成scroll");
            }
            resetHeadViewPadding(mState);
        }

    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeFooterViewByState()
    {
        if (mFooterViewManager == null)
        {
            return;
        }
        mFooterViewManager.changeHeaderViewByState(mState, mIsBack);
        if (mState == PULL_TO_REFRESH && mIsBack)
        {
            mIsBack = false;
        }
        else if (mState == RefreshController.REFRESHING)
        {
            if (Config.Debug)
            {
                Log.v(TAG, "刷新造成scroll");
            }
            resetFooterViewPadding(mState);
        }
        else if (mState == RefreshController.DONE)
        {
            if (Config.Debug)
            {
                Log.v(TAG, "不需要刷新或者刷新完成造成scroll");
            }
            resetFooterViewPadding(mState);
        }

    }

    /**
     * 据观察 多数下拉回弹的效果仅在下拉回弹到刷新或者下拉不成功的状况下  对于刷新成功都是直接隐藏的 感觉比较生硬 不如都差用滑动的效果
     */
    public void resetFooterViewPadding(int state)
    {
        if (mFooterViewManager == null)
        {
            return;
        }
        int height = mFooterViewManager.getHeight();
        if (height == 0)
        {
            return;
        }

        int finalPaddingBottom = 0;
        if (state == RefreshController.REFRESHING)
        {
            finalPaddingBottom = 0;
        }
        else if (state == RefreshController.DONE)
        {
            finalPaddingBottom = -height;
        }
        mIsScrolling = true;
        mScroller.startScroll(0, mFooterViewManager.getPaddingBottom(), 0, finalPaddingBottom - mFooterViewManager.getPaddingBottom(), 350);
    }

    public void setDownRefreshFinish(boolean downRefreshLoadFinish)
    {
        if (mHeaderViewManager != null)
        {
            mDownPullFinish = downRefreshLoadFinish;
            mHeaderViewManager.setFinish(downRefreshLoadFinish);
        }

    }

    public void setUpRefreshFinish(boolean downRefreshLoadFinish)
    {
        if (mFooterViewManager != null)
        {
            mUpPullFinish = downRefreshLoadFinish;
            mFooterViewManager.setFinish(downRefreshLoadFinish);
        }

    }

    private void resetHeadViewPadding(int state)
    {
        if (mHeaderViewManager == null)
        {
            return;
        }
        int height = mHeaderViewManager.getHeight();

        if (height == 0)
        {
            return;
        }

        int finalPaddingTop = 0;
        if (state == RefreshController.REFRESHING)
        {
            finalPaddingTop = 0;
        }
        else if (state == RefreshController.DONE)
        {
            finalPaddingTop = -height;
        }
        mIsScrolling = true;
        if (mIsHeadViewHeightContainLogoImage)
        {
            mScroller.startScroll(0, mHeaderViewManager.getPaddingTop(), 0, finalPaddingTop - mHeaderViewManager.getPaddingTop(), 350);
        }
        else
        {
            if (state == REFRESHING)
            {
                Log.i(TAG, "debug result : " + (mHeaderViewManager.getPaddingTop() + mHeaderViewManager.getHeight() - mHeaderViewManager.getImageHeight()));
                //会弹到仅仅显示ProgressBar的位置
                mScroller.startScroll(0, mHeaderViewManager.getPaddingTop(), 0, -1 * (mHeaderViewManager.getPaddingTop() + mHeaderViewManager.getHeight() - mHeaderViewManager.getImageHeight()), 350);
            }
            else if (state == DONE)
            {
                mScroller.startScroll(0, mHeaderViewManager.getPaddingTop(), 0, finalPaddingTop - mHeaderViewManager.getPaddingTop(), 350);
            }
        }
        //	mListView.invalidate();
    }

    public void setOnRefreshListener(OnPullToRefreshListener refreshListener)
    {
        this.mPullRefreshListener = refreshListener;
        mIsRefreshable = true;
    }

    public void setOnRefreshListener(OnDragToRefreshListener refreshListener)
    {
        this.mDragRefreshListener = refreshListener;
        mIsRefreshable = true;
    }

    private void onRefresh()
    {
        if (mPullDirection == DOWN_PULL)
        {
            if (this.mDownPullFinish)
            {
                this.onRefreshComplete();
            }
            else
            {
                if (mPullRefreshListener != null)
                {
                    mPullRefreshListener.onPullDownToRefresh();
                }
                if (mDragRefreshListener != null)
                {
                    mDragRefreshListener.onDragPositive();
                }
            }
        }
        else if (mPullDirection == UP_PULL)
        {
            if (this.mUpPullFinish)
            {
                this.onRefreshComplete();
            }
            else
            {
                if (mIsNeedPullUpToRefresh)
                {
                    if (mPullRefreshListener != null)
                    {
                        mPullRefreshListener.onPullUpToRefresh();
                    }
                    if (mDragRefreshListener != null)
                    {
                        mDragRefreshListener.onDragNegative();
                    }
                }
            }
        }
    }

    public void onRefreshComplete()
    {
        mState = DONE;
        if (mPullDirection == DOWN_PULL)
        {
            if (mHeaderViewManager != null)
            {
                mHeaderViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
                changeHeaderViewByState();
            }
        }
        else if (mPullDirection == UP_PULL)
        {
            if (mFooterViewManager != null)
            {
                mFooterViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
                if (mIsNeedPullUpToRefresh)
                {
                    changeFooterViewByState();
                }
                else if (!mUpPullFinish)
                {
                    mFooterViewManager.changeHeaderViewByState(RefreshController.DONE, true);
                    mIsAutoLoading = false;
                    //					mScroller.startScroll(0, mFooterViewManager.getPaddingBottom(), 0,finalPaddingBottom-mFooterViewManager.getPaddingBottom() ,350);
                    resetFooterViewPadding(RefreshController.DONE);
                    Log.i("liuzhiwei", "onRefreshComplete");
                }
            }
        }
    }

    public void setIsDownRefreshing()
    {
        if (mHeaderViewManager != null)
        {
            mState = RefreshController.REFRESHING;
            this.changeHeaderViewByState();
            mHeaderViewManager.setPadding(0, 0, 0, 0);
            mPullDirection = DOWN_PULL;
        }
    }

    public void setIsUpRefreshing()
    {
        if (mFooterViewManager != null)
        {
            mPullDirection = UP_PULL;
            mState = RefreshController.REFRESHING;
            this.changeHeaderViewByState();
            mFooterViewManager.setPadding(0, 0, 0, 0);
        }
    }

    public int getPullDirection()
    {
        return this.mPullDirection;
    }

    public void destroy()
    {
        mPullRefreshListener = null;
        mDragRefreshListener = null;
    }
}
