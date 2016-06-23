package com.frame.base.utl.view.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frame.base.utl.R;
import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.util.net.NetWorkUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 列表滚动加载更多处理类
 *
 * @author by WilliamChik on 15/8/14.
 */
public class CommonListLoadMoreHandler implements ILoadMoreHandler {

  private static final String LOADING_MSG = BaseApplication.getContext().getResources().getString(R.string.loading_more_footer);
  private static final String LOAD_FINISH_MSG = BaseApplication.getContext().getResources().getString(R.string.load_finish_footer);

  /** 提示跟布局 */
  private View mFooterRootView;

  /** 提示文本视图 */
  private TextView mFooterStateView;

  /** 底部布局 */
  private RelativeLayout footerLayout;


  /** 是否正在加载更多 */
  private boolean mIsLoadingMore;

  private LoadMoreCallBack mCallBack;

  public void setCallBack(LoadMoreCallBack callBack) {
    mCallBack = callBack;
  }

  public CommonListLoadMoreHandler() {
    mFooterRootView = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.common_list_pager_footer, null);
    mFooterRootView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (LOAD_FINISH_MSG.equals(mFooterStateView.getText())) {
          mCallBack.onClickLoadMore();
        }
      }
    });
    footerLayout = (RelativeLayout) mFooterRootView.findViewById(R.id.footer_layout);
    mFooterStateView = (TextView) mFooterRootView.findViewById(R.id.tv_common_list_pager_footer);
  }

  /**
   * 删除脚部
   */
  public void removeFooter(TRecyclerView listView) {
    listView.removeFooterView(mFooterRootView);
  }

  /**
   * @return 是否可以加载更多，只有当前不在进行加载更多的操作且网络可用时，才可以加载更多
   */
  public boolean canLoadMore() {
    return !mIsLoadingMore && NetWorkUtil.isNetworkAvailable(BaseApplication.getContext());
  }

  @Override
  public void onLoadStart() {
    mFooterStateView.setText(LOADING_MSG);
    mIsLoadingMore = true;
  }

  @Override
  public void onLoadFinish(TRecyclerView listView) {
    mFooterStateView.setText(LOAD_FINISH_MSG);
    listView.removeFooterView(mFooterRootView);
    listView.addFooterView(mFooterRootView);
    mIsLoadingMore = false;
  }

  @Override
  public void onWaitToLoadMore(TRecyclerView listView) {
    mFooterStateView.setText(LOADING_MSG);
    listView.removeFooterView(mFooterRootView);
    listView.addFooterView(mFooterRootView);
    mIsLoadingMore = false;
  }

  @Override
  public void onLoadError(TRecyclerView listView, int errorCode, String errorMessage) {

  }
  public boolean getLoadStatu(){
    return mIsLoadingMore;
  }

  public RelativeLayout getFooterLayout(){
    return footerLayout;
  }

  /**
   * 滚动加载更多模块的回调
   */
  public interface LoadMoreCallBack {

    /**
     * 点击加载更多视图时的回调
     */
    void onClickLoadMore();

  }
}
