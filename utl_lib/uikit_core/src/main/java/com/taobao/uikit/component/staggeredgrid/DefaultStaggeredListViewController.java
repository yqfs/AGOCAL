package com.taobao.uikit.component.staggeredgrid;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;

/**
 * 默认的controller实现
 */
@Deprecated
public abstract class DefaultStaggeredListViewController implements IStaggeredListViewController{

    private static final String TAG = "DefaultStaggeredListViewController";
    protected ListAdapter mAdapter;
    protected StaggeredGridView mListView;
    
    public DefaultStaggeredListViewController(ListAdapter adapter, StaggeredGridView listview) {
        mAdapter = adapter;
        mListView = listview;
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
        
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
        {// 滚动停止
            onScrollPaused();
        }
        else if (scrollState == OnScrollListener.SCROLL_STATE_FLING)
        {// 滚动开始
            onScrollResumed();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        
        // 滚动到达末尾
        if (firstVisibleItem + visibleItemCount >= totalItemCount - mListView.getAutoLoadThreshold())
        {
            onFetchNext();
        }
        else if (firstVisibleItem <= 0)
        {
            onFetchPrev();
        }
    }

    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public int getItemCount() {
        if (getAdapter() != null)
            return getAdapter().getCount();
        return 0;
    }
    
    /* 获取下一页 */
    public abstract void onFetchNext();
    
    /* 获取前一页 */
    public abstract void onFetchPrev();
    
    /* 滑动中(可用于 停止加载图片) */
    public abstract void onScrollResumed();
    
    /* 滑动停止(可用于 继续加载图片) */
    public abstract void onScrollPaused();

}
