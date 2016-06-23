package com.taobao.uikit.component;

import com.taobao.uikit.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yanpei on 4-21-14.
 *
 * Displays child views in horizontal, but when the child cannot be displayed fully on width, it will be arranged in another line.
 *
 * <p> To allow users to copy some or all of the BrickLayout's value and paste it somewhere else, set the XML attribute {@link R.styleable#BrickLayout <b>XML attributes</b> <p> See {@link
 * R.styleable#BrickLayout BrickLayout Attributes},
 *
 * @attr ref R.styleable#BrickLayout_brickMaxLines
 * @attr ref R.styleable#BrickLayout_brickGap
 */

public class BrickLayout extends ViewGroup
{

    private int mGap = 0;

    private int mMaxLines = Integer.MAX_VALUE;

    public BrickLayout(Context context)
    {
        this(context, null);
    }

    public BrickLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BrickLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BrickLayout, defStyle, 0);
        if (a != null)
        {
            mMaxLines = a.getInt(R.styleable.BrickLayout_uik_brickMaxLines, mMaxLines);
            mGap = a.getDimensionPixelSize(R.styleable.BrickLayout_uik_brickGap, mGap);
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int count = getChildCount();

        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE)
            {
                BrickLayoutParams lp = (BrickLayoutParams) child.getLayoutParams();

                int childLeft = lp.x;
                int childTop = lp.y;
                child.layout(childLeft, childTop, childLeft + lp.width, childTop + lp.height);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        final int freeSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST);
        int perX = 0;
        int perY = 0;
        int perHeight = 0;
        int lines = 1;
        final int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            final View child = getChildAt(i);
            child.measure(freeSpec, heightMeasureSpec);
            BrickLayoutParams lp = (BrickLayoutParams) child.getLayoutParams();
            if (0 >= lp.height)
            {
                lp.height = child.getMeasuredHeight();
            }
            if (0 >= lp.width)
            {
                lp.width = child.getMeasuredWidth();
            }

            if (totalWidth - perX < lp.width)
            {
                perX = 0;
                perY += perHeight;
                perHeight = 0;
                lines++;
            }
            lp.x = perX;
            lp.y = perY + lp.topMargin;
            perHeight = Math.max(perHeight, lp.height + lp.topMargin + lp.bottomMargin + mGap);
            perX += lp.width + mGap;
            if (lines <= mMaxLines)
            {
                totalHeight = perY + perHeight;
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p)
    {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new BrickLayout.BrickLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void addView(View child, int index, LayoutParams params)
    {
        super.addView(child, index, new BrickLayout.BrickLayoutParams(params));
    }

    public static class BrickLayoutParams extends MarginLayoutParams
    {

        int x;

        int y;

        public BrickLayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            x = 0;
            y = 0;
        }

        public BrickLayoutParams(LayoutParams source)
        {
            super(source);
            x = 0;
            y = 0;
        }

        public BrickLayoutParams(int width, int height)
        {
            super(width, height);
            x = 0;
            y = 0;
        }

    }
}
