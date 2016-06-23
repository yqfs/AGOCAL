package com.taobao.uikit.feature.features;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.view.TListView;

/**
 * PageNumberFeature is that show Page number when ListView is scrolling.
 * 
 * The page size can be set by calling {@link #setPageSize(int) setPageSize}
 * 
 * The total count in the ListView can be set by calling
 * {@link #setTotalCount(int) setTotalCount}
 * 
 * @author jiajing
 * 
 */
public class PageNumberFeature extends AbsFeature<ListView> implements
		OnScrollListener, MeasureCallback, LayoutCallback, CanvasCallback {

	public static final int SHOW_ALWAYS = 100;
	public static final int SHOW_SCROLLING = 101;
	public static final int SHOW_NONE = 102;

	private static final int PAGETIP_MARGIN_RIGHT = 25;
	private static final int PAGETIP_MARGIN_BOTTOM = 20;

	private PageTip mPageTip;
	private int mPageSize;
	private int mCurrentPage;
	private int mTotalCount;
	private int mLastVisibleItemIndex;
	private int mScrollState = SCROLL_STATE_IDLE;
	private int mShowType = SHOW_SCROLLING;

	private void init() {
		mPageTip = new PageTip(this.getHost().getContext());
		this.getHost().setOnScrollListener(this);
	}

	public void setHost(ListView host) {
		super.setHost(host);
		init();
	}

	public int getPageSize() {
		return mPageSize;
	}

	/** set show type 
	 * @param showType Pass SHOW_ALWAYS,SHOW_SCROLLING or SHOW_NONE. Default
     * value is SHOW_SCROLLING.
	 */
	public void setShowType(int showType) {
		mShowType = showType;
	}

	public void setPageSize(int pageSize) {
		this.mPageSize = pageSize;
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void setTotalCount(int totalCount) {
		this.mTotalCount = totalCount;
	}

	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mScrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mLastVisibleItemIndex != this.getHost().getLastVisiblePosition()) {
			mLastVisibleItemIndex = this.getHost().getLastVisiblePosition();

			mPageTip.updatePageIndex(mLastVisibleItemIndex
					- this.getHost().getHeaderViewsCount(), mPageSize,
					mTotalCount, firstVisibleItem);
		}

	}

	@Override
	public void beforeOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		((TListView) this.getHost()).measureChild(mPageTip, widthMeasureSpec,
				heightMeasureSpec, 0);
	}

	@Override
	public void beforeOnLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterOnLayout(boolean changed, int left, int top, int right,
			int bottom) {

		mPageTip.layout(this.getHost().getWidth() - mPageTip.getMeasuredWidth()
				- mPageTip.mMarginRight,
				this.getHost().getHeight() - mPageTip.getMeasuredHeight()
						- mPageTip.mMarginBottom, this.getHost().getWidth()
						- mPageTip.mMarginRight, this.getHost().getHeight()
						- mPageTip.mMarginBottom);

	}

	@Override
	public void beforeDraw(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDraw(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeDispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	private boolean showPageTip() {
		if (mShowType == SHOW_SCROLLING) {
			return mScrollState != OnScrollListener.SCROLL_STATE_IDLE;
		} else return mShowType == SHOW_ALWAYS;
	}

	@Override
	public void afterDispatchDraw(Canvas canvas) {
		if (showPageTip()) {
			((TListView) this.getHost()).drawChild(canvas, mPageTip, this
					.getHost().getDrawingTime(), 0);
		}
	}

	@Override
	public void beforeOnDraw(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterOnDraw(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	public class PageTip extends TextView {
		int mMarginRight;
		int mMarginBottom;

		public PageTip(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public PageTip(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public PageTip(Context context) {
			super(context);
			init();
		}

		private void init() {
			float density = this.getContext().getResources()
					.getDisplayMetrics().density;

			setPadding((int) (5 * density), 0, (int) (5 * density), 0);
			setTextSize(17);
			setBackgroundColor(Color.GRAY);
			setGravity(Gravity.CENTER);
			setText("0/0");

			setTextColor(Color.WHITE);

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(params);

			DisplayMetrics dm = new DisplayMetrics();
			dm = this.getContext().getResources().getDisplayMetrics();

			mMarginRight = (int) (PAGETIP_MARGIN_RIGHT * dm.density);
			mMarginBottom = (int) (PAGETIP_MARGIN_BOTTOM * dm.density);
		}

		void updatePageIndex(int currentItemCountIndex, int pageSize,
				int totalCount, int realFirstVisiblePosition) {
			if(pageSize == 0){
				return;
			}
			
			int currentPageIndex;
			if (realFirstVisiblePosition == 0)
				currentPageIndex = 1;
			else
				currentPageIndex = currentItemCountIndex / pageSize + 1;
			
			int totalPageCount = (totalCount + pageSize - 1) / pageSize;
			
			if(currentPageIndex > totalPageCount){
				currentPageIndex = totalPageCount; 
			}
			String pageTipStr = "";
			if (totalCount > 0) {
				pageTipStr = String.format("%s/%s", currentPageIndex,
						totalPageCount);
			} else
				pageTipStr = String.format("%s", currentPageIndex);
			
			mCurrentPage = currentPageIndex;
			
			if (!TextUtils.equals(getText(), pageTipStr)) {
				setText(pageTipStr);
				measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				layout(getRight() - getMeasuredWidth(), getBottom()
						- getMeasuredHeight(), getRight(), getBottom());
			}
		}
	}

}
