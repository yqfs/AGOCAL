package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 
 * @author fenglin.lzw
 *
 */
public class CustomProgressBar extends View {

	private final float MAX_SWEEP_ANGLE = 340f;
	private final float MIN_START_ANGLE = -80f;
	private final float DEFAULT_START_ANGLE = 260f;

	private Paint mPaint;
	private boolean mIsLoading = false; 
	private float mStartAngle = DEFAULT_START_ANGLE;
	private float mSweepAngle = 0;
	private int mDistance;
	private boolean mIsShow = false;//用于区别这个半圆是否在最初的时候显示
	
	private static final String TAG = "CustomProgressBar";
	private RectF mRectF;
	
	public CustomProgressBar(Context context) {
		this(context, null);
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public void startLoadingAnimaton() {
		mSweepAngle = MAX_SWEEP_ANGLE;
		mStartAngle = MIN_START_ANGLE;
		mIsLoading = true;
		invalidate();
	}
	
	public void stopLoadingAnimation() {
		mIsLoading = false;
		preDistance = 0f;
		if(mIsShow){
			mSweepAngle = MAX_SWEEP_ANGLE;
			mStartAngle = MIN_START_ANGLE;
		}
		else {
			mStartAngle = DEFAULT_START_ANGLE;
			mSweepAngle = 0f;
		}
		invalidate();
	}
	
	
	
	private void init() {
		mPaint = new Paint();
//		mPaint.setColor(0xFFFF5000);
		mPaint.setColor(0xFF999999);
		mPaint.setAntiAlias(true);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(3);
		
		mRectF = new RectF();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		float x = getWidth();
		float y = getHeight();
		mRectF.set(3, 3, x-3, y-3);
//		Log.i(TAG, "onDraw() ---->  mStartAngle : " + mStartAngle + "   mSweepAngle : " + mSweepAngle);
		canvas.drawArc(mRectF, mStartAngle, mSweepAngle, false, mPaint);
		if(mIsLoading) {
//			Log.i(TAG, "mIsLoading ---->  mStartAngle : " + mStartAngle + "   mSweepAngle : " + mSweepAngle);
			mStartAngle += 10;
			invalidate();
		}
		
	}
	
	float preDistance = 0f;
	
	/**
	 * 根据当前的滑动长度，计算画圆的弧度
	 * @param distance
	 */
	public void changeProgressBarState(int distance) {
		float count = 0f;
		float angle = 0f;
		if(distance < 0) {
			mStartAngle = DEFAULT_START_ANGLE;
			mSweepAngle = 0f;
			invalidate();
			return;
		}
		count = (distance - preDistance)/mDistance;
		angle = MAX_SWEEP_ANGLE * count;
		mStartAngle -= angle;
		mSweepAngle += angle;
//		Log.i(TAG, "distance : " + distance + "  preDistance : " + preDistance + "  mDistance : " + mDistance +
//				"  count : " + count + " angle : " + angle + " mStartAngle : " + mStartAngle + " mSweepAngle : " + mSweepAngle);
		//滑动的距离是不是超过规定的距离
		if(mStartAngle < MIN_START_ANGLE) {
			Log.i(TAG, "不在距离范围之内");
			mStartAngle = MIN_START_ANGLE;
			mSweepAngle = MAX_SWEEP_ANGLE;
			if(preDistance == mDistance) {
//				Log.i(TAG, "distance : changeProgressBarState : " + distance);
				return;
			}
			preDistance = mDistance;
		}
		else{
			Log.i(TAG, "在距离中");
			preDistance = distance;
		}
		invalidate();
	}
	
	/**
	 * 设置滑动长度，用来计算旋转的角度
	 * @param distance
	 */
	public void setPullDownDistance(int distance) {
		mDistance = distance ;
	}
	
	public void setPaintColor(int color) {
		if(mPaint != null)
			mPaint.setColor(color);
	}
	
	/**
	 * 判断是否需要在初始化的时候就显示完整的半圆
	 * @param isShow
	 */
	public void isInitShow(boolean isShow) {
		mIsShow = isShow;
		if(isShow) {
			mSweepAngle = MAX_SWEEP_ANGLE;
			mStartAngle = MIN_START_ANGLE;
		}
	}
	
}
