package com.taobao.uikit.feature.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.taobao.uikit.feature.callback.AdapterCallback;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.FocusCallback;
import com.taobao.uikit.feature.callback.InterceptTouchEventCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.utils.FeatureList;
import com.taobao.uikit.utils.IFeatureList;

/**
 * TListView: UIKit's custom ListView
 * 
 * @author jiajing
 */
public class TListView extends ListView implements OnScrollListener, ViewHelper, ViewGroupHelper, IFeatureList<ListView> {

    private FeatureList<ListView>  mFeatureList       = new FeatureList<ListView>(this);

    private List<OnScrollListener> mOnScrollListeners = new ArrayList<OnScrollListener>();

    private boolean                unScroll           = false;

    public TListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        super.setOnScrollListener(this);
        mFeatureList.init(context, attrs, defStyle);
    }

    public TListView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TListView(Context context){
        this(context, null);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListeners.add(l);
    }
    
    public void removeOnScrollListener(OnScrollListener l) {
    	if(l == null){
    		return;
    	}
        mOnScrollListeners.remove(l);
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for (OnScrollListener l : mOnScrollListeners) {
            l.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        for (OnScrollListener l : mOnScrollListeners) {
        	if(l != null){
        		l.onScrollStateChanged(view, scrollState);
        	}
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (unScroll) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            for (AbsFeature<? super ListView> feature : mFeatureList) {
                if (feature instanceof MeasureCallback) {
                    ((MeasureCallback) feature).beforeOnMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            for (int i = mFeatureList.size() - 1; i >= 0; i--) {
                AbsFeature<? super ListView> feature = mFeatureList.get(i);
                if (feature instanceof MeasureCallback) {
                    ((MeasureCallback) feature).afterOnMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof LayoutCallback) {
                ((LayoutCallback) feature).beforeOnLayout(changed, left, top, right, bottom);
            }
        }
        super.onLayout(changed, left, top, right, bottom);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof LayoutCallback) {
                ((LayoutCallback) feature).afterOnLayout(changed, left, top, right, bottom);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof CanvasCallback) {
                ((CanvasCallback) feature).beforeDraw(canvas);
            }
        }
        super.draw(canvas);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof CanvasCallback) {
                ((CanvasCallback) feature).afterDraw(canvas);
            }
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof CanvasCallback) {
                ((CanvasCallback) feature).beforeDispatchDraw(canvas);
            }
        }
        super.dispatchDraw(canvas);
        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof CanvasCallback) {
                ((CanvasCallback) feature).afterDispatchDraw(canvas);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof CanvasCallback) {
                ((CanvasCallback) feature).beforeOnDraw(canvas);
            }
        }

        super.onDraw(canvas);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof CanvasCallback) {
                ((CanvasCallback) feature).afterOnDraw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof TouchEventCallback) {
                ((TouchEventCallback) feature).beforeOnTouchEvent(event);
            }
        }
        boolean result = super.onTouchEvent(event);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof TouchEventCallback) {
                ((TouchEventCallback) feature).afterOnTouchEvent(event);
            }
        }
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof TouchEventCallback) {
                ((TouchEventCallback) feature).beforeDispatchTouchEvent(event);
            }
        }
        boolean result = super.dispatchTouchEvent(event);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof TouchEventCallback) {
                ((TouchEventCallback) feature).afterDispatchTouchEvent(event);
            }
        }
        return result;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof FocusCallback) {
                ((FocusCallback) feature).beforeOnFocusChanged(gainFocus, direction, previouslyFocusedRect);
            }
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof FocusCallback) {
                ((FocusCallback) feature).afterOnFocusChanged(gainFocus, direction, previouslyFocusedRect);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof FocusCallback) {
                ((FocusCallback) feature).beforeOnWindowFocusChanged(hasWindowFocus);
            }
        }
        super.onWindowFocusChanged(hasWindowFocus);

        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof FocusCallback) {
                ((FocusCallback) feature).afterOnWindowFocusChanged(hasWindowFocus);
            }
        }
    }

    @Override
    public void setMeasuredDimension(long width, long height) {
        super.setMeasuredDimension((int) width, (int) height);
    }

    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec, int reserve) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    public boolean drawChild(Canvas canvas, View child, long drawingTime, int reserve) {
        return super.drawChild(canvas, child, drawingTime);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ret = super.onInterceptTouchEvent(ev);
        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof InterceptTouchEventCallback) {
                ret |= ((InterceptTouchEventCallback) feature).onInterceptTouchEvent(ev);
            }
        }
        return ret;
    }

    @Override
    public void computeScroll() {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof ScrollCallback) {
                ((ScrollCallback) feature).beforeComputeScroll();
            }
        }
        super.computeScroll();
        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof ScrollCallback) {
                ((ScrollCallback) feature).afterComputeScroll();
            }
        }
    }

    @Override
    public boolean addFeature(AbsFeature<? super ListView> feature) {
        return mFeatureList.addFeature(feature);
    }

    @Override
    public AbsFeature<? super ListView> findFeature(Class<? extends AbsFeature<? super ListView>> clazz) {
        return mFeatureList.findFeature(clazz);
    }

    @Override
    public boolean removeFeature(Class<? extends AbsFeature<? super ListView>> clazz) {
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
    public void setAdapter(ListAdapter adapter) {
        for (AbsFeature<? super ListView> feature : mFeatureList) {
            if (feature instanceof AdapterCallback) {
                ((AdapterCallback) feature).beforeSetAdapter(adapter);
            }
        }
        super.setAdapter(adapter);
        for (int i = mFeatureList.size() - 1; i >= 0; i--) {
            AbsFeature<? super ListView> feature = mFeatureList.get(i);
            if (feature instanceof AdapterCallback) {
                ((AdapterCallback) feature).afterSetAdapter(adapter);
            }
        }

    }

    public boolean isUnScroll() {
        return unScroll;
    }

    public void setUnScroll(boolean unScroll) {
        this.unScroll = unScroll;
    }

}
