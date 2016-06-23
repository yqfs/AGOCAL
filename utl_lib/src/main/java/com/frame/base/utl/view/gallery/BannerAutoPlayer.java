package com.frame.base.utl.view.gallery;

import android.os.Handler;
import android.os.Looper;

/**
 * A player who can play a @Playable object. It can play next till end and play previous till head.
 * <p/>
 * Once it go to the last element, it can play by the reverse order or jump to the first and play again.
 * <p/>
 * Between each frame, there is a pause, you can call `setTimeInterval()` to set the time interval you want.
 *
 * @author YANGQIYUN
 */
public class BannerAutoPlayer {

  /**
   * Define how an object can be auto-playable.
   */
  public interface Playable {

    void playTo(int to);

    void playNext();

    void playPrevious();

    int getTotal();

    int getCurrent();
  }

  /**
   * two play direction
   */
  public enum PlayDirection {
    to_left, to_right
  }

  /**
   * two play recycle mode
   */
  public enum PlayRecycleMode {
    repeat_from_start, play_back
  }

  private PlayDirection mDirection = PlayDirection.to_right;
  private PlayRecycleMode mPlayRecycleMode = PlayRecycleMode.repeat_from_start;
  // time period of auto-play
  private int mTimeInterval = 5000;
  private Playable mPlayable;
  // timer task
  private Runnable mTimerTask;
  // handler to run the timer task
  private Handler mHandler = new Handler(Looper.myLooper());
  // total num of playable object
  private int mTotal;
  // a tag that defines the playable object should skip the auto-play of current page and directly play the next frame or not
  private boolean mSkipCurrentAndGoToNext = false;
  // a tag that defines the playable object is playing or not
  private boolean mPlaying = false;
  // a tag that defines the playable object is pausing or not
  private boolean mPaused = false;
  // a tag that defines the playable object will auto play next frame after certain period or not
  private boolean mIsAutoPlay;
  // a tag that defines the playable object can play cirularly or not
  private boolean mIsRecycleShow;

  public BannerAutoPlayer(Playable playable) {
    mPlayable = playable;
  }

  public void play() {
    play(0, PlayDirection.to_right);
  }

  public void play(int start) {
    play(start, PlayDirection.to_right);
  }

  public void play(int start, PlayDirection direction) {
    if (mPlaying) {
      return;
    }

    mTotal = mPlayable.getTotal();
    if (mTotal <= 1) {
      return;
    }

    mPlaying = true;
    playTo(start);
    // if auto-play is on, post the same runnable periodically
    if (mIsAutoPlay) {
      mTimerTask = new Runnable() {
        @Override
        public void run() {
          if (!mPaused) {
            playNextFrame();
          }
          if (mPlaying && mHandler != null && mTimerTask != null) {
            mHandler.postDelayed(mTimerTask, mTimeInterval);
          }
        }
      };
      if (mHandler != null && mTimerTask != null) {
        mHandler.postDelayed(mTimerTask, mTimeInterval);
      }
    }
  }

  private void playNextFrame() {
    // 当前页是直接滑动到达的话，自动播放到达的任务将会取消
    if (mSkipCurrentAndGoToNext) {
      mSkipCurrentAndGoToNext = false;
      return;
    }
    int current = mPlayable.getCurrent();
    if (mDirection == PlayDirection.to_right) {
      if (current == mTotal - 1) {
        // 如果不是循环显示，则显示到最后一页时就停止播放
        if (!mIsRecycleShow) {
          return;
        }
        if (mPlayRecycleMode == PlayRecycleMode.play_back) {
          mDirection = PlayDirection.to_left;
          playNextFrame();
        } else {
          playTo(0);
        }
      } else {
        playNext();
      }
    } else {
      if (current == 0) {
        if (mPlayRecycleMode == PlayRecycleMode.play_back) {
          mDirection = PlayDirection.to_right;
          playNextFrame();
        } else {
          playTo(mTotal - 1);
        }
      } else {
        playPrevious();
      }
    }
  }

  private void playTo(int to) {
    mPlayable.playTo(to);
  }

  private void playNext() {
    mPlayable.playNext();
  }

  private void playPrevious() {
    mPlayable.playPrevious();
  }

  public void skipCurrentAndGoToNext() {
    mSkipCurrentAndGoToNext = true;
  }

  public void stop() {
    if (!mPlaying) {
      return;
    }

    mPlaying = false;
  }

  public void stopPlay() {
    mPlaying = false;
    if (mTimerTask != null) {
      mHandler.removeCallbacks(mTimerTask);
    }
  }

  /** when user's fingers touch down the view pager, the player object stops playing */
  public void pause() {
    mPaused = true;
  }

  /** when user's fingers touches up the view pager, the player object resume playing */
  public void resume() {
    mPaused = false;
  }

  public BannerAutoPlayer setTimeInterval(int timeInterval) {
    mTimeInterval = timeInterval;
    return this;
  }

  public BannerAutoPlayer setPlayRecycleMode(PlayRecycleMode playRecycleMode) {
    mPlayRecycleMode = playRecycleMode;
    return this;
  }

  public BannerAutoPlayer setAutoPlayState(boolean isAutoPlay) {
    mIsAutoPlay = isAutoPlay;
    return this;
  }

  public BannerAutoPlayer setRecycleShowState(boolean isRecycleShow) {
    mIsRecycleShow = isRecycleShow;
    return this;
  }

  public void onDestroy() {
    mTimerTask = null;
    // 当参数为 null 的时候，可以清除掉所有跟次 handler 相关的 Runnable 和 Message
    mHandler.removeCallbacksAndMessages(null);
    mHandler = null;
  }

}
