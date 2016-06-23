package com.frame.base.utl.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础RecyclerView列表适配器，封装一些基本的操作
 *
 * @author WilliamChik on 15/7/31.
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  protected LayoutInflater mInflater;
  protected List<T> mData = new ArrayList<T>();
  protected Activity mActivity;

  protected OnAdapterListener onAdapterListener;

  public BaseListAdapter(Activity activity) {
    mActivity = activity;
    mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void clearDataList() {
    mData.clear();
  }

  /**
   * 在原数据的基础上添加新的数据集
   */
  public void addDataList(List<T> newDataList) {
    if (newDataList == null) {
      return;
    }
    this.mData.addAll(newDataList);
  }

  public T getItem(int position) {
    if (position < getItemCount()) {
      return this.mData.get(position);
    }

    return null;
  }

  public void removeItem(int position) {
    mData.remove(position);
  }

  public int getItemPosition(T item) {
    return mData.indexOf(item);
  }

  public List<T> getDataList() {
    return mData;
  }


  /**
   * 销毁时的相关操作，通常在这里销毁一些大对象，如图片、Handler、PopupWindow、Activity、Dialog等可能引起内存泄露的元素。
   */
  public void onDestroy() {
  }
  public interface OnAdapterListener{
    void onItemClick(View v, int position);
  }
  public void setOnAdapterListener(OnAdapterListener onAdapterListener){
    this.onAdapterListener = onAdapterListener;
  }
}
