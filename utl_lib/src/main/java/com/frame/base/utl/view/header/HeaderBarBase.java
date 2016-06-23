package com.frame.base.utl.view.header;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.frame.base.utl.R;


/**
 * 页头基类，规范定义左中右三个区域的操作
 *
 * @author WilliamChik on 2015/7/
 */
public class HeaderBarBase extends RelativeLayout {

  private RelativeLayout mLeftViewContainer;
  private RelativeLayout mRightViewContainer;
  private RelativeLayout mCenterViewContainer;

  public HeaderBarBase(Context context) {
    super(context);
    init(context);
  }

  public HeaderBarBase(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public HeaderBarBase(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(getLayoutId(), this);
    mLeftViewContainer = (RelativeLayout) findViewById(R.id.header_bar_left_container);
    mCenterViewContainer = (RelativeLayout) findViewById(R.id.header_bar_center_container);
    mRightViewContainer = (RelativeLayout) findViewById(R.id.header_bar_right_container);
  }

  protected int getLayoutId() {
    return R.layout.base_header_bar_base;
  }

  public RelativeLayout getLeftViewContainer() {
    return mLeftViewContainer;
  }

  public RelativeLayout getCenterViewContainer() {
    return mCenterViewContainer;
  }

  public RelativeLayout getRightViewContainer() {
    return mRightViewContainer;
  }

  public void setLeftOnClickListener(OnClickListener l) {
    mLeftViewContainer.setOnClickListener(l);
  }

  public void setCenterOnClickListener(OnClickListener l) {
    mCenterViewContainer.setOnClickListener(l);
  }

  public void setRightOnClickListener(OnClickListener l) {
    mRightViewContainer.setOnClickListener(l);
  }
}