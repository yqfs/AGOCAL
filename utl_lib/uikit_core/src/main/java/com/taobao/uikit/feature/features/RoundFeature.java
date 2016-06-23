package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;

/**
 * Created by yanpei on 4-16-14.
 */
public class RoundFeature extends AbsFeature<View> implements CanvasCallback, LayoutCallback
{

    private Paint mPaint;

    private Drawable mShadowDrawable;

    private Rect mRectOut;

    private Path mPath;

    /**
     * PathExtraA/B/C/D is aimed for layerType == hardware
     */
    private Path mPathExtraA;

    private Path mPathExtraB;

    private Path mPathExtraC;

    private Path mPathExtraD;

    private Rect mRectOld;

    private RectF mRectF;

    private int mShadowOffset;

    private int mShadowOffsetPixel;

    private boolean mFastEnable = false;

    private float mRadius = 0;
    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        int fastColor = Color.WHITE;
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundFeature);
            if (null != a)
            {
                mShadowDrawable = a.getDrawable(R.styleable.RoundFeature_uik_shadowDrawable);
                mShadowOffset = a.getDimensionPixelOffset(R.styleable.RoundFeature_uik_shadowOffset, 0);
                mFastEnable = a.getBoolean(R.styleable.RoundFeature_uik_fastEnable, false);
                fastColor = a.getColor(R.styleable.RoundFeature_uik_fastColor, fastColor);
                
                mRadius = a.getDimensionPixelOffset(R.styleable.RoundFeature_uik_radius, 0);
                a.recycle();
            }
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(fastColor);
        if (!mFastEnable)
        {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        mRectF = new RectF();
        mRectOut = new Rect();
        mPath = new Path();
        mRectOld = new Rect();
        mPathExtraA = new Path();
        mPathExtraB = new Path();
        mPathExtraC = new Path();
        mPathExtraD = new Path();
    }

    @Override
    public void beforeDraw(Canvas canvas)
    {
        if (!mFastEnable)
        {
            canvas.saveLayerAlpha(mRectF, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        }
    }

    @Override
    public void afterDraw(Canvas canvas)
    {
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPathExtraA, mPaint);
        canvas.drawPath(mPathExtraB, mPaint);
        canvas.drawPath(mPathExtraC, mPaint);
        canvas.drawPath(mPathExtraD, mPaint);
        drawShadow(canvas, mRectOut);
        if (!mFastEnable)
        {
            canvas.restore();
        }
    }

    @Override public void beforeOnLayout(boolean changed, int left, int top, int right, int bottom)
    {
    }

    @Override public void afterOnLayout(boolean changed, int left, int top, int right, int bottom)
    {
    	update();
    }

	private void update() {
		if(mHost == null){
			return;
		}
		float w = (float) mHost.getMeasuredWidth();
		float h = (float) mHost.getMeasuredHeight();
		float r = w > h ? h / 2 : w / 2;

		float offset = mShadowOffsetPixel;
		if (mRadius > 0 && mRadius < r) {
			offset += r - mRadius;
			r = mRadius;
		}
		calcRect(mRectOut);
		mRectF.set(0, 0, w, h);
		mPath.reset();
		mPathExtraA.reset();
		mPathExtraB.reset();
		mPathExtraC.reset();
		mPathExtraD.reset();
		
		mPath.addCircle(w / 2, h / 2, r - mShadowOffsetPixel,
				Path.Direction.CCW);
		mPath.setFillType(Path.FillType.INVERSE_WINDING);
		if (h > w) {
			mPathExtraA.addRect(0.0f, 0.0f, w, (h - w) / 2 + offset,
					Path.Direction.CW);
			mPathExtraB.addRect(0.0f, (h + w) / 2 - offset, w, h,
					Path.Direction.CW);
			mPathExtraC.addRect(0.0f, (h - w) / 2 + offset, offset, (h + w) / 2
					- offset, Path.Direction.CW);
			mPathExtraD.addRect(w - offset, (h - w) / 2 + offset, w, (h + w)
					/ 2 - offset, Path.Direction.CW);
		} else {
			mPathExtraA.addRect(0.0f, 0.0f, (w - h) / 2 + offset, h,
					Path.Direction.CW);
			mPathExtraB.addRect((h + w) / 2 - offset, 0.0f, w, h,
					Path.Direction.CW);
			mPathExtraC.addRect((w - h) / 2 + offset, 0.0f, (h + w) / 2
					- offset, offset, Path.Direction.CW);
			mPathExtraD.addRect((w - h) / 2 + offset, h - offset, (h + w) / 2
					- offset, h, Path.Direction.CW);
		}
	}
    private void calcRect(Rect out)
    {
        if (null != mShadowDrawable)
        {
            final float ratio = (float) mShadowDrawable.getIntrinsicWidth() / mShadowDrawable.getIntrinsicHeight();
            int origWidth = mHost.getMeasuredWidth();
            int origHeight = mHost.getMeasuredHeight();
            int left = 0;
            int top = 0;
            int right = origWidth;
            int bottom = origHeight;
            int scaledWidth = (int) (ratio * origHeight);
            if (scaledWidth <= origWidth)
            {
                left = (origWidth - scaledWidth) / 2;
                right = left + scaledWidth;
            }
            else
            {
                int scaledHeight = (int) (origWidth / ratio);
                top = (origHeight - scaledHeight) / 2;
                bottom = top + scaledHeight;
            }

            mShadowOffsetPixel = (right - left) * mShadowOffset / mShadowDrawable.getIntrinsicWidth();
            out.set(left, top, right, bottom);
        }
        else
        {
            out.set(0, 0, 0, 0);
        }
    }

    private void drawShadow(Canvas canvas, Rect rect)
    {
        if (null != mShadowDrawable)
        {
            mRectOld.set(mShadowDrawable.getBounds());
            mShadowDrawable.setBounds(rect);
            mShadowDrawable.draw(canvas);
            mShadowDrawable.setBounds(mRectOld);
        }
    }

    @Override
    public void beforeDispatchDraw(Canvas canvas)
    {
    }

    @Override
    public void afterDispatchDraw(Canvas canvas)
    {
    }

    @Override
    public void beforeOnDraw(Canvas canvas)
    {
    }

    @Override
    public void afterOnDraw(Canvas canvas)
    {
    }

    /**
     * Set the enabled state of fast mode. If true, canvas will not use alpha layer.
     *
     * @param fastEnable True if fast mode is enabled, false otherwise.
     * @see #setFastColor(int)
     */
    public void setFastEnable(boolean fastEnable)
    {
        mFastEnable = fastEnable;
        if (!mFastEnable)
        {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else
        {
            mPaint.setXfermode(null);
        }
        invalidateHost();
    }

    /**
     * Set the color of fast mode.
     *
     * @see #setFastEnable(boolean)
     */
    public void setFastColor(int fastColor)
    {
        mPaint.setColor(fastColor);
        invalidateHost();
    }

    /**
     * Set the drawable of shadow, can be NULL. if NOT NULL, the drawable will be drawn after Image been drawn.
     *
     * @see #setShadowOffset(int)
     */
    public void setShadowDrawable(Drawable drawable)
    {
        mShadowDrawable = drawable;
        invalidateHost();
    }

    /**
     * Set the offset of shadow. It is the radius delta between Image and shadow drawable.
     *
     * @see #setShadowDrawable(Drawable)
     */
    public void setShadowOffset(int offset)
    {
        mShadowOffset = offset;
        invalidateHost();
    }


    private void invalidateHost()
    {
        if (null != mHost)
        {
            mHost.invalidate();
        }
    }
    
    @Override
	public void setHost(View host) {
		super.setHost(host);
		host.requestLayout();
	}
    
    public void setRadius(float radius){
    	if(radius != mRadius){
    		mRadius = radius;
    		update();
    		this.invalidateHost();
    	}
    }
    
    public float getRadius(){
    	return mRadius;   	
    }
}
