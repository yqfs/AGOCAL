package com.frame.base.utl.view.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 倒计时控件
 * 1、CountDownListener onFinish实现回调
 * 2、继承TextView，可以用TextView的布局属性
 *
 * @author lxd on 2015/10/24.
 */
public class CountDownTextView extends TextView {

  public static final String TAG = "CountDownTextView";

  private CountDownTimer mCountDownTimer;
  private CountDownListener mCountDownListener;

  private String startText, endText, timeTextColor;
  private long mMillisInFuture;
  private long mCountDownInterval = 1000;//default

  //时分秒
  private int mHours = 0;
  private int mMinutes = 0;
  private int mSeconds = 0;

  public CountDownTextView(Context context) {
    this(context, null);
  }

  public CountDownTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void initCountDownTimer() {
    mCountDownTimer = new CountDownTimer(mMillisInFuture, mCountDownInterval) {


      @Override
      public void onTick(long millisUntilFinished) {
        String hmsTime = millisToHMS(millisUntilFinished);
        if (startText == null || "".equals(startText.trim())) {
          CountDownTextView.this.setText(hmsTime);
        } else if (startText != null && !"".equals(startText.trim())) {
          CountDownTextView.this
              .setText(Html.fromHtml(startText + " <font color = '"+timeTextColor+"'> " + hmsTime + "</font>" + " " + endText));
//          Spannable sText = (Spannable) (startText + hmsTime + endText+ "jd");
//          sText.setSpan(new BackgroundColorSpan(Color.RED), 1, 4, 0);
//
//          CountDownTextView.this.setText(startText + hmsTime + endText+"jd");
        }


      }

      @Override
      public void onFinish() {
        CountDownTextView.this.setText("0");

        if (mCountDownListener != null) {
          Log.d(TAG, "onFinish");
          mCountDownListener.onFinish();
        }

      }
    };
  }

  private String millisToHMS(long millisUntilFinished) {
    long totalSeconds = millisUntilFinished / 1000 + 1;//进一取整
    mHours = (int) (totalSeconds / (60 * 60));
    mMinutes = (int) ((totalSeconds - mHours * 60 * 60) / 60);
    mSeconds = (int) (totalSeconds - mHours * 60 * 60 - mMinutes * 60);

    if (mHours == 0 && mMinutes != 0) {
      return String.format("%02d分%02d秒", mMinutes, mSeconds);
    } else if (mMinutes == 0) {
      return String.format("%02d秒", mSeconds);
    }
    return String.format("%02d时%02d分%02d秒", mHours, mMinutes, mSeconds);
  }


  public interface CountDownListener {

    void onFinish();
  }


  public void setCountDownListener(CountDownListener countDownListener) {
    mCountDownListener = countDownListener;
  }


  /**
   * 设置倒计时时间
   */
  public void setCountDownTimer(long millisInFuture) {
    setCountDownTimer(millisInFuture, 1000);
  }

  /**
   * @param millisInFuture
   * @param countDownInterval
   */
  public void setCountDownTimer(long millisInFuture, long countDownInterval) {
    mMillisInFuture = millisInFuture;
    mCountDownInterval = countDownInterval;
    initCountDownTimer();
  }

  /**
   * 开始倒计时
   */
  public void start() {
    mCountDownTimer.start();
  }

  public void cancel() {
    if (mCountDownTimer != null) {
      mCountDownTimer.cancel();
      mCountDownTimer = null;
    }
  }

  public void setTextViewType(String startText, String endText, String timeTextColor) {
    this.startText = startText;
    this.endText = endText;
    this.timeTextColor = timeTextColor;
  }

}


