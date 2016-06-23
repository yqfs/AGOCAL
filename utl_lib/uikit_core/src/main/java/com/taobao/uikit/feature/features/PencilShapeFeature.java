package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;

public class PencilShapeFeature extends AbsFeature<View> implements
		CanvasCallback, LayoutCallback {

	private final static float sTopRatio = 0.2f;

	private float mTopRatio = sTopRatio;

	private final static float sDefaultRadius = 6;

	private float mRadiusX = sDefaultRadius;

	private float mRadiusY = sDefaultRadius;

	protected Paint mPaint;

	private Path mPath;

	private Path mPath1;

	private RectF mRectF;

	@Override
	public void constructor(Context context, AttributeSet attrs, int defStyle) {

		if (null != attrs) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.PencilShapeFeature);
			if (null != a) {
				mRadiusX = a.getDimension(
						R.styleable.PencilShapeFeature_uik_radiusX,
						sDefaultRadius);
				mRadiusY = a.getDimension(
						R.styleable.PencilShapeFeature_uik_radiusY,
						sDefaultRadius);
				
				mTopRatio = a.getFloat(
						R.styleable.PencilShapeFeature_uik_topRatio,
						sTopRatio);
				a.recycle();
			}
		}
		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		mPath = new Path();
		mPath1 = new Path();
		mRectF = new RectF();
	}

	@Override
	public void beforeDraw(Canvas canvas) {
		canvas.saveLayerAlpha(mRectF, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
	}

	@Override
	public void afterDraw(Canvas canvas) {

		canvas.drawPath(mPath, mPaint);
		canvas.drawPath(mPath1, mPaint);
		canvas.restore();
	}

	@Override
	public void beforeOnLayout(boolean changed, int left, int top, int right,
			int bottom) {

	}

	@Override
	public void afterOnLayout(boolean changed, int left, int top, int right,
			int bottom) {

		mRectF.set(0, 0, mHost.getWidth(), mHost.getHeight());

		int w = mHost.getWidth();
		int h = mHost.getHeight();

		mPath.moveTo(w * mTopRatio, 0);

		mPath.lineTo(0, h / 2);

		mPath.lineTo(w * mTopRatio, h);

		mPath.lineTo(w, h);

		mPath.lineTo(w, 0);

		mPath.lineTo(w * mTopRatio, 0);

		mPath.setFillType(Path.FillType.INVERSE_WINDING);

		mPath1.addRoundRect(mRectF, mRadiusX, mRadiusY, Path.Direction.CCW);
		mPath1.setFillType(Path.FillType.INVERSE_WINDING);
	}

	@Override
	public void beforeDispatchDraw(Canvas canvas) {
	}

	@Override
	public void afterDispatchDraw(Canvas canvas) {
	}

	@Override
	public void beforeOnDraw(Canvas canvas) {
	}

	@Override
	public void afterOnDraw(Canvas canvas) {
	}
	
	  /**
     * Set the radius x of the RoundRect.
     * @see #setRadiusY(float)
     */
    public void setRadiusX(float radius)
    {
        mRadiusX = radius;
        invalidateHost();
    }

    /**
     * Set the ratio of top.
     *
     */
    public void setTopRatio(float ratio)
    {
    	mTopRatio = ratio;
        invalidateHost();
    }
    
    /**
     * Set the radius y of the RoundRect.
     * @see #setRadiusX(float)
     */
    public void setRadiusY(float radius)
    {
        mRadiusY = radius;
        invalidateHost();
    }
    
    private void invalidateHost()
    {
        if (null != mHost)
        {
            mHost.invalidate();
        }
    }
}
