package com.taobao.uikit.feature.features;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.FocusCallback;
import com.taobao.uikit.feature.callback.InterceptTouchEventCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.view.ViewGroupHelper;

/**
 * 
 * The feature give us the function of the header view pinned like IOS.
 * 
 * The pinned head view can be set by calling {@link #setPinnedHeaderView(View)
 * setPinnedHeaderView()}.
 * 
 * SectionAdapter sets the section data behind the ListView
 * 
 * The sectionAdapter can be set by calling
 * {@link #setSectionAdapter(SectionAdapter) setSectionAdapter()}.
 * 
 * @author jiajing
 */
public class PinnedHeaderFeature extends AbsFeature<ListView> implements
		MeasureCallback, InterceptTouchEventCallback, LayoutCallback, CanvasCallback,FocusCallback,OnScrollListener {
	private static final String TAG = "PinnedHeaderFeature";
	private static final boolean DBG = false;

	private static final int MAX_ALPHA = 255;
	private SectionAdapter mAdapter;
	private PinnedHeaderAdapterInternal mInternalAdapter;
	private View mHeaderView;
	private boolean mHeaderViewVisible = false;

	private int mHeaderViewWidth;

	private int mHeaderViewHeight;

	private int mWidthMeasureSpec;
	private int mHeightMeasureSpec;
	private boolean mLastHeaderDown = false;
	//private int[] mPositions;
	
	public interface SectionAdapter {
		
	    /** the position belong to which section
	     * @param position
	     * @return section position in the list view
	     */
	    int getSection(int position);
	    
		/** first section position in the list view
		 * @return first section position
		 */
		int firstSection();
		
		/** callback to bind section to pinned header view 
		 * @param header 
		 * @param position
		 */
		void bindSection(View header, int position);
	}
	
	/**
	 * Adapter interface. The list adapter must implement this interface.
	 */
	 interface PinnedHeaderAdapterInternal {

		/**
		 * Pinned header state: don't show the header.
		 */
		int PINNED_HEADER_GONE = 0;

		/**
		 * Pinned header state: show the header at the top of the list.
		 */
		int PINNED_HEADER_VISIBLE = 1;

		/**
		 * Pinned header state: show the header. If the header extends beyond
		 * the bottom of the first shown element, push it up and clip.
		 */
		int PINNED_HEADER_PUSHED_UP = 2;

		/**
		 * Computes the desired state of the pinned header for the given
		 * position of the first visible list item. Allowed return values are
		 * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or
		 * {@link #PINNED_HEADER_PUSHED_UP}.
		 */
		int getPinnedHeaderState(int position);

		/**
		 * Configures the pinned header view to match the first visible list
		 * item.
		 * 
		 * @param header
		 *            pinned header view.
		 * @param position
		 *            position of the first visible list item.
		 * @param alpha
		 *            fading of the header view, between 0 and 255.
		 */
		void configurePinnedHeader(View header, int position, int alpha);
	}

	

	@Override
	public void setHost(ListView host) {
		// TODO Auto-generated method stub
		super.setHost(host);
		this.getHost().setFadingEdgeLength(0);
		this.getHost().setOnScrollListener(this);
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean ret = false;
		if (mHeaderViewVisible) {
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			final Rect frame = new Rect(0, 0, mHeaderViewWidth,
					mHeaderView.getBottom());
			boolean isInHeaderArea = frame.contains(x, y);

			if (isInHeaderArea) {
				ret = mHeaderView.dispatchTouchEvent(event);
				this.getHost().invalidate(frame);
			} else if (mLastHeaderDown) {
				mHeaderView.dispatchWindowFocusChanged(false);
				mHeaderView.setPressed(false);
				this.getHost().invalidate(frame);
				mLastHeaderDown = false;

			}

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mLastHeaderDown = isInHeaderArea;
			}
		}
		return ret;
	}


	@Override
	public void beforeOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mHeaderView != null) {
			if (this.getHost() instanceof ViewGroupHelper) {
				((ViewGroupHelper) this.getHost()).measureChild(mHeaderView,
						widthMeasureSpec, heightMeasureSpec, 0);
			}
			mWidthMeasureSpec = widthMeasureSpec;
			mHeightMeasureSpec = heightMeasureSpec;
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}

	}

	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub

	}

	/** set the custom pinned header view
	 * @param view
	 */
	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		// Disable vertical fading when the pinned header is present
		// TODO change ListView to allow separate measures for top and bottom
		// fading edge;
		// in this particular case we would like to disable the top, but not the
		// bottom edge.
		if (mHeaderView != null && this.getHost() != null) {
			this.getHost().setFadingEdgeLength(0);
		}

	}
	
	
	/** set the section adapter
	 * @param adapter
	 */
	public void setSectionAdapter(SectionAdapter adapter){
		this.mAdapter = adapter;
		if(mAdapter != null){
			mInternalAdapter = new PinnedHeaderAdapterInternal(){
				@Override
				public int getPinnedHeaderState(int position) {
					if (position < 0 ) {
						return PINNED_HEADER_GONE;
					}
					if (position >= PinnedHeaderFeature.this.getHost().getCount()){
						return PINNED_HEADER_GONE;
					}
					
					if(mAdapter.firstSection() > position){
						return PINNED_HEADER_GONE;
					}
					
					if (mAdapter.getSection(position + 1) == position + 1 && mAdapter.firstSection() != position + 1) {
						return PINNED_HEADER_PUSHED_UP;
					}
					return PINNED_HEADER_VISIBLE;
					
				}

				@Override
				public void configurePinnedHeader(View header, int position,
						int alpha) {
					{		
						mAdapter.bindSection(header, mAdapter.getSection(position));
					}
				}
				
			};
		}
	}
	private void measureHeadView() {
		if (mHeaderView != null) {
			if (this.getHost() instanceof ViewGroupHelper) {
				((ViewGroupHelper) this.getHost()).measureChild(mHeaderView,
						mWidthMeasureSpec, mHeightMeasureSpec, 0);
			}
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	private void configureHeaderView(int position) {
		position = (position - 1) >= 0 ? (position - this.getHost().getHeaderViewsCount()) : 0;
		if (mHeaderView == null) {
			return;
		}

		int state = mInternalAdapter.getPinnedHeaderState(position);
		if (DBG) {
			Log.d(TAG, "position = " + position);
			Log.d(TAG, "state = " + state);
		}
		switch (state) {
		case PinnedHeaderAdapterInternal.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case PinnedHeaderAdapterInternal.PINNED_HEADER_VISIBLE: {
			mInternalAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
			measureHeadView();
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			mHeaderViewVisible = true;
			break;
		}

		case PinnedHeaderAdapterInternal.PINNED_HEADER_PUSHED_UP: {
			View firstView = this.getHost().getChildAt(0);
			if (firstView == null)
				break;
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (DBG) {
				Log.d(TAG, "getBottom = " + firstView.getBottom());
				Log.d(TAG, "headerHeight = " + mHeaderView.getHeight());
				Log.d(TAG, "downrefreshHeight = "
						+ this.getHost().getChildAt(0).getHeight());
			}

			if (bottom < headerHeight && headerHeight > 0) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			mInternalAdapter.configurePinnedHeader(mHeaderView, position, alpha);
			measureHeadView();
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);

			}
			mHeaderViewVisible = true;
			break;
		}
		}
	}

	@Override
	public void beforeOnLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterOnLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (mHeaderView != null) {
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			configureHeaderView(this.getHost().getFirstVisiblePosition());
		}

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

	@Override
	public void afterDispatchDraw(Canvas canvas) {
		/*if (this.getHost().getFirstVisiblePosition() == 0) {
			mHeaderViewVisible = false;
		}*/
		if (mHeaderViewVisible) {
			if (this.getHost() instanceof ViewGroupHelper) {
				((ViewGroupHelper) this.getHost()).drawChild(canvas,
						mHeaderView, this.getHost().getDrawingTime(), 0);
			}

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

	@Override
	public void beforeOnFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterOnFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeOnWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterOnWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		if(!hasWindowFocus){
			if(mHeaderView != null)
				mHeaderView.dispatchWindowFocusChanged(false);
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(mHeaderView != null){
			this.configureHeaderView(firstVisibleItem);
		}
	
		
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
