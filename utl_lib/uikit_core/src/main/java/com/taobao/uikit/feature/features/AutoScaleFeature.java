package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.LayoutCallback;

/** 使用在TextView上，自适应字号大小
 * Created by kangyong on 14-7-3.
 */
public class AutoScaleFeature extends AbsFeature<TextView> implements LayoutCallback
{
    private float minTextSize = 10f;

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle)
    {
        if (context != null && attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoScaleFeature);
            if (null != a)
            {
                minTextSize = a.getDimension(R.styleable.AutoScaleFeature_uik_minTextSize, 10f);
                a.recycle();
            }
        }
    }

    public void setMinTextSize(float size)
    {
        minTextSize = size;
        getHost().requestLayout();
    }


    @Override
    public void beforeOnLayout(boolean changed, int left, int top, int right, int bottom)
    {

    }

    @Override
    public void afterOnLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int textWidth = getHost().getWidth() - getHost().getPaddingLeft() - getHost().getPaddingRight();
        String text = getHost().getText().toString();

        if (textWidth > 0 && !TextUtils.isEmpty(text))
        {
            Paint textPaint = new Paint(getHost().getPaint());
            textPaint.setTextSize(getHost().getTextSize());
            float trySize = getHost().getTextSize();
            while (trySize > minTextSize)
            {
                if (textPaint.measureText(text) > textWidth)
                {
                    trySize --;
                }
                else
                {
                    break;
                }
                textPaint.setTextSize(trySize);
            }

            getHost().setTextSize(TypedValue.COMPLEX_UNIT_PX,trySize);
        }
    }
}
