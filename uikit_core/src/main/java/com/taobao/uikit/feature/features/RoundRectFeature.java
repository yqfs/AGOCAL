package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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

/**
 * Created by yanpei on 4-16-14.
 */
public class RoundRectFeature extends AbsFeature<View> implements CanvasCallback, LayoutCallback
{

    private static float sDefaultRadius = 6;

    private float mRadiusX = sDefaultRadius;

    private float mRadiusY = sDefaultRadius;

    private Paint mPaint;

    private Path mPath;

    private RectF mRectF;

    private boolean mFastEnable = false;

    private boolean mStrokeEnable = false;

    private Path mStrokePath;

    private Paint mStrokePaint;

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        int fastColor = Color.WHITE;
        int strokeColor = Color.GRAY;
        float strokeWidth = 1.0f;
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectFeature);
            if (null != a)
            {
                mRadiusX = a.getDimension(R.styleable.RoundRectFeature_uik_radiusX, sDefaultRadius);
                mRadiusY = a.getDimension(R.styleable.RoundRectFeature_uik_radiusY, sDefaultRadius);
                mFastEnable = a.getBoolean(R.styleable.RoundRectFeature_uik_fastEnable, false);
                mStrokeEnable = a.getBoolean(R.styleable.RoundRectFeature_uik_strokeEnable, false);
                fastColor = a.getColor(R.styleable.RoundFeature_uik_fastColor, fastColor);
                strokeColor = a.getColor(R.styleable.RoundRectFeature_uik_strokeColor, strokeColor);
                strokeWidth = a.getDimension(R.styleable.RoundRectFeature_uik_strokeWidth, strokeWidth);
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

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(strokeColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(strokeWidth);

        mPath = new Path();
        mStrokePath = new Path();
        mRectF = new RectF();
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
        if (mStrokeEnable)
        {
            canvas.drawPath(mStrokePath, mStrokePaint);
        }

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
        mRectF.set(0, 0, mHost.getMeasuredWidth(), mHost.getMeasuredHeight());
        mPath.addRoundRect(mRectF, mRadiusX, mRadiusY, Path.Direction.CCW);
        mPath.setFillType(Path.FillType.INVERSE_WINDING);
        mStrokePath.addRoundRect(mRectF, mRadiusX, mRadiusY, Path.Direction.CCW);
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
     * Set the radius x of the RoundRect.
     * @see #setRadiusY(float)
     */
    public void setRadiusX(float radius)
    {
        mRadiusX = radius;
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
     * Set the enabled state of stroke mode. If true, it will draw a stroke.
     *
     * @param strokeEnable True if stroke mode is enabled, false otherwise.
     * @see #setStrokeColor(int)
     * @see #setStrokeWidth(float)
     */
    public void setStrokeEnable(boolean strokeEnable)
    {
        mStrokeEnable = strokeEnable;
        invalidateHost();
    }

    /**
     * Set the color of stroke mode.
     *
     * @see #setStrokeEnable(boolean)
     */
    public void setStrokeColor(int strokeColor)
    {
        mStrokePaint.setColor(strokeColor);
        invalidateHost();
    }

    /**
     * Set the width of stroke.
     *
     * @see #setStrokeEnable(boolean)
     */
    public void setStrokeWidth(float strokeWidth)
    {
        mStrokePaint.setStrokeWidth(strokeWidth);
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
   		// TODO Auto-generated method stub
   		super.setHost(host);
   		host.requestLayout();
   	}
}
