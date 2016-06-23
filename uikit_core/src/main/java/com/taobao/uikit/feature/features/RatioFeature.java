package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.view.ViewHelper;

/**
 * 
 * The feature make the ratio of view's height and width remain unchanged. For
 * example, if the width remains unchanged,then the height equals the width plus
 * the ratio.
 * 
 * The direction of unchanged can be set by calling {@link #setOrientation(int)
 * setOrientation()}. The ratio can be set by calling {@link #setRatio(float)
 * setRatio()}.
 * 
 * The default orientation is horizontal. The default ratio is 0.5f.
 * 
 * @author jiajing
 * 
 * @attr ref R.styleable#ratio
 * @attr ref R.styleable#orientation
 */
public class RatioFeature extends AbsFeature<View> implements MeasureCallback
{
	
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	private static float sDefaultRatio = 0.5f;
	
	private float mRatio = sDefaultRatio;
	private int mOrientation = HORIZONTAL;

	@Override
	public void beforeOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	}
	
	@Override
	public void setHost(View host) {
		super.setHost(host);
		host.requestLayout();
	}

	@Override
	public void afterOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mRatio > 0) {
			int widthSize = 0;
			int heightSize = 0;

			//int pleft = getHost().getPaddingLeft();
			//int pright = getHost().getPaddingRight();
			//int ptop = getHost().getPaddingTop();
			//int pbottom = getHost().getPaddingBottom();
			boolean shouldSet = false;
			if (mOrientation == HORIZONTAL) {
				
				  /*if (widthMeasureSpec == MeasureSpec.AT_MOST ||
						  widthMeasureSpec == MeasureSpec.EXACTLY)*/{
					//int w = 0;
					
					//w += pleft + pright;
					//widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);					
					//heightSize = (int) ((widthSize - pleft - pright) * mAspectRatio)
					//		+ ptop + pbottom;
//					widthSize = this.getHost().getMeasuredWidth();
					widthSize = MeasureSpec.getSize(widthMeasureSpec);
					heightSize = (int) (widthSize * mRatio);
					shouldSet = true;
				}
			} else if (mOrientation == VERTICAL) {

				/*if (heightMeasureSpec == MeasureSpec.AT_MOST
						|| heightMeasureSpec == MeasureSpec.EXACTLY) */{
					//int h = 0;
					//h += ptop + pbottom;
					//heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
					//widthSize = (int) ((heightSize - ptop - pbottom) * mAspectRatio)
					//		+ ptop + pbottom;
//					heightSize = this.getHost().getMeasuredHeight();
				    heightSize = MeasureSpec.getSize(heightMeasureSpec);
					widthSize = (int) (heightSize * mRatio);
					shouldSet = true;

				}
			}
			if(shouldSet && this.getHost() instanceof ViewHelper){
				((ViewHelper)this.getHost()).setMeasuredDimension(widthSize, heightSize);
			}
		}

	}

	/** set the ratio
	 * @param ratio
	 */
	public void setRatio(float ratio) {
		if (ratio > 0 && this.mRatio != ratio) {
			this.mRatio = ratio;
			if (this.getHost() != null) {
				this.getHost().requestLayout();
			}
		}
	}

	/**set the orientation which remains unchanged
	 * @param orientation Pass HORIZONTAL or VERTICAL. Default
     * value is HORIZONTAL.
	 */
	public void setOrientation(int orientation) {
		this.mOrientation = orientation;
	}

	/*private int resolveSizeAndState(int size, int measureSpec,
			int childMeasuredState) {
		int result = size;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			result = size;
			break;
		case MeasureSpec.AT_MOST:
			if (specSize < size) {
				result = specSize | 0x01000000;
			} else {
				result = size;
			}
			break;
		case MeasureSpec.EXACTLY:
			result = specSize;
			break;
		}
		return result | (childMeasuredState & 0xff000000);
	}*/

	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {
	        if (null != attrs)
	        {
	            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioFeature);
	            if (null != a)
	            {
	            	mRatio = a.getFloat(R.styleable.RatioFeature_uik_ratio, sDefaultRatio);
	            	mOrientation = a.getInt(R.styleable.RatioFeature_uik_orientation, HORIZONTAL);
	                a.recycle();
	            }
	        }
	}
}
