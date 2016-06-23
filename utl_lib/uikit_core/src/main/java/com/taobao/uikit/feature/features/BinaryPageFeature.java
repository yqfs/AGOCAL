/**
 * 
 */
package com.taobao.uikit.feature.features;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.taobao.uikit.feature.callback.TouchEventCallback;

/**
 * 
 * The feature separate the list view into two page,and at the border of the
 * pages the behaviour of the list view like the view pager. when pull the
 * bottom page up or down by a certain distance, the page will auto scroll to
 * bottom or top.
 * 
 * the distance of the touch offset can be set by calling
 * {@link #setTouchOffsetLimit(float) setTouchOffsetLimit()}. the distance of
 * the first page bottom off set can be set by calling
 * {@link #setFirstPageBottomOffset(int) setFirstPageBottomOffset()}. the auto
 * scroll duration can be set by calling {@link #setAnimationDuration(int)
 * setAnimationDuration()}. OnPageChangedListener which callback of the page
 * changed can be set by calling
 * {@link #setOnPageChangedListener(OnPageChangedListener)
 * setOnPageChangedListener()}
 * 
 * setFirstPageBottomOffset
 * 
 * @author jiajing
 */
public class BinaryPageFeature extends AbsFeature<ListView> implements
		OnScrollListener, TouchEventCallback {

	public static final String ACTION_PERCENT = "Home.PagerDivider.ACTION_PERCENT";
	public static final String EXTRA_PERCENT = "EXTRA_PERCENT";
	public static final String EXTRA_HALF = "EXTRA_HALF";
	public static final String EXTRA_TOUCH = "EXTRA_TOUCH";

	/**
	 * the page changed callback
	 * 
	 * @author jiajing
	 * 
	 */
	public interface OnPageChangedListener {
		void onPageSelected(BinaryPageFeature binaryPage, Page page);
	}

	private int mPagePosition;
	private int mScrollState;
	private Page mTouchPage = Page.NONE;
	private PageState mPageState = PageState.Wait;
	private int mFirstPageBottomOffset;
	private int mAnimationDuration = 600;
	private TouchOffset mTouchOffset;
	private OnPageChangedListener mOnPageChangedListener;

	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {
		this.mTouchOffset = new TouchOffset();
	}

	@Override
	public void setHost(ListView host) {
		super.setHost(host);
		init();
	}

	private void init() {		
		mHost.setOnScrollListener(this);
	}

	/**
	 * set the page position
	 * 
	 * @param pagePosition
	 */
	public void setPagePosition(int pagePosition) {
		mPagePosition = pagePosition;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mScrollState = scrollState;
		computeScroll();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		computeScroll();
	}

	private boolean onTouch(View v, MotionEvent event) {
		if (v == mHost) {
			if (event.getAction() == MotionEvent.ACTION_DOWN
					|| event.getAction() == MotionEvent.ACTION_MOVE) {
				mPageState = PageState.Wait;
				int lastPosition = mHost.getLastVisiblePosition();
				int firstPosition = mHost.getFirstVisiblePosition();
				if (mPagePosition < firstPosition
						|| mPagePosition >= lastPosition) {
					mTouchOffset.reset();
					return false;
				}
				int index = mPagePosition - firstPosition;
				View dividerView = mHost.getChildAt(index);
				if (!mTouchOffset.isRecord()) {
					float dividerTop = dividerView.getTop();
					float totalHeight = mHost.getHeight();
					float percent = dividerTop / totalHeight;
					float half = (1.0f - ((float) dividerView.getHeight() + mFirstPageBottomOffset)
							/ mHost.getHeight()) / 2.0f;
					Page page = Page.NONE;
					if (percent > half) {
						page = Page.FIRST;
					} else {
						page = Page.LAST;
					}
					mTouchOffset.startRecord(event.getRawY(), page);
					return false;
				} else {
					mTouchOffset.setEndY(event.getRawY());
				}
				scrollLikePullToFresh(dividerView, true);
			}

		}
		return false;
	}

	private void sendLocalBroadcastManager(float percent, float half,
			boolean touch) {
		/*
		 * LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
		 * .getInstance(mHost.getContext()); if (localBroadcastManager != null)
		 * { Intent intent = new Intent(ACTION_PERCENT);
		 * intent.putExtra(EXTRA_PERCENT, percent); intent.putExtra(EXTRA_HALF,
		 * half); intent.putExtra(EXTRA_TOUCH, touch);
		 * localBroadcastManager.sendBroadcast(intent); intent = null; }
		 */
	}

	/**
	 * http://stackoverflow.com/questions/15338509/android-listview-stop-
	 * scrolling-at-whole-row-position
	 * */
	private void computeScroll() {
		if (mPageState == PageState.Asjusting) {
			return;
		}
		int lastPosition = mHost.getLastVisiblePosition();
		int firstPosition = mHost.getFirstVisiblePosition();
		if (mScrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			if (mPagePosition > lastPosition) { // 当前页面
				mTouchPage = Page.FIRST;
			} else if (mPagePosition < firstPosition) {
				mTouchPage = Page.LAST;
			} else {
				mTouchPage = Page.NONE;
			}
			mPageState = PageState.Wait;
			return;
		}

		if (mPagePosition < firstPosition || mPagePosition >= lastPosition) {
			return;
		}
		if (mPageState == PageState.Wait) {
			mPageState = PageState.Asjusting;
			MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(), MotionEvent.ACTION_OUTSIDE, 0,
					0, 0);
			mHost.dispatchTouchEvent(event);
			mHost.onTouchEvent(event);
			int index = mPagePosition - firstPosition;
			View dividerView = mHost.getChildAt(index);
			if (mTouchPage == Page.NONE) {
				// scrollLikeViewPager(dividerView);
				if (mTouchOffset.getCurrentPage() != mTouchOffset
						.getStartPage()) {
					if (mOnPageChangedListener != null) {
						mOnPageChangedListener.onPageSelected(this,
								mTouchOffset.getCurrentPage());
					}
				}
				if (mTouchOffset.getCurrentPage() == Page.FIRST
						&& mTouchOffset.getStartPage() == Page.FIRST
						&& mTouchOffset.getOffset() > 0) {
					return;
				}
				scrollLikePullToFresh(dividerView, false);
			} else if (mTouchPage == Page.FIRST) {
				scrollToFirstPageBottom(dividerView);
				mTouchPage = Page.NONE;
			} else if (mTouchPage == Page.LAST) {
				scrollToLastPageTop(dividerView);
				mTouchPage = Page.NONE;
			}
			mTouchOffset.reset();
			event.recycle();
		}
	}

	private void scrollLikePullToFresh(View dividerView, boolean inTouch) {
		if (mTouchOffset.getOffset() == 0) {
			return;
		}
		if (mTouchOffset.getCurrentPage() == Page.FIRST) {
			if (inTouch) {
				sendLocalBroadcastManager(1.0f, 0.5f, true);
			} else {
				sendLocalBroadcastManager(1.0f, 0.5f, false);
				scrollToFirstPageBottom(dividerView);
			}
			return;
		}

		if (mTouchOffset.getCurrentPage() == Page.LAST) {
			if (inTouch) {
				sendLocalBroadcastManager(0.1f, 0.5f, true);
			} else {
				scrollToLastPageTop(dividerView);
				sendLocalBroadcastManager(0.1f, 0.5f, false);
			}
			return;
		}
	}

	private void scrollToLastPageTop(View dividerView) {
		float offset = dividerView.getBottom();
		final int duration = (int) ((offset * mAnimationDuration) / mHost
				.getHeight());
		mHost.post(new Runnable() {
			@Override
			public void run() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mHost.smoothScrollToPositionFromTop(mPagePosition + 1, 0,
							duration);
					mHost.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mPageState == PageState.Asjusting) {
								mPageState = PageState.Complete;
							}
						}
					}, duration);
				} else {
					mHost.setSelectionFromTop(mPagePosition + 1, 0);
					if (mPageState == PageState.Asjusting) {
						mPageState = PageState.Complete;
					}
				}

			}
		});
	}

	private void scrollToFirstPageBottom(View dividerView) {
		float actualBottom = dividerView.getBottom() + mFirstPageBottomOffset;
		if (actualBottom == dividerView.getHeight()) {
			return;
		}
		float offset = Math.abs(actualBottom - mHost.getHeight());
		final int duration = (int) ((offset * mAnimationDuration) / mHost
				.getHeight());
		final int offsetFromTop = mHost.getHeight() - dividerView.getHeight()
				- mFirstPageBottomOffset;
		mHost.post(new Runnable() {
			@Override
			public void run() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mHost.smoothScrollToPositionFromTop(mPagePosition,
							offsetFromTop, duration);
					mHost.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mPageState == PageState.Asjusting) {
								mPageState = PageState.Complete;
							}
						}
					}, duration);
				} else {
					mHost.setSelectionFromTop(mPagePosition, offsetFromTop);
					if (mPageState == PageState.Asjusting) {
						mPageState = PageState.Complete;
					}
				}

			}
		});
	}

	/**set firstPageBottomOffset
	 * @param firstPageBottomOffset
	 */
	public void setFirstPageBottomOffset(int firstPageBottomOffset) {
		this.mFirstPageBottomOffset = firstPageBottomOffset;
	}

	/** set the animationDuration
	 * @param animationDuration
	 */
	public void setAnimationDuration(int animationDuration) {
		this.mAnimationDuration = animationDuration;
	}

	/** set touchOffsetLimit
	 * @param touchOffsetLimit
	 */
	public void setTouchOffsetLimit(float touchOffsetLimit) {
		mTouchOffset.setOffsetLimit(touchOffsetLimit);
	}

	/** set OnPageChangedListener
	 * @param pageChangedListener
	 */
	public void setOnPageChangedListener(
			OnPageChangedListener pageChangedListener) {
		this.mOnPageChangedListener = pageChangedListener;
	}

	public enum PageState {
		Wait(0, "待调整"), Asjusting(1, "调整中"), Complete(2, "系统完成");
		private final int value;
		private final String desc;

		PageState(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}
	}

	enum Page {
		NONE(-1, "分界处"), FIRST(0, "第一页"), LAST(1, "第二页");
		private final int value;
		private final String desc;

		Page(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}
	}

	class TouchOffset {

		private boolean record = false;
		private float startY;
		private Page startPage = Page.NONE;
		private float endY;
		private float offsetLimit = 160;

		public void startRecord(float startY, Page startPage) {
			this.record = true;
			this.startY = startY;
			this.startPage = startPage;
		}

		public void setEndY(float endY) {
			this.endY = endY;
		}

		public Page getStartPage() {
			return startPage;
		}

		public Page getCurrentPage() {
			float offset = getOffset();

			if (Math.abs(offset) <= offsetLimit) {
				return startPage;
			}
			if (offset > offsetLimit) {
				return Page.FIRST;
			} else {
				return Page.LAST;
			}
		}

		public boolean isUpDown() {
			return getOffset() < 0;
		}

		public boolean isUp() {
			return getOffset() < 0;
		}

		public boolean isDown() {
			return getOffset() >= 0;
		}

		public float getOffset() {
			if (record) {
				float offset = endY - startY;
				return offset;
			} else {
				return 0;
			}
		}

		public float getOffsetLimit() {
			return offsetLimit;
		}

		public void setOffsetLimit(float offsetLimit) {
			this.offsetLimit = offsetLimit;
		}

		public boolean isRecord() {
			return record;
		}

		public void setRecord(boolean record) {
			this.record = record;
		}

		public void reset() {
			record = false;
			startPage = Page.NONE;
		}
	}

	@Override
	public void afterOnTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// onTouch(mHost, event);
	}

	@Override
	public void beforeDispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeOnTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		onTouch(mHost, event);
	}
}
