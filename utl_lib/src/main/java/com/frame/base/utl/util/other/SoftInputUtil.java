package com.frame.base.utl.util.other;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具
 *
 * @author WilliamChik  on 2015/8/26.
 */
public class SoftInputUtil {

  /**
   * 展示软键盘
   */
  public static void showSoftInput(EditText editText) {
    editText.requestFocus();
    InputMethodManager inputMethodManager =
        (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.showSoftInput(editText, 0);
  }

  /**
   * 隐藏软键盘
   */
  public static void hideSoftInput(Activity activity) {
    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
      inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
  }

  public static boolean softInputIsActive(Activity activity, EditText editText) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    return inputMethodManager.isActive(editText);
  }

}
