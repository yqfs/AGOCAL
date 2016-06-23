package com.frame.base.utl.view.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.frame.base.utl.R;


/**
 * 图片 banner 展示容器，带轮播功能
 *
 * @author YANGQIYUN on 15/10/12.
 */
public class SliderBanner extends RelativeLayout {

  /** view pager 的资源 id */
  protected int mResIdForViewPager;
  /** 指示布局的资源 id */
  protected int mResIdForIndicator;
  /** 如果开启自动播放的话，切换视图的周期 */
  protected int mTimeInterval = 2000;
  /** view pager */
  private ViewPager mViewPager;
  /** 指示布局，包含轮播图片数量的小圆点 */
  private IPagerIndicator mPagerIndicator;
  /** banner 数据适配器 */
  private BannerAdapter mBannerAdapter;
  /** 是否循环显示，默认是循环显示的 */
  private boolean isRecycleShow = true;
  /** 是否自动播放，默认不自动播放 */
  private boolean isAutoPlay = false;

  private BannerAutoPlayer mAutoPlayer;

  private ViewPager.OnPageChangeListener mOnPageChangeListener;
  private OnTouchListener mViewPagerOnTouchListener;
  private BannerAutoPlayer.Playable mGalleryPlayable = new BannerAutoPlayer.Playable() {

    @Override
    public void playTo(int to) {
      mViewPager.setCurrentItem(to, true);
    }

    @Override
    public void playNext() {
      mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    @Override
    public void playPrevious() {
      mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
    }

    @Override
    public int getTotal() {
      return mBannerAdapter.getCount();
    }

    @Override
    public int getCurrent() {
      return mViewPager.getCurrentItem();
    }
  };

  public SliderBanner(Context context) {
    this(context, null);
  }

  public SliderBanner(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SliderBanner, 0, 0);
    mResIdForViewPager = arr.getResourceId(R.styleable.SliderBanner_my_slider_banner_pager, 0);
    mResIdForIndicator = arr.getResourceId(R.styleable.SliderBanner_my_slider_banner_indicator, 0);
    mTimeInterval = arr.getInt(R.styleable.SliderBanner_my_slider_banner_time_interval, mTimeInterval);
    isRecycleShow = arr.getBoolean(R.styleable.SliderBanner_my_slider_banner_recycle_show, isRecycleShow);
    isAutoPlay = arr.getBoolean(R.styleable.SliderBanner_my_slider_banner_auto_play, isAutoPlay);
    arr.recycle();
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    int action = ev.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        if (mAutoPlayer != null) {
          mAutoPlayer.pause();
        }
        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        if (mAutoPlayer != null) {
          mAutoPlayer.resume();
        }
        break;
      default:
        break;
    }
    if (mViewPagerOnTouchListener != null) {
      mViewPagerOnTouchListener.onTouch(this, ev);
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mViewPager = (ViewPager) findViewById(mResIdForViewPager);
    mPagerIndicator = (IndicatorContainer) findViewById(mResIdForIndicator);
    mAutoPlayer = new BannerAutoPlayer(mGalleryPlayable).setAutoPlayState(isAutoPlay).setRecycleShowState(isRecycleShow)
        .setTimeInterval(mTimeInterval);
    mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i2) {
        if (mOnPageChangeListener != null) {
          mOnPageChangeListener.onPageScrolled(i, v, i2);
        }
      }

      @Override
      public void onPageSelected(int position) {
        if (mPagerIndicator != null) {
          mPagerIndicator.setSelected(mBannerAdapter.getPositionForIndicator(position));
        }
        mAutoPlayer.skipCurrentAndGoToNext();

        if (mOnPageChangeListener != null) {
          mOnPageChangeListener.onPageSelected(position);
        }
      }

      @Override
      public void onPageScrollStateChanged(int i) {
        if (mOnPageChangeListener != null) {
          mOnPageChangeListener.onPageScrollStateChanged(i);
        }
      }
    });
  }

  /**
   * 终止自动切换
   */
  public void stopPlay() {
    mAutoPlayer.stopPlay();
  }

  /**
   * 开始自动切换
   */
  public void beginPlay() {
    mPagerIndicator.setSelected(0);
    mAutoPlayer.play();
  }

  public void setTimeInterval(int interval) {
    mAutoPlayer.setTimeInterval(interval);
  }

  public void setAdapter(BannerAdapter adapter) {
    adapter.setRecycleShowState(isRecycleShow);
    mViewPager.setAdapter(adapter);
    mBannerAdapter = adapter;
  }

  public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
    mOnPageChangeListener = listener;
  }

  public void setDotNum(int num) {
    if (mPagerIndicator != null) {
      mPagerIndicator.setNum(num);
    }
  }

  public void setViewPagerOnTouchListener(OnTouchListener onTouchListener) {
    mViewPagerOnTouchListener = onTouchListener;
  }

  public void setAutoPlayState(boolean isAutoPlay) {
    this.isAutoPlay = isAutoPlay;
  }

  public void setRecycleShowState(boolean isRecycleShow) {
    this.isRecycleShow = isRecycleShow;
  }

  /**
   * 如果允许自动播放，则需要手动销毁 BannerAutoPlayer
   */
  public void onDestroy() {
    mAutoPlayer.onDestroy();
  }
}
