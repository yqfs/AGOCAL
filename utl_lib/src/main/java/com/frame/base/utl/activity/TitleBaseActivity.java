package com.frame.base.utl.activity;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.frame.base.utl.R;
import com.frame.base.utl.dialog.CommonLoadingDialog;
import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.frame.base.utl.mvp.delegate.IDelegate;
import com.frame.base.utl.view.header.TitleHeaderBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 带页头的Activity基类
 * 使用一个orientation="vertical"的LinearLayout，包含一个统一的页头{@link TitleHeaderBar}，内容置于页头下部
 *
 * @author WilliamChik on 2015/7/21
 */
public abstract class TitleBaseActivity<T extends IDelegate> extends SwipeBackActivity {

  protected T viewDelegate;

  // 头部标题栏
  protected TitleHeaderBar mTitleHeaderBar;

  // activity主布局
  private ViewGroup mContentViewContainer;

  public TitleBaseActivity() {
    try {
      viewDelegate = getDelegateClass().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException("create IDelegate error");
    } catch (IllegalAccessException e) {
      throw new RuntimeException("create IDelegate error");
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    //按顺序
    viewDelegate.create(getLayoutInflater(), null, savedInstanceState);
    super.onCreate(savedInstanceState);

    super.setContentView(viewDelegate.getRootView());

  }

  private void init() {
    mContentViewContainer = (ViewGroup) findViewById(R.id.hai_content);
    mTitleHeaderBar = (TitleHeaderBar) findViewById(R.id.ly_header_bar_title_wrap);
    if (enableDefaultBack()) {
      //使用默认的返回按钮
      mTitleHeaderBar.setLeftOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          // 如果子类实现了点击返回按钮的操作，则不做面板的返回操作；否则做面板的返回操作；
          // 子类实现的返回操作优先级高于超类的面板返回操作
          if (!processClickBack()) {
            returnBack();
          }
        }
      });
    } else {
      // 否则隐藏返回按钮
      mTitleHeaderBar.getLeftViewContainer().setVisibility(View.INVISIBLE);
    }
  }

  /**
   * 允许activity点击页头回顶（子类实现，默认允许）
   *
   * @return true 允许，false 不允许
   */
  protected boolean enableClickHeaderBackToTop() {
    return true;
  }

  /**
   * 点击返回按钮后的处理（子类实现，默认不处理）
   *
   * @return true 处理，false 不处理
   */
  protected boolean processClickBack() {
    return false;
  }

  /**
   * 重写，将内容置于LinearLayout中的统一头部的下方
   */
  @Override
  public void setContentView(int layoutResID) {
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    mContentViewContainer.addView(view);
    /**
     * TODO 先不设置带颜色的透明状态栏，因为会和部分机型的状态栏颜色冲突
     */
//    initSystemBarTint(R.color.white);
  }

  /**
   * 将内容置于LinearLayout中的统一头部的下方
   */
  public void setContentView(int layoutResID, ViewGroup.LayoutParams params) {
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    mContentViewContainer.addView(view, params);
    initSystemBarTint(R.color.white);
  }

  /**
   * 将内容置于LinearLayout中的统一头部的下方，并半透明状态栏的颜色
   */
  public void setContentViewAndSystemBarTint(int layoutResID, int systemBarTintResId) {
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    mContentViewContainer.addView(view);
    initSystemBarTint(systemBarTintResId);
  }

  /**
   * 初始化半透明状态栏 | 导航栏（支持4.4以后的系统），必须在 setContentView() 之后设置才有效
   *
   * @param systemBarTintResId 状态栏 | 导航栏 的背景颜色
   */
  @TargetApi(19)
  private void initSystemBarTint(int systemBarTintResId) {
    if (Build.VERSION.SDK_INT >= 19) {
      // 半透明状态栏
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    // create SystemBarTintManager instance after the content view is set
    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    // enable status bar tint
    tintManager.setStatusBarTintEnabled(true);
    // set a custom tint color for all system bars
    tintManager.setStatusBarTintColor(getResources().getColor(systemBarTintResId));
  }

  /**
   * 是否使用默认的返回处理
   */
  protected boolean enableDefaultBack() {
    return true;
  }

  /**
   * 点击头部中央返回页面头部
   */
  protected void setOnClickHeaderCenterBackToTopHandler(OnClickListener handler) {
    mTitleHeaderBar.getCenterViewContainer().setOnClickListener(handler);
  }

  /**
   * 设置标题
   */
  protected void setHeaderTitle(int id) {
    mTitleHeaderBar.getTitleTextView().setText(id);
    mTitleHeaderBar.getTitleTextView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getTitleImageView().setVisibility(View.GONE);
  }

  /**
   * 设置标题图片
   */
  protected void setHeaderImg(int resId) {
    mTitleHeaderBar.getTitleImageView().setImageResource(resId);
    mTitleHeaderBar.getTitleImageView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getTitleTextView().setVisibility(View.GONE);
  }

  /**
   * 设置标题
   */
  protected void setHeaderTitle(String title) {
    mTitleHeaderBar.setTitle(title);
  }

  /**
   * 设置左文本（iconfont）
   */
  protected void setLeftText(String title, int textSize) {
    mTitleHeaderBar.getLeftTextView().setTextSize(textSize);
    setLeftText(title);
  }

  /**
   * 设置左文本（iconfont）
   */
  protected void setLeftText(String title) {
    mTitleHeaderBar.getLeftTextView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getLeftTextView().setText(title);
    mTitleHeaderBar.getLeftImageView().setVisibility(View.GONE);
  }

  /**
   * 设置左图标
   */
  protected void setLeftImage(int resId) {
    mTitleHeaderBar.getLeftImageView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getLeftImageView().setImageResource(resId);
    mTitleHeaderBar.getLeftTextView().setVisibility(View.GONE);
  }


  /**
   * 设置右文本（iconfont）
   */
  protected void setRightText(String text) {
    mTitleHeaderBar.getRightTextView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getRightTextView().setText(text);
    mTitleHeaderBar.getRightImageView().setVisibility(View.GONE);
  }

  /**
   * 设置右文本（iconfont）
   */
  protected void setRightText(String text, int size) {
    mTitleHeaderBar.getRightTextView().setTextSize(size);
    mTitleHeaderBar.getRightTextView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getRightTextView().setText(text);
    mTitleHeaderBar.getRightImageView().setVisibility(View.GONE);
  }

  /**
   * 设置右图标
   */
  protected void setRightImage(int resId) {
    mTitleHeaderBar.getRightImageView().setVisibility(View.VISIBLE);
    mTitleHeaderBar.getRightImageView().setImageResource(resId);
    mTitleHeaderBar.getRightTextView().setVisibility(View.GONE);
  }

  /**
   * 设置右图标
   */
  protected void setRightImage(int resId, int width, int height) {
    setRightImage(resId);
    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitleHeaderBar.getRightImageView().getLayoutParams();
    lp.width = width;
    lp.height = height;
  }


  /**
   * 设置右文本（iconfont）为灰
   */
  protected void setRightTextGray() {
    mTitleHeaderBar.getRightTextView().setTextColor(0xff999999);
  }

//  /**
//   * 默认不特殊后退
//   */
//  @Override
//  protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
//    if (processClickBack()) {
//      return true;
//    }
//    return false;
//  }

  /**
   * 隐藏页头
   */
  protected void hideTitleBar() {
    mTitleHeaderBar.setVisibility(View.GONE);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
//    if (hasFocus) {
//      RookieGuideManager.getInstance().show(TitleBaseActivity.this);
//    }
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    if(viewDelegate instanceof AppDelegate){
      AppDelegate mAppDelegate = (AppDelegate) viewDelegate;
      mAppDelegate.onDestroy();
    }
    super.onDestroy();
  }

  protected abstract Class<T> getDelegateClass();

}