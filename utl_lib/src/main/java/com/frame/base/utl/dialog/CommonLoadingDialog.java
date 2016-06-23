package com.frame.base.utl.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frame.base.utl.R;
import com.frame.base.utl.jump.PanelManager;


/**
 * 通用的加载中 dialog
 *
 * @author WilliamChik on 15/9/17.
 */
public class CommonLoadingDialog extends CommonBaseSafeDialog {

  private View mMainLayout;
  private TextView mLoadingMsgTv;

  private AnimatorSet mAnimatorSet;

  public CommonLoadingDialog(Activity activity) {
    this(activity, false);
  }

  /**
   * @param cancelable true 点击回退按钮，退回上一步
   */
  public CommonLoadingDialog(Activity activity, boolean cancelable) {
    super(activity, R.style.App_CommonLoadingDialog);
    if (cancelable) {
      setOnCancelListener(new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
          PanelManager.getInstance().back();
        }
      });
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_loading_dialog_layout);
    mLoadingMsgTv = (TextView) findViewById(R.id.tv_loading_dialog_msg);
    mMainLayout = findViewById(R.id.ll_common_loading_dialog_main_layout);
  }

  public void setMessage(String msg) {
    if (mLoadingMsgTv != null) {
      mLoadingMsgTv.setText(msg);
    }
  }

  @Override
  public void dismiss() {
    // dismiss 时有渐消动画
    if (mAnimatorSet == null) {
      // 懒加载
      ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(mMainLayout, "scaleX", 1.0f, 0.8f);
      ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(mMainLayout, "scaleY", 1.0f, 0.8f);
      ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(mMainLayout, "alpha", 1.0f, 0.8f);
      mAnimatorSet = new AnimatorSet();
      mAnimatorSet.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mMainLayout.clearAnimation();
          CommonLoadingDialog.super.dismiss();
        }
      });
      mAnimatorSet.playTogether(scaleXAnim, scaleYAnim, alphaAnim);
      mAnimatorSet.setDuration(400);
    }
    mAnimatorSet.start();
  }
}
