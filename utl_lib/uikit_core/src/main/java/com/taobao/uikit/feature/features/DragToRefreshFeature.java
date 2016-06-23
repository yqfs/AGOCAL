package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.features.internal.pullrefresh.IViewEdgeJudge;
import com.taobao.uikit.feature.features.internal.pullrefresh.RefreshController;
import com.taobao.uikit.feature.view.TRecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by yanpei on 1-14-2015.
 */
public class DragToRefreshFeature extends AbsFeature<RecyclerView> implements TouchEventCallback, IViewEdgeJudge, ScrollCallback
{

    public interface OnDragToRefreshListener
    {

        void onDragPositive();

        void onDragNegative();

    }

    private RefreshController mRefreshController;

    private Scroller mScroller;

    private RecyclerView.OnScrollListener mAutoLoadScrollListener;

    private boolean mIsAuto = false;

    private boolean mEnablePositive;

    private boolean mEnableNegative;

    public DragToRefreshFeature(Context context, int orientation)
    {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mRefreshController = new RefreshController(this, context, mScroller);
    }

    public void setIsPositiveRefreshing()
    {
        if (mRefreshController != null)
        {
            mRefreshController.setIsDownRefreshing();
        }
    }

    public void setIsNegativeRefreshing()
    {
        if (mRefreshController != null)
        {
            mRefreshController.setIsUpRefreshing();
        }
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
    }

    @Override
    public void setHost(final RecyclerView host)
    {
        super.setHost(host);
        mRefreshController.addFooterView();
        mRefreshController.addHeaderView();
        if (mIsAuto)
        {
            addAutoLoadScrollListener(host);
        }
    }

    private void addAutoLoadScrollListener(RecyclerView host)
    {
        if (null == mAutoLoadScrollListener)
        {
            mAutoLoadScrollListener = new RecyclerView.OnScrollListener()
            {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
                    if (hasArrivedBottomEdgeOffset(1) && mRefreshController.isScrollStop())
                    {
                        mRefreshController.autoLoadingData();
                    }
                }
            };
            host.setOnScrollListener(mAutoLoadScrollListener);
        }
    }

    @Override
    public void beforeOnTouchEvent(MotionEvent event)
    {
        if (mRefreshController != null)
        {
            mRefreshController.onTouchEvent(event);
        }
    }

    @Override
    public void afterOnTouchEvent(MotionEvent event)
    {
    }

    @Override
    public void beforeDispatchTouchEvent(MotionEvent event)
    {
    }

    @Override
    public void afterDispatchTouchEvent(MotionEvent event)
    {
    }

    @Override
    public boolean hasArrivedTopEdge()
    {
        RecyclerView.LayoutManager lm = mHost.getLayoutManager();
        int adj_p = mEnablePositive ? 1 : 0;
        int adj_n = mEnableNegative ? 1 : 0;
        if (lm instanceof LinearLayoutManager)
        {
            int first = ((LinearLayoutManager) lm).findFirstCompletelyVisibleItemPosition();
            if (RecyclerView.NO_POSITION != first)
            {
                return 0 >= first - adj_p;
            }
            else
            {
                return 0 == ((TRecyclerView) mHost).getTotalCount() - adj_p - adj_n;
            }
        }
        else if (lm instanceof StaggeredGridLayoutManager)
        {
            int[] firsts = ((StaggeredGridLayoutManager) lm).findFirstCompletelyVisibleItemPositions(null);
            for (int first : firsts)
            {
                if (RecyclerView.NO_POSITION != first)
                {
                    if (0 >= first - adj_p)
                    {
                        return true;
                    }
                }
                else
                {
                    if (0 == ((TRecyclerView) mHost).getTotalCount() - adj_p - adj_n)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasArrivedBottomEdge()
    {
        return hasArrivedBottomEdgeOffset(0);
    }

    private boolean hasArrivedBottomEdgeOffset(int offset)
    {
        boolean ret = false;
        RecyclerView.LayoutManager lm = mHost.getLayoutManager();
        int adj = mEnableNegative ? 1 : 0;
        if (lm instanceof LinearLayoutManager)
        {
            ret = (((TRecyclerView) mHost).getTotalCount() - 1 - adj) <= ((LinearLayoutManager) lm).findLastCompletelyVisibleItemPosition() + offset;
        }
        else if (lm instanceof StaggeredGridLayoutManager)
        {
            int[] lasts = ((StaggeredGridLayoutManager) lm).findLastCompletelyVisibleItemPositions(null);
            for (int last : lasts)
            {
                if (RecyclerView.NO_POSITION != last && (((TRecyclerView) mHost).getItemCount() - 1 - adj) <= last + offset)
                {
                    ret = true;
                }
            }
        }

        return ret && !hasArrivedTopEdge();
    }

    @Override
    public void setHeadView(View view)
    {
        ((TRecyclerView) mHost).addHeaderView(view);
    }

    @Override
    public void setFooterView(View view)
    {
        ((TRecyclerView) mHost).addFooterView(view);
    }

    @Override
    public void keepTop()
    {
        mHost.scrollToPosition(0);
    }

    @Override
    public void keepBottom()
    {
        RecyclerView.Adapter adapter = mHost.getAdapter();
        if (null != adapter)
        {
            mHost.scrollToPosition(adapter.getItemCount());
        }
        else
        {
            mHost.scrollToPosition(0);
        }
    }

    /**
     * 设置下拉动画的颜色值
     */
    public void setProgressBarColor(int color)
    {
        if (mRefreshController != null)
        {
            mRefreshController.setProgressBarColor(color);
        }
    }

    /**
     * @param enable          是否可以正向刷新
     * @param arrowDrawableId 显示的箭头
     * @param custom          自定义图片，例如“随时随地 想淘就淘”
     */
    public void enablePositiveDrag(boolean enable, int arrowDrawableId, View custom)
    {
        if (mRefreshController == null)
        {
            return;
        }

        mEnablePositive = enable;
        mRefreshController.enablePullDownRefresh(enable, arrowDrawableId, 0, custom);
    }

    public void enablePositiveDrag(boolean enable)
    {
        enablePositiveDrag(enable, R.drawable.uik_arrow, null);
    }

    /**
     * @param enable          是否可以逆向刷新
     * @param arrowDrawableId 显示的箭头
     * @param custom          自定义图片，例如“随时随地 想淘就淘”
     */
    public void enableNegativeDrag(boolean enable, int arrowDrawableId, View custom)
    {
        if (mRefreshController == null)
        {
            return;
        }

        mEnableNegative = enable;
        mRefreshController.enablePullUpRefresh(enable, arrowDrawableId, 0, custom);
    }

    public void enableNegativeDrag(boolean enable)
    {
        enableNegativeDrag(enable, R.drawable.uik_arrow, null);
    }

    /**
     * 设置下拉刷新的高度是否包含所设置的图片高度 该方法的调用必须在{@link #enablePositiveDrag }方法之后调用
     *
     * @param isContained 是否让把LogoImage的图片高度算在下拉的阀值内
     */
    public void isHeadViewHeightContainImage(boolean isContained)
    {
        if (mRefreshController != null)
        {
            mRefreshController.isHeadViewHeightContainImage(isContained);
        }
    }

    /**
     * 用于区别是否需要逆向拖动才能刷新，如果设置为true，则代表当RecyclerView滑动到底部自动刷新， 设置为false，需要在RecyclerView到达底部的时候手动逆向拖动到一定位置才能刷新，默认为false
     */
    public void setNegativeDragAuto(boolean isAuto)
    {
        mRefreshController.setPullUpRefreshAuto(isAuto);
        mIsAuto = isAuto;
        if (getHost() != null)
        {
            if (isAuto)
            {
                addAutoLoadScrollListener(getHost());
            }
            else
            {
                if (null != mAutoLoadScrollListener)
                {
                    getHost().removeOnScrollListener(mAutoLoadScrollListener);
                    mAutoLoadScrollListener = null;
                }
            }
        }
    }

    public void setPositiveDragTips(String[] array)
    {
        if (null != mRefreshController)
        {
            mRefreshController.setDownRefreshTips(array);
        }
    }

    public void setNegativeTips(String[] array)
    {
        if (null != mRefreshController)
        {
            mRefreshController.setUpRefreshTips(array);
        }
    }

    public void setOnDragToRefreshListener(OnDragToRefreshListener refreshListener)
    {
        if (mRefreshController != null)
        {
            mRefreshController.setOnRefreshListener(refreshListener);
        }
    }

    public void onDragRefreshComplete()
    {
        if (mRefreshController != null)
        {
            mRefreshController.onRefreshComplete();
            mHost.invalidate();
        }
    }

    /**
     * 该接口是为了支持首页下拉显示彩蛋的需求
     *
     * @return 下拉的距离 异常情况下返回-1
     */
    public int getPositiveDragDistance()
    {
        if (mRefreshController != null)
        {
            return mRefreshController.getPullDownDistance();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public void beforeComputeScroll()
    {
        if (mScroller != null && mScroller.computeScrollOffset())
        {
            if (mRefreshController != null)
            {
                mRefreshController.onScrollerStateChanged(mScroller.getCurrY(), true);
            }
            mHost.invalidate();
        }
        else
        {
            if (mRefreshController != null && mScroller != null)
            {
                mRefreshController.onScrollerStateChanged(mScroller.getCurrY(), false);
            }
        }
    }


    @Override
    public void afterComputeScroll()
    {
    }

    @Override
    public void beforeOnScrollChanged(int l, int t, int oldl, int oldt)
    {
    }

    @Override
    public void afterOnScrollChanged(int l, int t, int oldl, int oldt)
    {
    }

    public void setPositiveRefreshFinish(boolean finished)
    {
        mRefreshController.setDownRefreshFinish(finished);
    }

    public void setNegativeRefreshFinish(boolean finished)
    {
        mRefreshController.setUpRefreshFinish(finished);
    }
}
