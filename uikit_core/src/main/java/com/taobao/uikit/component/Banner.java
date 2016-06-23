package com.taobao.uikit.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.taobao.uikit.R;
import com.taobao.uikit.utils.HandlerTimer;

/**
 * 
 * The Banner contains two component: LoopViewPager and IndicatorView.
 * 
 * @author jiajing
 */
public class Banner extends FrameLayout {
	// private static final String TAG = "Banner";
	private static final int DEFAULT_CYCLE_INTERVAL_MILLS = 3000;
	
	private HandlerTimer mTimer;
	private int mScrollInterval = DEFAULT_CYCLE_INTERVAL_MILLS;
	protected LoopViewPager mViewPager;
	protected IndicatorView mIndicator;
	private boolean mAutoScroll = false;
    private float mRatio;
    private boolean mInTouchMode = false;

	public Banner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	public Banner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public Banner(Context context) {
		super(context);
		init(context, null, 0);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		initAttr(context, attrs, defStyle);
		
		//initTimer();
				
		initView(LayoutInflater.from(this.getContext()).inflate(R.layout.uik_banner,
				this));
	}
	
	private void initAttr(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.Banner, defStyle, 0);
		if (a != null) {
			mScrollInterval = a.getInt(
					R.styleable.Banner_uik_autoScrollInterval,
					DEFAULT_CYCLE_INTERVAL_MILLS);
			mAutoScroll = a
					.getBoolean(R.styleable.Banner_uik_autoScroll, false);
            mRatio = a.getFloat(R.styleable.Banner_uik_ratio, 0.3125f);
			a.recycle();
		}
	}
	
	private void initView(View root){
		
		mViewPager = (LoopViewPager) root.findViewById(R.id.viewpager);
        mViewPager.setRatio(mRatio);
		mIndicator = (IndicatorView) root.findViewById(R.id.indicator);
		mViewPager
		.setOnPageChangeListener(new LoopViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				mIndicator.setIndex(index);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

        root.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override public void onScrollChanged()
            {
                if (!mInTouchMode)
                {
                    Rect out = new Rect();
                    if (getGlobalVisibleRect(out))
                    {
                        if (mTimer != null)
                        {
                            mTimer.start();
                        }
                    }
                    else
                    {
                        if (mTimer != null)
                        {
                            mTimer.stop();
                        }
                    }
                }
            }
        });
	}
	
	private void initTimer() {
		if (mTimer != null) {
			mTimer.stop();
            mTimer = null;
		}
		if (mAutoScroll) {
			mTimer = new HandlerTimer(mScrollInterval, new Runnable() {
				@Override
				public void run() {
					if (mViewPager != null && mViewPager.getAdapter() != null) {
						int count = mViewPager.getAdapter().getCount();
						if (count != 0) {
							int current = mViewPager.getCurrentItem();
							int to = (current + 1) % count;
							mViewPager.setCurrentItem(to, true);
                            Log.d("yanpei","setCurrentItem");
						}
					}
				}
			});
			
//			mViewPager.setOnTouchListener(new View.OnTouchListener() {
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					doTimerEvent(event);
//					return false;
//				}
//			});
			
			mTimer.start();
		}
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		initTimer();
	}

	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		if(mTimer != null){
			mTimer.stop();
            mTimer = null;
		}
	}
	

    public void destory(){
    	if(mTimer != null){
			mTimer.stop();
            mTimer = null;
		}
    }
	
	public void setAutoScroll(boolean autoScroll) {
		this.mAutoScroll = autoScroll;
		initTimer();
	}

	public void setScrollInterval(int interval) {
		mScrollInterval = interval;
		initTimer();
	}

	public void setAdapter(PagerAdapter adapter) {
		mViewPager.setAdapter(adapter);
		mIndicator.setTotal(adapter.getCount());
	}

	public void setLayout(int layoutResID) {
		setLayout(LayoutInflater.from(this.getContext()).inflate(layoutResID, null));
	}

	public void setLayout(View view) {
		if (view.findViewById(R.id.viewpager) == null
				|| view.findViewById(R.id.indicator) == null) {
			throw new RuntimeException(
					"banner need two views which's are viewpager and indicator");
		}
		this.removeAllViews();
		this.addView(view);
		initView(view);

	}

    public void setRatio(float ratio){
        mRatio = ratio;
        mViewPager.setRatio(mRatio);
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev)
    {
        doTimerEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void doTimerEvent(MotionEvent event){
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mInTouchMode = true;
            if(mTimer != null){
				mTimer.stop();
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_CANCEL
				|| event.getAction() == MotionEvent.ACTION_OUTSIDE
				|| event.getAction() == MotionEvent.ACTION_UP) {
            mInTouchMode = false;
            if(mTimer != null){
				mTimer.start();
			}
		}
    }
    
	protected void onWindowVisibilityChanged(int visibility) {
		if (visibility == View.INVISIBLE || visibility == View.GONE) {
			if (mTimer != null) {
				mTimer.stop();
			}
		}
	}
    
}
