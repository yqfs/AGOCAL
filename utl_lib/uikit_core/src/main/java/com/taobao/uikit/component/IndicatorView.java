package com.taobao.uikit.component;


import com.taobao.uikit.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author yanpei
 * @version 创建时间：2013-5-16 下午8:48:22 类说明
 */
public class IndicatorView extends View
{

    private Paint mPaint;

    private int mTotal = 1;

    private int mIndex = 0;

    private int mFocusColor = Color.parseColor("#ff5000");

    private int mUnfocusColor = Color.parseColor("#7fffffff");

    private int mStrokeColor = Color.parseColor("#7f666666");

    private int mRadius = 4;

    private int mDiameter = mRadius * 2;

    private int mGapMargin = 8;

    private float mStrokeWidth = 1.0f;

    public IndicatorView(Context context)
    {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
            mFocusColor = a.getColor(R.styleable.IndicatorView_uik_focusColor, mFocusColor);
            mUnfocusColor = a.getColor(R.styleable.IndicatorView_uik_unfocusColor, mUnfocusColor);
            mStrokeColor = a.getColor(R.styleable.IndicatorView_uik_strokeColor, mStrokeColor);
            mStrokeWidth = a.getDimension(R.styleable.IndicatorView_uik_strokeWidth, mStrokeWidth);
            mPaint.setStrokeWidth(mStrokeWidth);
            mTotal = a.getInt(R.styleable.IndicatorView_uik_total, 1);
            mIndex = a.getInt(R.styleable.IndicatorView_uik_index, 0);
            judgeIndex();

            mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, context.getResources().getDisplayMetrics());
            mRadius = a.getDimensionPixelSize(R.styleable.IndicatorView_uik_indicatorRadius, mRadius);
            mDiameter = mRadius * 2;
            mGapMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mGapMargin, context.getResources().getDisplayMetrics());
            mGapMargin = a.getDimensionPixelSize(R.styleable.IndicatorView_uik_gapMargin, mGapMargin);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = Math.max(getDesiredWidth(), widthSize);
        }
        else
        {
            int desired = getDesiredWidth();
            width = desired;
            if (widthMode == MeasureSpec.AT_MOST)
            {
                width = Math.min(desired, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = Math.max(getDesiredHeight(), heightSize);
        }
        else
        {
            int desired = getDesiredHeight();
            height = desired;
            if (heightMode == MeasureSpec.AT_MOST)
            {
                height = Math.min(desired, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    private int getDesiredHeight()
    {
        return mDiameter + getPaddingTop() + getPaddingBottom();
    }

    private int getDesiredWidth()
    {
        return mTotal * mDiameter + (mTotal - 1) * mGapMargin + getPaddingLeft() + getPaddingRight();
    }

    private void judgeIndex()
    {
        if (0 > mIndex)
        {
            mIndex = 0;
        }

        if ((mTotal - 1) < mIndex)
        {
            mIndex = mTotal - 1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.TRANSPARENT);
        if (mTotal > 1)
        {
            int cx = mRadius + getPaddingLeft();
            int cy = mRadius + getPaddingTop();
            for (int i = 0; i < mTotal; i++)
            {
                if (i == mIndex)
                {
                    mPaint.setColor(mFocusColor);
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(cx + i * (mDiameter + mGapMargin), cy, mRadius, mPaint);
                }
                else
                {
                    mPaint.setColor(mUnfocusColor);
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(cx + i * (mDiameter + mGapMargin), cy, mRadius, mPaint);
                    mPaint.setColor(mStrokeColor);
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(cx + i * (mDiameter + mGapMargin), cy, mRadius - 0.5f * mStrokeWidth, mPaint);
                }
            }
        }
    }

    public int getTotal()
    {
        return mTotal;
    }

    public void setTotal(int total)
    {
        this.mTotal = total < 1 ? 1 : total;
        requestLayout();
        invalidate();
    }

    public int getIndex()
    {
        return mIndex;
    }

    public void setIndex(int index)
    {
        this.mIndex = index;
        judgeIndex();
        invalidate();
    }

    public void setFocusColor(int focusColor)
    {
        this.mFocusColor = focusColor;
    }

    public void setUnfocusColor(int unfocusColor)
    {
        this.mUnfocusColor = unfocusColor;
    }

    public void setRadius(int radius)
    {
        this.mRadius = radius;
    }

    public int getGapMargin()
    {
        return mGapMargin;
    }

    public void setGapMargin(int gapMargin)
    {
        this.mGapMargin = gapMargin;
    }
}
