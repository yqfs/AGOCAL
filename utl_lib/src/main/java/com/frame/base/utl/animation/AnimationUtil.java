package com.frame.base.utl.animation;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


/**
 * 常用Tween动画集合
 *
 * @author WilliamChik on 2015/7/22
 */
public class AnimationUtil {

  private Animation mInAnimation;
  private Animation mOutAnimation;
  private PlayListener mPlayListener;
  private Direction mDirection = Direction.to_bottom;

  private AnimationUtil() {
  }

  private AnimationUtil(Context context) {
    setDirection(Direction.to_bottom);
  }

  private AnimationUtil(Context context, Direction direction) {
    setDirection(direction);
  }

  private AnimationUtil(Context context, PlayListener listener) {
    mPlayListener = listener;
    setDirection(Direction.to_bottom);
  }

  private AnimationUtil(Context context, Direction direction, PlayListener listener) {
    mPlayListener = listener;
    setDirection(direction);
  }

  /**
   * 不常用,不需要单例模式
   */
  public static AnimationUtil getInstance(Activity act) {
    return new AnimationUtil(act);
  }

  public static AnimationUtil getInstance(Activity act,PlayListener listener) {
    return new AnimationUtil(act, listener);
  }

  public static AnimationUtil getInstance(Activity act,Direction direction) {
    return new AnimationUtil(act, direction);
  }

  public static AnimationUtil getInstance(Activity act,Direction direction, PlayListener listener) {
    return new AnimationUtil(act, listener);
  }

  /**
   * 设置动画方向
   */
  private void setDirection(Direction direction) {
    mDirection = direction;
    if (direction == Direction.to_bottom) {
      mInAnimation = in2BottomAnimation();
      mOutAnimation = out2BottomAnimation();
    } else if (direction == Direction.to_top) {
      mInAnimation = in2TopAnimation();
      mOutAnimation = out2TopAnimation();
    } else if (direction == Direction.to_left) {
      mInAnimation = in2LeftAnimation();
      mOutAnimation = out2LeftAnimation();
    } else if (direction == Direction.to_right) {
      mInAnimation = tipAnimation(0f, 1f, -0.3f, 0f, 0f, 0f);
      mOutAnimation = tipAnimation(1f, 0f, 0f, -0.3f, 0f, 0f);
    }
  }

  /**
   * 播放动画
   */
  public void play(final View view, final int startDelay, final int endDelay) {
    startPlay(view, startDelay, endDelay);
  }

  public void play(final View view, final int startDelay, final int endDelay, final int height) {
    startPlay(view, startDelay, endDelay, height);
  }

  /**
   * 执行单个动画
   */
  public void simplePlay(final View view, final int delay) {
    mInAnimation.setStartOffset(delay);
    mInAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        onPlayStart(animation);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        onPlayEnd(animation);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        onPlayRepeat(animation);
      }
    });
    view.startAnimation(mInAnimation);
  }

  /**
   * 开始动画
   */
  private void startPlay(final View view, final int startDelay, final int endDelay) {
    mInAnimation.setStartOffset(startDelay);
    mInAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        onPlayStart(animation);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        endPlay(view, endDelay);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        onPlayRepeat(animation);
      }
    });

    view.setVisibility(View.VISIBLE);
    view.startAnimation(mInAnimation);
  }


  private void startPlay(final View view, final int startDelay, final int endDelay, final int height) {
    mInAnimation.setStartOffset(startDelay);
    mInAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        onPlayStart(animation);
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        endPlay(view, endDelay);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        onPlayRepeat(animation);
      }
    });

    view.setVisibility(View.VISIBLE);
    view.startAnimation(mInAnimation);
  }

  /**
   * 结束动画
   */
  private void endPlay(final View view, final int endDelay) {
    mOutAnimation.setStartOffset(endDelay);
    mOutAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.GONE);
        onPlayEnd(animation);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
    view.startAnimation(mOutAnimation);
  }

  public AnimationSet in2BottomAnimationFast() {
    return tipAnimation(1f, 1f, 0f, 0f, 1f, 0f);
  }

  public AnimationSet in2BottomAnimation() {
    return tipAnimation(0f, 1f, 0f, 0f, 0.3f, 0f);
  }

  public AnimationSet out2BottomAnimation() {
    return tipAnimation(1f, 0f, 0f, 0f, 0f, 0.3f);
  }

  public AnimationSet in2TopAnimation() {
    return tipAnimation(0f, 1f, 0f, 0f, -0.3f, 0f);
  }

  public AnimationSet out2TopAnimation() {
    return tipAnimation(1f, 0f, 0f, 0f, 0f, -0.3f);
  }

  public AnimationSet in2LeftAnimation() {
    return tipAnimation(0f, 1f, 0.3f, 0f, 0f, 0f);
  }

  public AnimationSet out2LeftAnimation() {
    return tipAnimation(1f, 0f, 0f, 0.3f, 0f, 0f);
  }

  /**
   * 透明度
   */
  public AlphaAnimation alphaAnimation(Interpolator interpolator, int duration, float fromAlpha, float toAlpha) {
    // 透明度
    AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
    alphaAnimation.setInterpolator(interpolator);
    alphaAnimation.setDuration(duration);
    alphaAnimation.setFillAfter(true);
    return alphaAnimation;
  }

  /**
   * 移动
   */
  public TranslateAnimation translateAnimation(Interpolator interpolator, int duration, float fromXDelta,
                                               float toXDelta, float fromYDelta, float toYDelta) {
    // 移动
    TranslateAnimation translateAnimation =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromXDelta, Animation.RELATIVE_TO_SELF, toXDelta,
                               Animation.RELATIVE_TO_SELF, fromYDelta, Animation.RELATIVE_TO_SELF, toYDelta);
    translateAnimation.setInterpolator(interpolator);
    translateAnimation.setDuration(duration);
    translateAnimation.setFillAfter(true);
    return translateAnimation;
  }

  /**
   * 缩放动画
   */
  public ScaleAnimation scaleAnimation(Interpolator interpolator, int duration, float fromX, float toX, float fromY,
                                       float toY, int pivotXType, float pivotXValue, int pivotYType,
                                       float pivotYValue) {
    ScaleAnimation scaleAnimation =
        new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
    scaleAnimation.setInterpolator(interpolator);
    scaleAnimation.setDuration(duration);
    scaleAnimation.setFillAfter(true);
    return scaleAnimation;
  }


  /**
   * 放大弹跳效果
   */
  public Animation starInAnimation(int duration, int delay) {
    ScaleAnimation scaleAnimation =
        new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    scaleAnimation.setInterpolator(new AnticipateOvershootInterpolator());
    scaleAnimation.setDuration(duration);
    scaleAnimation.setFillAfter(true);
    scaleAnimation.setStartOffset(delay);
    return scaleAnimation;
  }

  /**
   * 缩小弹跳效果
   */
  public Animation starOutAnimation(int duration, int delay) {
    ScaleAnimation scaleAnimation =
        new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    scaleAnimation.setInterpolator(new AnticipateOvershootInterpolator());
    scaleAnimation.setDuration(duration);
    scaleAnimation.setFillAfter(true);
    scaleAnimation.setStartOffset(delay);
    return scaleAnimation;
  }

  /**
   * 点赞动效
   */
  public Animation diggAnimation() {
    int duration = 400;
    Interpolator interpolator = new AnticipateInterpolator();
    AnimationSet animationSet = new AnimationSet(true);
    //animationSet.setStartOffset(interpolator);
    animationSet.setInterpolator(interpolator);
    //animationSet.setFillBefore(true); //// 停留在第一帧
    animationSet.setFillAfter(true); // 停留在最后一帧

    // 透明度
        /*
                AlphaAnimation alphaAnimation = alphaAnimation(interpolator,((int) Math.abs(duration*0.8)),0, 1);
		animationSet.addAnimation(alphaAnimation);
        */

    // 放大缩小
    ScaleAnimation scaleAnimation =
        new ScaleAnimation(1.2f, 0.93f, 1.2f, 0.93f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                           0.5f);
    scaleAnimation.setInterpolator(interpolator);
    scaleAnimation.setDuration(duration);
    //scaleAnimation.setFillAfter(true);
    scaleAnimation.setFillAfter(true);
    return scaleAnimation;
  }

  /**
   * 旋转动画
   */
  public RotateAnimation rotateAnimation(Interpolator interpolator, int duration, float fromDegrees, float toDegrees,
                                         int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
    RotateAnimation rotateAnimation =
        new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
    rotateAnimation.setInterpolator(interpolator);
    rotateAnimation.setDuration(duration);
    rotateAnimation.setFillAfter(true);
    return rotateAnimation;
  }

  /**
   * 自定义插值
   */
  public float getInterpolation(float input) {
    return (float) (Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
  }

  private void onPlayStart(Animation animation) {
    if (mPlayListener != null) {
      mPlayListener.onPlayStart(animation);
    }
  }

  private void onPlayEnd(Animation animation) {
    if (mPlayListener != null) {
      mPlayListener.onPlayEnd(animation);
    }
  }

  private void onPlayRepeat(Animation animation) {
    if (mPlayListener != null) {
      mPlayListener.onPlayRepeat(animation);
    }
  }

  /**
   * tip提示
   */
  public AnimationSet tipAnimation(float fromAlpha, float toAlpha, float fromXDelta, float toXDelta, float fromYDelta,
                                   float toYDelta) {
    return tipAnimation(new AccelerateInterpolator(), 500, fromAlpha, toAlpha, fromXDelta, toXDelta, fromYDelta,
                        toYDelta);
  }

  /**
   * tip提示(透明度滑动动画)
   */
  public AnimationSet tipAnimation(Interpolator interpolator, int duration, float fromAlpha, float toAlpha,
                                   float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
    AnimationSet animationSet = new AnimationSet(true);
    //animationSet.setStartOffset(interpolator);
    animationSet.setInterpolator(interpolator);
    //animationSet.setFillBefore(true); //// 停留在第一帧
    animationSet.setFillAfter(true); // 停留在最后一帧

    // 透明度
    AlphaAnimation alphaAnimation = alphaAnimation(interpolator, ((int) Math.abs(duration * 0.8)), fromAlpha, toAlpha);
    animationSet.addAnimation(alphaAnimation);

    // 移动
    TranslateAnimation translateAnimation =
        translateAnimation(interpolator, duration, fromXDelta, toXDelta, fromYDelta, toYDelta);
    animationSet.addAnimation(translateAnimation);
    return animationSet;
  }

  /**
   * 动画方向
   * to_left: 从右到左显示,hideDelay后从左到右隐藏;
   * to_right: 从左到右显示,hideDelay后从右到左隐藏;
   * to_top: 从下到显示,hideDelay后从上到下隐藏;
   * to_bottom: 从上到下显示,hideDelay后从下到上隐藏;
   */
  public enum Direction {
    to_left, to_right, to_top, to_bottom
  }

  /**
   * 动画接口
   */
  public interface PlayListener {

    void onPlayStart(Animation animation);

    void onPlayRepeat(Animation animation);

    void onPlayEnd(Animation animation);
  }

}
