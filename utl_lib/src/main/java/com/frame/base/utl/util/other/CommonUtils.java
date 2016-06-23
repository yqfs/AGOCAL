package com.frame.base.utl.util.other;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.TouchDelegate;
import android.view.View;

/**
 * 临时添加的一些工具类
 *
 * @author lxd on 2015/11/05.
 */
public class CommonUtils {

  private static long lastClickTime;

  /**
   * 防止快速重复点击
   */
  public static boolean isFastDoubleClick() {
    long time = System.currentTimeMillis();
    long timeD = time - lastClickTime;
    if (0 < timeD && timeD < 800) {
      return true;
    }
    lastClickTime = time;
    return false;
  }


  /**
   * 1、RecyclerView 嵌套 RecyclerView
   * 内部 RecyclerView 的高度必须设定一个固定值，否则不能显示，这里设置的高度 = item 高度 * item 个数
   * 2、CommonAlertDialog
   */
  public static void fixRecyclerViewHeight(RecyclerView recyclerView, int size, int itemHeight) {
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    recyclerView.getLayoutParams().height = itemHeight * size;
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
  }

  /**
   * 扩大View的触摸和点击响应范围,最大不超过其父View范围
   *
   * @param view
   * @param left
   * @param top 向上扩展像素值 单位px
   * @param right
   * @param bottom
   */
  public static void expandViewTouchDelegate(final View view, final int left, final int top, final int right, final int bottom) {

    ((View) view.getParent()).post(new Runnable() {
      @Override
      public void run() {
        Rect bounds = new Rect();
        view.setEnabled(true);
        view.getHitRect(bounds);

        bounds.top -= top;
        bounds.bottom += bottom;
        bounds.left -= left;
        bounds.right += right;

        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

        if (View.class.isInstance(view.getParent())) {
          ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
      }
    });
  }

  public static void expandViewTouchDelegate(final View view, float screenDensity) {
    int expandDP = (int) (10 * screenDensity);
    expandViewTouchDelegate(view, expandDP, expandDP, expandDP, expandDP);
  }

}
