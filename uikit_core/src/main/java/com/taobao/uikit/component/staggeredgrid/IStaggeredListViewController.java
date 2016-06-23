package com.taobao.uikit.component.staggeredgrid;

import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;

/**
 * 瀑布流listview控件数据相关的controller
 */
@Deprecated
public interface IStaggeredListViewController extends OnScrollListener
{

    /**
     * 获取listdaapter
     */
    ListAdapter getAdapter();

    /**
     * 获取数据个数
     */
    int getItemCount();

}
