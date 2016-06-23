package com.taobao.uikit.feature.view;

import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.FocusCallback;
import com.taobao.uikit.feature.callback.InterceptTouchEventCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.callback.RecyclerAdapterCallback;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.utils.FeatureList;
import com.taobao.uikit.utils.IFeatureList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * TRecyclerView: UIKit's custom RecyclerView
 *
 * @author yanpei
 * @author modified by WilliamChik
 */
public class TRecyclerView extends RecyclerView implements ViewHelper, ViewGroupHelper, IFeatureList<RecyclerView> {

  public static final int ITEM_VIEW_TYPE_MASK = 0x8000;

  public static final int ITEM_POSITION_MASK = 0x7fff;

  private FeatureList<RecyclerView> mFeatureList = new FeatureList<RecyclerView>(this);

  private List<OnScrollListener> mOnScrollListeners = new ArrayList<OnScrollListener>();

  private ArrayList<View> mHeaderViews = new ArrayList<View>();

  private ArrayList<View> mFooterViews = new ArrayList<View>();

  public TRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    super.setOnScrollListener(new InnerOnScrollListener());
    mFeatureList.init(context, attrs, defStyle);
  }

  public TRecyclerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TRecyclerView(Context context) {
    this(context, null);
  }

  @Override
  public void setOnScrollListener(OnScrollListener l) {
    mOnScrollListeners.add(l);
  }

  public void removeOnScrollListener(OnScrollListener l) {
    if (l == null) {
      return;
    }
    mOnScrollListeners.remove(l);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof MeasureCallback) {
        ((MeasureCallback) feature).beforeOnMeasure(widthMeasureSpec, heightMeasureSpec);
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof MeasureCallback) {
        ((MeasureCallback) feature).afterOnMeasure(widthMeasureSpec, heightMeasureSpec);
      }
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof LayoutCallback) {
        ((LayoutCallback) feature).beforeOnLayout(changed, left, top, right, bottom);
      }
    }
    super.onLayout(changed, left, top, right, bottom);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof LayoutCallback) {
        ((LayoutCallback) feature).afterOnLayout(changed, left, top, right, bottom);
      }
    }
  }

  @Override
  public void draw(Canvas canvas) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof CanvasCallback) {
        ((CanvasCallback) feature).beforeDraw(canvas);
      }
    }
    super.draw(canvas);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof CanvasCallback) {
        ((CanvasCallback) feature).afterDraw(canvas);
      }
    }

  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof CanvasCallback) {
        ((CanvasCallback) feature).beforeDispatchDraw(canvas);
      }
    }
    super.dispatchDraw(canvas);
    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof CanvasCallback) {
        ((CanvasCallback) feature).afterDispatchDraw(canvas);
      }
    }
  }

  @Override
  public void onDraw(Canvas canvas) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof CanvasCallback) {
        ((CanvasCallback) feature).beforeOnDraw(canvas);
      }
    }

    super.onDraw(canvas);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof CanvasCallback) {
        ((CanvasCallback) feature).afterOnDraw(canvas);
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof TouchEventCallback) {
        ((TouchEventCallback) feature).beforeOnTouchEvent(event);
      }
    }
    boolean result = super.onTouchEvent(event);

    if (null != mGestureDetector) {
      mGestureDetector.onTouchEvent(event);
    }

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof TouchEventCallback) {
        ((TouchEventCallback) feature).afterOnTouchEvent(event);
      }
    }
    return result;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof TouchEventCallback) {
        ((TouchEventCallback) feature).beforeDispatchTouchEvent(event);
      }
    }
    boolean result = super.dispatchTouchEvent(event);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof TouchEventCallback) {
        ((TouchEventCallback) feature).afterDispatchTouchEvent(event);
      }
    }
    return result;
  }

  @Override
  protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof FocusCallback) {
        ((FocusCallback) feature).beforeOnFocusChanged(gainFocus, direction, previouslyFocusedRect);
      }
    }
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof FocusCallback) {
        ((FocusCallback) feature).afterOnFocusChanged(gainFocus, direction, previouslyFocusedRect);
      }
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof FocusCallback) {
        ((FocusCallback) feature).beforeOnWindowFocusChanged(hasWindowFocus);
      }
    }
    super.onWindowFocusChanged(hasWindowFocus);

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof FocusCallback) {
        ((FocusCallback) feature).afterOnWindowFocusChanged(hasWindowFocus);
      }
    }
  }

  @Override
  public void setMeasuredDimension(long width, long height) {
    super.setMeasuredDimension((int) width, (int) height);
  }

  @Override
  public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec, int reserve) {
    super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
  }

  @Override
  public boolean drawChild(Canvas canvas, View child, long drawingTime, int reserve) {
    return super.drawChild(canvas, child, drawingTime);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean ret = super.onInterceptTouchEvent(ev);
    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof InterceptTouchEventCallback) {
        ret |= ((InterceptTouchEventCallback) feature).onInterceptTouchEvent(ev);
      }
    }
    return ret;
  }

  @Override
  public void computeScroll() {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof ScrollCallback) {
        ((ScrollCallback) feature).beforeComputeScroll();
      }
    }
    super.computeScroll();
    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof ScrollCallback) {
        ((ScrollCallback) feature).afterComputeScroll();
      }
    }
  }

  @Override
  public boolean addFeature(AbsFeature<? super RecyclerView> feature) {
    return mFeatureList.addFeature(feature);
  }

  @Override
  public AbsFeature<? super RecyclerView> findFeature(Class<? extends AbsFeature<? super RecyclerView>> clazz) {
    return mFeatureList.findFeature(clazz);
  }

  @Override
  public boolean removeFeature(Class<? extends AbsFeature<? super RecyclerView>> clazz) {
    return mFeatureList.removeFeature(clazz);
  }

  @Override
  public void clearFeatures() {
    mFeatureList.clearFeatures();
  }

  @Override
  public void init(Context context, AttributeSet attrs, int defStyle) {
    mFeatureList.init(context, attrs, defStyle);
  }

  @Override
  protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
    if (getAdapter() != null) {

      GridLayoutAnimationController.AnimationParameters animationParams =
          (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

      if (animationParams == null) {
        animationParams = new GridLayoutAnimationController.AnimationParameters();
        params.layoutAnimationParameters = animationParams;
      }

      int columns = ((StaggeredGridLayoutManager) getLayoutManager()).getSpanCount();

      animationParams.count = count;
      animationParams.index = index;
      animationParams.columnsCount = columns;
      animationParams.rowsCount = count / columns;

      final int invertedIndex = count - 1 - index;
      animationParams.column = columns - 1 - (invertedIndex % columns);
      animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;

    } else {
      super.attachLayoutAnimationParameters(child, params, index, count);
    }
  }

  class InnerOnScrollListener extends OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      for (OnScrollListener l : mOnScrollListeners) {
        if (l != null) {
          l.onScrollStateChanged(recyclerView, newState);
        }
      }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      for (OnScrollListener l : mOnScrollListeners) {
        l.onScrolled(recyclerView, dx, dy);
      }
    }
  }

  /**
   * powered by yanpei
   */
  public static class HeaderViewHolder extends RecyclerView.ViewHolder {

    public HeaderViewHolder(View view) {
      super(view);
    }
  }

  public static class HeaderViewListAdapter extends Adapter implements Filterable {

    private final Adapter mAdapter;

    private final RecyclerView mRecyclerView;

    ArrayList<View> mHeaderViews;

    ArrayList<View> mFooterViews;

    static final ArrayList<View> EMPTY_INFO_LIST = new ArrayList<View>();

    private final boolean mIsFilterable;

    public HeaderViewListAdapter(ArrayList<View> headerViews, ArrayList<View> footerViews, Adapter adapter,
                                 RecyclerView recyclerView) {
      mAdapter = adapter;
      mRecyclerView = recyclerView;
      mIsFilterable = adapter instanceof Filterable;

      if (headerViews == null) {
        mHeaderViews = EMPTY_INFO_LIST;
      } else {
        mHeaderViews = headerViews;
      }

      if (footerViews == null) {
        mFooterViews = EMPTY_INFO_LIST;
      } else {
        mFooterViews = footerViews;
      }
    }

    public int getHeadersCount() {
      return mHeaderViews.size();
    }

    public int getFootersCount() {
      return mFooterViews.size();
    }

    public boolean isEmpty() {
      return mAdapter == null || 0 == mAdapter.getItemCount();
    }

    public boolean removeHeader(View v) {
      for (int i = 0; i < mHeaderViews.size(); i++) {
        View view = mHeaderViews.get(i);
        if (view == v) {
          mHeaderViews.remove(i);
          return true;
        }
      }

      return false;
    }

    public boolean removeFooter(View v) {
      for (int i = 0; i < mFooterViews.size(); i++) {
        View view = mFooterViews.get(i);
        if (view == v) {
          mFooterViews.remove(i);
          return true;
        }
      }
      return false;
    }

    @Override
    public long getItemId(int position) {
      int numHeaders = getHeadersCount();
      if (mAdapter != null && position >= numHeaders) {
        int adjPosition = position - numHeaders;
        int adapterCount = mAdapter.getItemCount();
        if (adjPosition < adapterCount) {
          return mAdapter.getItemId(adjPosition);
        }
      }
      return NO_ID;
    }

    @Override
    public int getItemCount() {
      if (mAdapter != null) {
        return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
      } else {
        return getFootersCount() + getHeadersCount();
      }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if (0 == (viewType & ITEM_VIEW_TYPE_MASK)) {
        if (mAdapter != null) {
          return mAdapter.onCreateViewHolder(parent, viewType);
        }
      } else {
        int position = viewType & ITEM_POSITION_MASK;
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
          return new HeaderViewHolder(mHeaderViews.get(position));
        } else {
          final int adjPosition = position - numHeaders;
          int adapterCount = 0;
          if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
          }
          return new HeaderViewHolder(mFooterViews.get(adjPosition - adapterCount));
        }
      }

      return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      int numHeaders = getHeadersCount();
      int numFooters = getFootersCount();
      if (position < numHeaders || position >= getItemCount() - numFooters) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (null == lp) {
          lp = mRecyclerView.getLayoutManager().generateDefaultLayoutParams();
          holder.itemView.setLayoutParams(lp);
        }

        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
          ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
        } else if (lp instanceof RecyclerView.LayoutParams) {
          lp.width = LayoutParams.MATCH_PARENT;
          lp.height = LayoutParams.MATCH_PARENT;
        }
      } else {
        final int adjPosition = position - numHeaders;
        if (mAdapter != null) {
          mAdapter.onBindViewHolder(holder, adjPosition);
        }
      }
    }

    @Override
    public int getItemViewType(int position) {
      int numHeaders = getHeadersCount();
      if (mAdapter != null && position >= numHeaders) {
        int adjPosition = position - numHeaders;
        int adapterCount = mAdapter.getItemCount();
        if (adjPosition < adapterCount) {
          return mAdapter.getItemViewType(adjPosition);
        }
      }

      return position | ITEM_VIEW_TYPE_MASK;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
      if (mAdapter != null) {
        mAdapter.registerAdapterDataObserver(observer);
      }
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
      if (mAdapter != null) {
        mAdapter.unregisterAdapterDataObserver(observer);
      }
    }

    public Filter getFilter() {
      if (mIsFilterable) {
        return ((Filterable) mAdapter).getFilter();
      }
      return null;
    }

    public Adapter getWrappedAdapter() {
      return mAdapter;
    }
  }

  public final void addHeaderView(View v) {
    mHeaderViews.add(v);

    final Adapter adapter = getAdapter();
    if (adapter != null) {
      if (!(adapter instanceof HeaderViewListAdapter)) {
        setAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this));
      } else {
        adapter.notifyDataSetChanged();
      }
    }

    final LayoutManager lm = getLayoutManager();
    if (lm != null) {
      setLayoutManager(lm);
    }
  }

  public final void addFooterView(View v) {
    mFooterViews.add(v);

    final Adapter adapter = getAdapter();
    if (adapter != null) {
      if (!(adapter instanceof HeaderViewListAdapter)) {
        setAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this));
      } else {
        adapter.notifyDataSetChanged();
      }
    }

    final LayoutManager lm = getLayoutManager();
    if (lm != null) {
      setLayoutManager(lm);
    }
  }

  public final boolean removeHeaderView(View v) {
    if (mHeaderViews.size() > 0) {
      boolean result = false;
      final Adapter adapter = getAdapter();
      if (adapter != null && ((HeaderViewListAdapter) adapter).removeHeader(v)) {
        adapter.notifyDataSetChanged();
        result = true;
      }
      removeFixedViewInfo(v, mHeaderViews);
      return result;
    }
    return false;
  }

  public final boolean removeFooterView(View v) {
    if (mFooterViews.size() > 0) {
      boolean result = false;
      final Adapter adapter = getAdapter();
      if (adapter != null && ((HeaderViewListAdapter) adapter).removeHeader(v)) {
        adapter.notifyDataSetChanged();
        result = true;
      }
      removeFixedViewInfo(v, mFooterViews);
      return result;
    }
    return false;
  }

  private void removeFixedViewInfo(View v, ArrayList<View> where) {
    int len = where.size();
    for (int i = 0; i < len; ++i) {
      View view = where.get(i);
      if (view == v) {
        where.remove(i);
        break;
      }
    }
  }

  public int getHeaderViewsCount() {
    return mHeaderViews.size();
  }

  public int getFooterViewsCount() {
    return mFooterViews.size();
  }

  public int getItemCount() {
    return getTotalCount() - getHeaderViewsCount() - getFooterViewsCount();
  }

  public int getTotalCount() {
    Adapter adapter = getAdapter();
    if (null == adapter) {
      return 0;
    } else {
      return adapter.getItemCount();
    }
  }

  @Override
  public void setAdapter(Adapter adapter) {
    for (AbsFeature<? super RecyclerView> feature : mFeatureList) {
      if (feature instanceof RecyclerAdapterCallback) {
        if (adapter instanceof HeaderViewListAdapter) {
          ((RecyclerAdapterCallback) feature).beforeSetAdapter(((HeaderViewListAdapter) adapter).getWrappedAdapter());
        } else {
          ((RecyclerAdapterCallback) feature).beforeSetAdapter(adapter);
        }
      }
    }

    if (null != adapter && !(adapter instanceof HeaderViewListAdapter) && (mHeaderViews.size() > 0 || mFooterViews.size() > 0)) {
      super.setAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this));
    } else {
      super.setAdapter(adapter);
    }

    for (int i = mFeatureList.size() - 1; i >= 0; i--) {
      AbsFeature<? super RecyclerView> feature = mFeatureList.get(i);
      if (feature instanceof RecyclerAdapterCallback) {
        if (adapter instanceof HeaderViewListAdapter) {
          ((RecyclerAdapterCallback) feature).afterSetAdapter(((HeaderViewListAdapter) adapter).getWrappedAdapter());
        } else {
          ((RecyclerAdapterCallback) feature).afterSetAdapter(adapter);
        }
      }
    }
  }

  @Override
  public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
    if (null != adapter && !(adapter instanceof HeaderViewListAdapter)) {
      if (mHeaderViews.size() > 0 || mFooterViews.size() > 0) {
        super.swapAdapter(new HeaderViewListAdapter(mHeaderViews, mFooterViews, adapter, this), removeAndRecycleExistingViews);
        return;
      }
    }
    super.swapAdapter(adapter, removeAndRecycleExistingViews);
  }

  @Override
  public void setLayoutManager(LayoutManager layout) {
    if (layout instanceof GridLayoutManager) {
      if (mHeaderViews.size() > 0 || mFooterViews.size() > 0) {
        final GridLayoutManager.SpanSizeLookup ssl = ((GridLayoutManager) layout).getSpanSizeLookup();
        if (null == ssl || !(ssl instanceof SpanSizeLookupCompat)) {
          ((GridLayoutManager) layout).setSpanSizeLookup(new SpanSizeLookupCompat(ssl));
        }
      }
    }
    super.setLayoutManager(layout);
  }

  private boolean isHeaderOrFooter(int position) {
    int numHeaders = getHeaderViewsCount();
    int numFooters = getFooterViewsCount();
    if (getAdapter() != null) {
      if (position >= numHeaders && position < getAdapter().getItemCount() - numFooters) {
        return false;
      }
    }

    return true;
  }

  class SpanSizeLookupCompat extends GridLayoutManager.SpanSizeLookup {

    private final GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

    public SpanSizeLookupCompat(GridLayoutManager.SpanSizeLookup ssl) {
      super();
      mSpanSizeLookup = ssl;
    }

    @Override
    public int getSpanSize(int position) {
      if (isHeaderOrFooter(position)) {
        return ((GridLayoutManager) getLayoutManager()).getSpanCount();
      } else if (null != mSpanSizeLookup) {
        return mSpanSizeLookup.getSpanSize(position - getHeaderViewsCount());
      }
      return 1;
    }

    @Override
    public void setSpanIndexCacheEnabled(boolean cacheSpanIndices) {
      if (null != mSpanSizeLookup) {
        mSpanSizeLookup.setSpanIndexCacheEnabled(cacheSpanIndices);
        return;
      }
      super.setSpanIndexCacheEnabled(cacheSpanIndices);
    }

    @Override
    public void invalidateSpanIndexCache() {
      if (null != mSpanSizeLookup) {
        mSpanSizeLookup.invalidateSpanIndexCache();
        return;
      }
      super.invalidateSpanIndexCache();
    }

    @Override
    public boolean isSpanIndexCacheEnabled() {
      if (null != mSpanSizeLookup) {
        return mSpanSizeLookup.isSpanIndexCacheEnabled();
      }
      return super.isSpanIndexCacheEnabled();
    }

    @Override
    public int getSpanIndex(int position, int spanCount) {
      if (!isHeaderOrFooter(position) && null != mSpanSizeLookup) {
        return mSpanSizeLookup.getSpanIndex(position - getHeaderViewsCount(), spanCount);
      }
      return 0;
    }

    @Override
    public int getSpanGroupIndex(int adapterPosition, int spanCount) {
      if (!isHeaderOrFooter(adapterPosition) && null != mSpanSizeLookup) {
        return mSpanSizeLookup.getSpanGroupIndex(adapterPosition - getHeaderViewsCount(), spanCount);
      }
      return 0;
    }
  }

  public interface OnItemLongClickListener {

    boolean onItemLongClick(TRecyclerView parent, View view, int position, long id);
  }

  public interface OnItemClickListener {

    void onItemClick(TRecyclerView parent, View view, int position, long id);
  }

  private OnItemClickListener mItemClickListener;

  private OnItemLongClickListener mItemLongClickListener;

  public void setOnItemClickListener(OnItemClickListener listener) {
    mItemClickListener = listener;
    if (null != listener) {
      addGestureDetectorIfNeed();
    }
  }

  public void setOnItemLongClickListener(OnItemLongClickListener listener) {
    if (!isLongClickable()) {
      setLongClickable(true);
    }

    mItemLongClickListener = listener;
    if (null != listener) {
      addGestureDetectorIfNeed();
    }
  }

  public OnItemClickListener getItemClickListener() {
    return mItemClickListener;
  }

  public OnItemLongClickListener getItemLongClickListener() {
    return mItemLongClickListener;
  }

  private GestureDetector mGestureDetector;

  private void addGestureDetectorIfNeed() {
    if (null == mGestureDetector) {
      mGestureDetector = new GestureDetector(getContext(), new ItemClickGestureListener(this) {
        @Override
        boolean performItemClick(TRecyclerView parent, View view, int position, long id) {
          if (mItemClickListener != null) {
            mItemClickListener.onItemClick(parent, view, position, id);
            return true;
          }

          return false;
        }

        @Override
        boolean performItemLongClick(TRecyclerView parent, View view, int position, long id) {
          return mItemLongClickListener != null && mItemLongClickListener.onItemLongClick(parent, view, position, id);
        }
      });
    }
  }

  private abstract static class ItemClickGestureListener extends GestureDetector.SimpleOnGestureListener {

    abstract boolean performItemClick(TRecyclerView parent, View view, int position, long id);

    abstract boolean performItemLongClick(TRecyclerView parent, View view, int position, long id);

    private final TRecyclerView mHostView;

    private View mTargetChild;

    public ItemClickGestureListener(TRecyclerView hostView) {
      mHostView = hostView;
    }

    @Override
    public boolean onDown(MotionEvent event) {
      final int x = (int) event.getX();
      final int y = (int) event.getY();

      mTargetChild = mHostView.findChildViewUnder(x, y);
      return (mTargetChild != null);
    }

    @Override
    public void onShowPress(MotionEvent event) {
      if (mTargetChild != null) {
        mTargetChild.setPressed(true);
      }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
      boolean handled = false;

      if (mTargetChild != null) {
        mTargetChild.setPressed(false);

        final int position = mHostView.getChildPosition(mTargetChild);
        final long id = mHostView.getAdapter().getItemId(position);
        final int adjPos = position - mHostView.getHeaderViewsCount();
        if (!mHostView.isHeaderOrFooter(position)) {
          handled = performItemClick(mHostView, mTargetChild, adjPos, id);
        }

        mTargetChild = null;
      }

      return handled;
    }

    @Override
    public boolean onScroll(MotionEvent event, MotionEvent event2, float v, float v2) {
      if (mTargetChild != null) {
        mTargetChild.setPressed(false);
        mTargetChild = null;
        return true;
      }

      return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
      if (mTargetChild == null) {
        return;
      }
      boolean handled = false;
      final int position = mHostView.getChildPosition(mTargetChild);
      final long id = mHostView.getAdapter().getItemId(position);
      final int adjPos = position - mHostView.getHeaderViewsCount();
      if (!mHostView.isHeaderOrFooter(position)) {
        handled = performItemLongClick(mHostView, mTargetChild, adjPos, id);
      }

      if (handled) {
        mTargetChild.setPressed(false);
        mTargetChild = null;
      }
    }
  }
/**
 * powered by yanpei end
 */
}
