package com.frame.base.utl.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.frame.base.utl.R;
import com.frame.base.utl.application.BaseApplication;

/**
 * Created by lxd on 10/12/15.
 * Divider like post stamp in HaigouCang Coupon page.
 */
public class CouponItemDotDivider extends View {

  private int dotStrokeWidth = 1;

  private int width;
  //第一个小圆点的X坐标
  private int startXofLineDot;
  //小圆点数量
  private int lineDotCount;
  //元尺寸参数(通过这些参数可以确定所有大小和位置)
  private int sideDotRadio = (int) (7 * BaseApplication.info.getScreenDensity());
  private int lineDotRadio = (int) (2.5 * BaseApplication.info.getScreenDensity());
  private int dotMargin = (int) (4 * BaseApplication.info.getScreenDensity());

  //view高度
  private int height = sideDotRadio * 2 + dotStrokeWidth * 2;


  private Paint paintStroke;
  private Paint paintFill;

  //Attrs start
  private static final int COLOR_DEFAULT = Color.GRAY;
  private int strokeColor = COLOR_DEFAULT;
  private int fillColor = COLOR_DEFAULT;
  //Attrs End

  public CouponItemDotDivider(Context context) {
    this(context, null);
  }

  public CouponItemDotDivider(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CouponItemDotDivider(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttrs(context, attrs);
    initPaint();
  }


  private void initAttrs(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.couponItemDotDivider);
    for (int i = 0; i < ta.getIndexCount(); i++) {
      int attr = ta.getIndex(i);
      if (attr == R.styleable.couponItemDotDivider_dotFillColor) {
        fillColor = ta.getColor(attr, COLOR_DEFAULT);

      } else if (attr == R.styleable.couponItemDotDivider_dotStrokeColor) {
        strokeColor = ta.getColor(attr, COLOR_DEFAULT);

      }
    }
    ta.recycle();

  }

  private void initDimens() {
    lineDotCount = (int) Math.floor((width - dotMargin - sideDotRadio * 2) / (lineDotRadio * 2 + dotMargin));
    startXofLineDot = (width - sideDotRadio * 2 - lineDotCount * (lineDotRadio * 2 + dotMargin) + dotMargin) / 2 + sideDotRadio;
  }

  private void initPaint() {
    paintStroke = new Paint();
    paintStroke.setColor(strokeColor);
    paintStroke.setAntiAlias(true);
    paintStroke.setStrokeWidth(dotStrokeWidth);
    paintStroke.setStyle(Paint.Style.STROKE);

    paintFill = new Paint();
    paintFill.setColor(fillColor);
    paintFill.setAntiAlias(true);
    paintFill.setStyle(Paint.Style.FILL);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    width = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(width, height);
    initDimens();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    doDraw(canvas, paintStroke);
    doDraw(canvas, paintFill);
  }

  private void doDraw(Canvas canvas, Paint paint) {
    canvas.drawCircle(0, height / 2, sideDotRadio, paint);
    canvas.drawCircle(width, height / 2, sideDotRadio, paint);
    for (int i = 0; i < lineDotCount; i++) {
      int x = startXofLineDot + lineDotRadio + (dotMargin + lineDotRadio * 2) * i;
      canvas.drawCircle(x, height / 2, lineDotRadio, paint);
    }
  }
}
