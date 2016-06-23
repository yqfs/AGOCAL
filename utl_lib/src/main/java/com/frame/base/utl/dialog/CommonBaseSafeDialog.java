package com.frame.base.utl.dialog;


import android.app.Activity;
import android.app.Dialog;

import com.frame.base.utl.R;


/**
 * 自定义Dialog的基类
 * dialog show和dismiss的时候，activity已经不存在的话就会报java.lang.IllegalArgumentException: View not attached to window manager，
 * 针对这个问题在这个类中统一处理，show和dismiss时判断下activity是否存在。
 *
 * @author WilliamChik on 2015/7/21
 */
public class CommonBaseSafeDialog extends Dialog {

  protected Activity activity;

  public CommonBaseSafeDialog(Activity activity, int theme) {
    super(activity, theme);
    this.activity = activity;
  }

  public CommonBaseSafeDialog(Activity activity) {
    super(activity, R.style.CommonAlertDialog);
    this.activity = activity;
  }

  @Override
  public void cancel() {
    try {
      if (activity != null && !(activity.isFinishing())) {
        super.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dismiss() {
    try {
      if (activity != null && !(activity.isFinishing())) {
        super.dismiss();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void show() {
    try {
      if (activity != null && !(activity.isFinishing())) {
        super.show();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
