package com.frame.base.utl.view.header;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frame.base.utl.R;

/**
 * 普通标题头部的实现:
 * 左侧返回
 * 中部标题
 * 右侧文字
 *
 * @author WilliamChik on 2015/7/21
 */
public class TitleHeaderBar extends HeaderBarBase {

  private FrameLayout mMainContainer;

  private RelativeLayout rl_title_bar_left;
  private TextView mLeftTextView;
  private ImageView mLeftReturnImageView;

  private TextView mCenterTitleTextView;
  private ImageView mCenterTitleImageView;

  private RelativeLayout rl_title_bar_right;
  private TextView mRightTextView;
  private ImageView mRightImageView;

//  private View mTopDivider;
//  private View mBottomDivider;

  private String title;

  public TitleHeaderBar(Context context) {
    super(context);
    init();
  }

  public TitleHeaderBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TitleHeaderBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    mMainContainer = (FrameLayout) findViewById(R.id.header_bar_main_container);

    rl_title_bar_left = (RelativeLayout) findViewById(R.id.header_bar_left_container);
    mLeftTextView = (TextView) findViewById(R.id.tv_title_bar_left);
    mLeftReturnImageView = (ImageView) findViewById(R.id.iv_title_bar_left);

    mCenterTitleTextView = (TextView) findViewById(R.id.tv_title_bar_title);
    mCenterTitleImageView = (ImageView) findViewById(R.id.iv_title_bar_img);

    rl_title_bar_right = (RelativeLayout) findViewById(R.id.header_bar_right_container);
    mRightTextView = (TextView) findViewById(R.id.tv_title_bar_right);
    mRightImageView = (ImageView) findViewById(R.id.iv_title_bar_right);

//    mTopDivider = findViewById(R.id.header_top_divider);
//    mTopDivider.setVisibility(View.GONE);
//    mBottomDivider = findViewById(R.id.header_bottom_divider);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.base_header_bar_title;
  }

//  public View getBottomDivider() {
//    return mBottomDivider;
//  }
//
//  public View getTopDivider() {
//    return mTopDivider;
//  }

  public void setBackgroundColor(int bgColor) {
    mMainContainer.setBackgroundColor(bgColor);
  }

  public void setLeftContainerWidth(int width) {
    rl_title_bar_left.getLayoutParams().width = width;
  }

  public ImageView getLeftImageView() {
    return mLeftReturnImageView;
  }

  public TextView getLeftTextView() {
    return mLeftTextView;
  }

  public TextView getTitleTextView() {
    return mCenterTitleTextView;
  }

  public ImageView getTitleImageView(){
    return mCenterTitleImageView;
  }

  public void setTitle(String title) {
    this.title = title;
    mCenterTitleTextView.setText(title);
  }

  public void setLeftViewTitle(String title) {
    mLeftTextView.setVisibility(View.VISIBLE);
    mLeftTextView.setText(title);
    rl_title_bar_left.setVisibility(View.VISIBLE);
  }

  public ImageView getRightImageView() {
    return mRightImageView;
  }

  public TextView getRightTextView() {
    return mRightTextView;
  }

  private RelativeLayout.LayoutParams makeLayoutParams(View view) {
    ViewGroup.LayoutParams lpOld = view.getLayoutParams();
    RelativeLayout.LayoutParams lp = null;
    if (lpOld == null) {
      lp = new RelativeLayout.LayoutParams(-2, -1);
    } else {
      lp = new RelativeLayout.LayoutParams(lpOld.width, lpOld.height);
    }
    return lp;
  }

  /**
   * set customized view to leftPos side
   *
   * @param view the view to be added to leftPos side
   */
  public void setCustomizedLeftView(View view) {
    mLeftReturnImageView.setVisibility(GONE);
    getLeftViewContainer().addView(view);
  }

  /**
   * set customized view to leftPos side
   *
   * @param layoutId the xml layout file id
   */
  public void setCustomizedLeftView(int layoutId) {
    View view = inflate(getContext(), layoutId, null);
    setCustomizedLeftView(view);
  }

  public void setLeftWidth(int width) {
    ViewGroup.LayoutParams lpOld = rl_title_bar_left.getLayoutParams();
    lpOld.width = width;
  }

  /**
   * set customized view to center
   *
   * @param view the view to be added to center
   */
  public void setCustomizedCenterView(View view) {
    mCenterTitleTextView.setVisibility(GONE);
    RelativeLayout.LayoutParams lp = makeLayoutParams(view);
    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
    getCenterViewContainer().addView(view, lp);
  }

  /**
   * set customized view to center
   *
   * @param layoutId the xml layout file id
   */
  public View setCustomizedCenterView(int layoutId) {
    View view = inflate(getContext(), layoutId, null);
    setCustomizedCenterView(view);
    return view;
  }

  /**
   * set customized view to rightPos side
   *
   * @param view the view to be added to rightPos side
   */
  public void setCustomizedRightView(View view) {
    mRightTextView.setVisibility(GONE);
    getRightViewContainer().addView(view);
  }

  public void setRightContainerWidth(int width) {
    rl_title_bar_right.getLayoutParams().width = width;
  }

  /**
   * set customized view to rightPos side
   *
   * @param layoutId the xml layout file id
   */
  public void setCustomizedRightView(int layoutId) {
    View view = inflate(getContext(), layoutId, null);
    setCustomizedRightView(view);
  }

  public void setCustomizedRightView(int layoutId, int width) {
    ViewGroup.LayoutParams rightLp = rl_title_bar_right.getLayoutParams();
    rightLp.width = width;
    View view = inflate(getContext(), layoutId, null);
    setCustomizedRightView(view);

    ViewGroup.LayoutParams leftLp = rl_title_bar_left.getLayoutParams();
    leftLp.width = width;

    rl_title_bar_left.setLayoutParams(leftLp);

    if (!TextUtils.isEmpty(title)) {
      mCenterTitleTextView.setVisibility(View.INVISIBLE);
    }
  }

  /**
   * 设置右文本（iconfont）2015-11-05
   */
  public void setRightText(String text) {
    getRightTextView().setVisibility(View.VISIBLE);
    getRightTextView().setText(text);
    getRightImageView().setVisibility(View.GONE);
  }
}