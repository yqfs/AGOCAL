/**
 * Copyright 2013 Romain Guy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taobao.uikit.component;

import com.taobao.uikit.R;
import com.taobao.uikit.utils.svg.SVG;
import com.taobao.uikit.utils.svg.SVGParser;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PathView extends View
{

    private Paint mPaint;

    private int mSvgResource;

    private final Object mLock = new Object();

    private List<PathObj> mInnerPaths = new ArrayList<PathObj>();

    private float mPhase;

    private float mFillTransparency;

    private boolean mPinPath = false;

    private SVG mSVG;

    public PathView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        if (null != attrs)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PathView, defStyle, 0);
            try
            {
                if (a != null)
                {
                    mPaint.setStrokeWidth(a.getDimension(R.styleable.PathView_uik_strokeWidth, 1.0f));
                    mPaint.setColor(a.getColor(R.styleable.PathView_uik_strokeColor, Color.WHITE));
                    mPhase = a.getFloat(R.styleable.PathView_uik_phase, 0.0f);
                }
            }
            finally
            {
                if (a != null)
                {
                    a.recycle();
                }
            }
        }
    }

    private void updatePathsPhaseLocked()
    {
        if (null != mInnerPaths)
        {
            for (PathObj po : mInnerPaths)
            {
                po.paint.setStyle(Paint.Style.STROKE);
                po.paint.setPathEffect(createPathEffect(po.length, mPhase, 0.0f));
            }
        }
    }

    private void updatePathsFillTransparencyLocked()
    {
        if (null != mInnerPaths)
        {
            for (PathObj po : mInnerPaths)
            {
                po.paint.setStyle(Paint.Style.FILL);
                po.paint.setAlpha((int) (mFillTransparency * 255.0f));
            }
        }
    }

    public float getPhase()
    {
        return mPhase;
    }

    public void setPhase(float phase)
    {
        mPhase = phase;
        mPinPath = false;
        synchronized (mLock)
        {
            updatePathsPhaseLocked();
        }
        invalidate();
    }

    public float getFillTransparency()
    {
        return mFillTransparency;
    }

    public void setFillTransparency(float fillTransparency)
    {
        mFillTransparency = fillTransparency;
        mPinPath = true;
        synchronized (mLock)
        {
            updatePathsFillTransparencyLocked();
        }
        invalidate();
    }

    public int getSvgResource()
    {
        return mSvgResource;
    }

    public void setSvgResource(int resId)
    {
        if (mSvgResource != resId)
        {
            mSVG = null;
            mSvgResource = resId;
            mSVG = SVGParser.getSVGFromResource(getResources(), mSvgResource, 0, 0, true);

            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        synchronized (mLock)
        {
            if (null != mSVG)
            {
                obtainPaths(mSVG, w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom());
                if (0f < mFillTransparency)
                {
                    updatePathsFillTransparencyLocked();
                }
                else
                {
                    updatePathsPhaseLocked();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        synchronized (mLock)
        {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());
            if (null != mInnerPaths)
            {
                for (PathObj po : mInnerPaths)
                {
                    canvas.drawPath(po.path, po.paint);
                    if (mPinPath)
                    {
                        canvas.drawPath(po.path, mPaint);
                    }
                }
            }
            canvas.restore();
        }
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[]{pathLength, pathLength}, Math.max(phase * pathLength, offset));
    }

    private void obtainPaths(final SVG svg, final int width, final int height)
    {
        if (null != mInnerPaths)
        {
            mInnerPaths.clear();

            RectF viewBox = svg.getViewBox();
            float scale = Math.min(width / viewBox.width(), height / viewBox.height());

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            matrix.postTranslate((width - viewBox.width() * scale) / 2.0f, (height - viewBox.height() * scale) / 2.0f);
            for (Path path : svg.getPaths())
            {
                Path dst = new Path();
                path.transform(matrix, dst);
                mInnerPaths.add(new PathObj(dst, new Paint(mPaint)));
            }
        }
    }

    @Override protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mPaint = null;

        if (null != mInnerPaths)
        {
            for (PathObj po : mInnerPaths)
            {
                po.paint = null;
                po.path = null;
            }
            mInnerPaths.clear();
            mInnerPaths = null;
        }
        if (null != mSVG)
        {
            mSVG = null;
        }
    }

    public static class PathObj
    {

        Path path;

        Paint paint;

        float length;

        PathObj(Path path, Paint paint)
        {
            this.path = path;
            this.paint = paint;
            PathMeasure measure = new PathMeasure(path, false);
            this.length = measure.getLength();
        }
    }
}
