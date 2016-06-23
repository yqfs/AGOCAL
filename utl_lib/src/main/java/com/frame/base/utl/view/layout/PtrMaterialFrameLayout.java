package com.frame.base.utl.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.frame.base.utl.R;
import com.frame.base.utl.util.local.DensityUtil;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrCLog;

/**
 * material 风格的下拉刷新 header， 封装一些基本操作及初始化参数
 *
 * @author YANGQIYUN on 2015/9/2.
 */
public class PtrMaterialFrameLayout extends PtrFrameLayout {

  private MaterialHeader mPtrMaterialHeader;

  public PtrMaterialFrameLayout(Context context) {
    super(context);
    initViews();
  }

  public PtrMaterialFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initViews();
  }

  public PtrMaterialFrameLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initViews();
  }

  private void initViews() {
    // init header
    mPtrMaterialHeader = new MaterialHeader(getContext());
    int[] colors = getResources().getIntArray(R.array.ptr_material_header_colors);
    mPtrMaterialHeader.setColorSchemeColors(colors);
    mPtrMaterialHeader.setLayoutParams(new LayoutParams(-1, -2));
    mPtrMaterialHeader.setPadding(0, DensityUtil.dp2px(15), 0, DensityUtil.dp2px(10));
    mPtrMaterialHeader.setPtrFrameLayout(this);

    // init PtrFrameLayout
    setHeaderView(mPtrMaterialHeader);
    addPtrUIHandler(mPtrMaterialHeader);
    // 阻尼系数，越大，感觉下拉时越吃力
    setResistance(1.9f);
    // 触发刷新时移动的位置对于头部的比例
    setRatioOfHeaderHeightToRefresh(1.7f);
    // 回弹到刷新高度所用时间
    setDurationToClose(200);
    // 头部回弹时间
    setDurationToCloseHeader(800);
    // 刷新时保持头部
    setKeepHeaderWhenRefresh(true);
    // false 释放刷新 | true 下拉刷新
    setPullToRefresh(false);
  }

  public MaterialHeader getHeader() {
    return mPtrMaterialHeader;
  }

}
