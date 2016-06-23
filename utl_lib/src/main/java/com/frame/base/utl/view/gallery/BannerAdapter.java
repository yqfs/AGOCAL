package com.frame.base.utl.view.gallery;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 图片轮播 banner 的数据适配器
 *
 * @author YANGQIYUN on 15/10/12.
 */
public abstract class BannerAdapter<T> extends PagerAdapter {

  /** 是否循环显示 */
  private boolean mIsRecycleShow = false;

  /** view pager 需要轮播的数据集合 */
  private List<T> mDataList;

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    View view = getView(LayoutInflater.from(container.getContext()), position);
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }


  @Override
  public boolean isViewFromObject(View view, Object o) {
    return view == o;
  }

  @Override
  public int getCount() {
    if (mIsRecycleShow) {
      return Integer.MAX_VALUE;
    } else {
      if (mDataList == null) {
        return 0;
      }
      return mDataList.size();
    }
  }

  /**
   * 返回指定索引的数据对象
   *
   * @return null if error occurred
   */
  public T getItem(int position) {
    if (mDataList == null || mDataList.size() == 0) {
      return null;
    }
    return mDataList.get(getPositionForIndicator(position));
  }

  public int getPositionForIndicator(int position) {
    if (null == mDataList || mDataList.size() == 0) {
      return 0;
    }
    return position % mDataList.size();
  }

  public void setData(List<T> dataList) {
    if (dataList != null && dataList.size() > 0) {
      mDataList = dataList;
    }
  }

  public void setRecycleShowState(boolean isRecycleShow) {
    mIsRecycleShow = isRecycleShow;
  }

  public abstract View getView(LayoutInflater layoutInflater, int position);

}
