package com.frame.base.utl.view.listview;

import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 加载更多接口类，定义处理各类情况下的操作
 *
 * @author by WilliamChik on 15/8/15.
 */
public interface ILoadMoreHandler {

  /**
   * 加载开始
   */
  void onLoadStart();

  /**
   * 加载完成: 数据为空 / 没有更多
   */
  void onLoadFinish(TRecyclerView listView);

  /**
   * 等待加载更多
   */
  void onWaitToLoadMore(TRecyclerView listView);

  /**
   * 加载失败
   */
  void onLoadError(TRecyclerView listView, int errorCode, String errorMessage);

}
