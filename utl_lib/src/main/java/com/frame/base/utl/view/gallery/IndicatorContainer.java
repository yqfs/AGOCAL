package com.frame.base.utl.view.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.frame.base.utl.R;

/**
 * 图片轮播的标示符容器，当图片轮播容器切换图片时会自动高亮该位置的标示符
 *
 * @author YANGQIYUN on 15/10/12.
 */
public class IndicatorContainer extends LinearLayout implements IPagerIndicator {

  /**
   * 标示符的类型，有圆点和矩形两种
   */
  private enum IndicatorType {
    DOT(0),
    RECT(1);

    public int type;

    IndicatorType(int type) {
      this.type = type;
    }
  }

  /** 指示符的类型，目前支持两种：圆点和矩形 */
  private int mIndicatorType = IndicatorType.RECT.type;
  /** 标示符间的间距 */
  private int mIndicatorSpan = 36;
  /** 标示符的宽（当绘制的标示符是圆点时，该变量的意义就是圆点的直径） */
  private float mIndicatorWidth = 32f;
  /** 标示符的高 */
  private float mIndicatorHeight = 4f;
  /** 当前高亮小圆点的索引 */
  private int mCurrentIdx = 0;
  /** 标示符的总数量 */
  private int mTotalNum = 0;
  /** 标示符高亮的颜色 */
  private int mSelectedColor = getResources().getColor(R.color.app_main_color);
  /** 标示符非高亮的颜色 */
  private int mUnSelectedColor = getResources().getColor(R.color.white);


  public IndicatorContainer(Context context) {
    super(context);
  }

  public IndicatorContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    setGravity(Gravity.CENTER_HORIZONTAL);

    TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.IndicatorContainer, 0, 0);
    mIndicatorWidth = arr.getDimension(R.styleable.IndicatorContainer_my_indicator_width, mIndicatorWidth);
    mIndicatorHeight = arr.getDimension(R.styleable.IndicatorContainer_my_indicator_height, mIndicatorHeight);
    mIndicatorSpan = (int) arr.getDimension(R.styleable.IndicatorContainer_my_indicator_span, mIndicatorSpan);
    mSelectedColor = arr.getColor(R.styleable.IndicatorContainer_my_indicator_selected_color, mSelectedColor);
    mUnSelectedColor = arr.getColor(R.styleable.IndicatorContainer_my_indicator_unselected_color, mUnSelectedColor);
    mIndicatorType = arr.getInt(R.styleable.IndicatorContainer_my_dot_type, mIndicatorType);
    arr.recycle();
  }

  @Override
  public final void setNum(int num) {
    if (num < 0) {
      return;
    }

    mTotalNum = num;

    removeAllViews();
    setOrientation(HORIZONTAL);
    for (int i = 0; i < num; i++) {
      LittleIndicator indicator = new LittleIndicator(getContext(), i);
      if (i == 0) {
        indicator.setColor(mSelectedColor);
      } else {
        indicator.setColor(mUnSelectedColor);
      }

      float height;
      if (mIndicatorType == IndicatorType.DOT.type) {
        height = mIndicatorWidth;
      } else {
        height = mIndicatorHeight;
      }
      indicator.setLayoutParams(new LayoutParams((int) (mIndicatorWidth + mIndicatorSpan), (int) height, 1));
      indicator.setClickable(true);
      addView(indicator);
    }
  }

  @Override
  public int getTotal() {
    return mTotalNum;
  }

  @Override
  public int getCurrentIndex() {
    return mCurrentIdx;
  }

  public final void setSelected(int index) {
    if (index >= getChildCount() || index < 0 || mCurrentIdx == index) {
      return;
    }
    if (mCurrentIdx < getChildCount() && mCurrentIdx >= 0) {
      ((LittleIndicator) getChildAt(mCurrentIdx)).setColor(mUnSelectedColor);
    }
    ((LittleIndicator) getChildAt(index)).setColor(mSelectedColor);
    mCurrentIdx = index;
  }

  /**
   * 自定义的标示符，用画布绘制
   */
  private class LittleIndicator extends View {

    private int mColor;
    private Paint mPaint;
    private int mIndex;

    public LittleIndicator(Context context, int index) {
      super(context);
      mPaint = new Paint();
      // 抗锯齿
      mPaint.setAntiAlias(true);
      mIndex = index;
    }

    public int getIndex() {
      return mIndex;
    }

    public void setColor(int color) {
      if (color == mColor) {
        return;
      }
      mColor = color;
      invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      mPaint.setColor(mColor);
      if (mIndicatorType == IndicatorType.DOT.type) {
        canvas.drawCircle((mIndicatorSpan + mIndicatorWidth) / 2, mIndicatorWidth / 2, mIndicatorWidth / 2, mPaint);
      } else {
        canvas.drawRect(0, 0, mIndicatorWidth, mIndicatorHeight, mPaint);
      }
    }
  }
}
