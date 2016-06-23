package com.frame.base.utl.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.frame.base.utl.R;

public class SlideSwitchView extends View{
	/** Switch底部样式图片 */
	private Bitmap mSwitchBottom;
	/** Switch 当前样式  */
	private Bitmap mSwitchThumb;
	/** Switch无操作情况下的样式  */
	private Bitmap mSwitchThumbNormal;
	/** Switch当前手指触摸式的样式  */
	private Bitmap mSwitchThumbPressed;
	/** Switch 框架  */
	private Bitmap mSwitchFrame;
	private Bitmap mSwitchMask;
	private float mCurrentX = 0;
	/** Switch 开关状态，默认是  开：true  */
	private boolean mSwitchOn = true;
	/** Switch 最大移动距离   */
	private int mMoveLength;
	/** 第一次按下的有效区域 */
	private float mLastX = 0;
	/** 绘制的目标区域大小  */
	private Rect mDest = null;
	/** 截取源图片的大小  */
	private Rect mSrc = null;
	/** Switch 移动的偏移量  */
	private int mMoveDeltX = 0;
	/** 画笔工具  */
	private Paint mPaint = null;
	/** Switch 状态监听接口  */
	private OnSwitchChangedListener switchListener = null;
	private boolean mFlag = false;
	/** enabled 属性 为 true */
	private boolean mEnabled = true;
	/** 最大透明度，就是不透明 */
	private final int MAX_ALPHA = 255;
	/** 当前透明度，这里主要用于如果控件的enable属性为false时候设置半透明 ，即不可以点击 */
	private int mAlpha = MAX_ALPHA;
	/** Switch 判断是否在拖动 */
	private boolean mIsScrolled =false;

	public SlideSwitchView(Context context) {
		this(context, null);
	}

	public SlideSwitchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideSwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化相关资源
	 */
	public void init() {
		mSwitchThumbPressed = BitmapFactory.decodeResource(getResources(), R.mipmap.checkswitch_btn_pressed);
		mSwitchThumbNormal = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_btn_unpressed);
		mSwitchBottom = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_bottom);
		mSwitchFrame = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_frame);
		mSwitchMask = BitmapFactory.decodeResource(getResources(),R.mipmap.checkswitch_mask);
		mSwitchThumb = mSwitchThumbNormal;
		mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth();
		//绘制区域大小
		mDest = new Rect(0, 0, mSwitchFrame.getWidth(),mSwitchFrame.getHeight());
		mSrc = new Rect();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(255);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mMoveDeltX > 0 || mMoveDeltX == 0 && mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(mMoveLength - mMoveDeltX, 0, mSwitchBottom.getWidth()
						- mMoveDeltX, mSwitchFrame.getHeight());
			}
		} else if (mMoveDeltX < 0 || mMoveDeltX == 0 && !mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(-mMoveDeltX, 0, mSwitchFrame.getWidth() - mMoveDeltX,
						mSwitchFrame.getHeight());
			}
		}
		Log.d("mAlpha", "mAlpha:" + mAlpha);
		canvas.saveLayerAlpha(new RectF(mDest), mAlpha, Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchFrame, 0, 0, null);
		canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
		canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//如果Enabled属性设定为true,触摸效果才有效
		if(!mEnabled){
			return true;
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mSwitchThumb = mSwitchThumbPressed;
				mLastX = event.getX();
				break;
			case MotionEvent.ACTION_MOVE:
				mCurrentX = event.getX();
				mMoveDeltX = (int) (mCurrentX - mLastX);
				if(mMoveDeltX > 10){
					//设置了10这个误差距离，可以更好的实现点击效果
					mIsScrolled = true;
				}
				// 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
				if ((mSwitchOn && mMoveDeltX < 0) || (!mSwitchOn && mMoveDeltX > 0)) {
					mFlag = true;
					mMoveDeltX = 0;
				}

				if (Math.abs(mMoveDeltX) > mMoveLength) {
					mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				mSwitchThumb = mSwitchThumbNormal;
				//如果没有滑动过，就看作一次点击事件
				if(!mIsScrolled){
					mMoveDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
					mSwitchOn = !mSwitchOn;
					if (switchListener != null) {
						switchListener.onSwitchChange(this, mSwitchOn);
					}
					invalidate();
					mMoveDeltX = 0;
					break;
				}
				mIsScrolled = false;
				if (Math.abs(mMoveDeltX) > 0 && Math.abs(mMoveDeltX) < mMoveLength / 2) {
					mMoveDeltX = 0;
					invalidate();
				} else if (Math.abs(mMoveDeltX) > mMoveLength / 2
						&& Math.abs(mMoveDeltX) <= mMoveLength) {
					mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
					mSwitchOn = !mSwitchOn;
					if (switchListener != null) {
						switchListener.onSwitchChange(this, mSwitchOn);
					}
					invalidate();
					mMoveDeltX = 0;
				} else if (mMoveDeltX == 0 && mFlag) {
					// 这时候得到的是不需要进行处理的，因为已经move过了
					mMoveDeltX = 0;
					mFlag = false;
				}
			default:
				break;
		}
		invalidate();
		return true;
	}
	/**
	 * 设置 switch 状态监听
	 * */
	public void setOnChangeListener(OnSwitchChangedListener listener) {
		switchListener = listener;
	}
	/**
	 * switch 开关监听接口
	 *  */
	public interface OnSwitchChangedListener{
		public void onSwitchChange(SlideSwitchView switchView, boolean isChecked);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		mEnabled = enabled;
		mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA/2;
		Log.d("enabled",enabled ? "true": "false");
		super.setEnabled(enabled);
		invalidate();
	}

	/** 自动判断切换至相反的属性 : true -->false ;false -->true */
	public void toggle() {
		setChecked(!mSwitchOn);
	}

	/** 设置选中的状态（选中:true   非选中: false） */
	public void setChecked(boolean checked) {
		mSwitchOn = checked;
		invalidate();
	}
}