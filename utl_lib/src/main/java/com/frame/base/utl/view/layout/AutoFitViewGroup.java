package com.frame.base.utl.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.utl.R;

import java.util.Hashtable;

/**
 * 根据布局内的子控件宽度自动换行的 LinearLayout，带有默认的行边距和列边距，暂时只支持等高的子视图的添加。
 *
 * @author yangqiyun on 15/9/17.
 */
public class AutoFitViewGroup extends ViewGroup {

  private static final String TAG = AutoFitViewGroup.class.getSimpleName();

  // 内部回调，用于 adapter 的数据集发生变化时通知 AutoFitViewGroup 更新布局
  private InnerCallBack mDataSetCallBack = new InnerCallBack();

  private Adapter mAdapter;

  /** 布局中所有控件的布局坐标集合，通过 onMeasure 方法生成，然后在 onLayout 方法中动态布局 */
  private Hashtable mLayoutPositions = new Hashtable();

  /** 行间距，注意第一行的上方和最后一行的下方是没有间距的 */
  private int mRowMargin;

  /** 列间距，注意第一列的左方和最后一列的右方是没有间距的 */
  private int mColumnMargin;

  public AutoFitViewGroup(Context context) {
    super(context);
  }

  public AutoFitViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AutoFitViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.autoFitViewGroup);
    mRowMargin = typeArray.getDimensionPixelSize(R.styleable.autoFitViewGroup_rowMargin, context.getResources()
        .getDimensionPixelSize(R.dimen.auto_fit_linear_layout_default_row_margin));
    mColumnMargin = typeArray.getDimensionPixelSize(R.styleable.autoFitViewGroup_columnMargin, context.getResources()
        .getDimensionPixelSize(R.dimen.auto_fit_linear_layout_default_column_margin));
    typeArray.recycle();
  }

  public void setAdapter(Adapter<ViewHolder> adapter) {
    mAdapter = adapter;
    if (mAdapter != null) {
      mAdapter.setDataCallBack(mDataSetCallBack);
    }
    updateLayout();
  }

  /**
   * 更新布局
   */
  private void updateLayout() {
    if (mAdapter == null || mAdapter.getItemCount() == 0) {
      return;
    }
    for (int i = 0; i < mAdapter.getItemCount(); i++) {
      ViewHolder vh = mAdapter.onCreateViewHolder();
      mAdapter.onBindViewHolder(vh, i);
      addView(vh.itemView);
    }
  }

  /**
   * 行边距在布局的顶部和底部都会有
   */
  public void setRowMargin(int rowMargin) {
    mRowMargin = rowMargin;
  }

  /**
   * 列边距在布局的左边和右边都会有
   */
  public void setColumnMargin(int columnMargin) {
    mColumnMargin = columnMargin;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 布局的 measure 宽度
    int measureParentWidth = MeasureSpec.getSize(widthMeasureSpec);
    // 布局内部全部子控件实际占用的宽度（布局 measure 宽度减去左右的内边距）
    int totalChildWidth = measureParentWidth - getPaddingLeft() - getPaddingRight();
    // 初始化时子控件的累计X轴长度
    int initLeftTopX = 0;
    // 初始化时子控件的累计Y轴高度
    int initLeftTopY = getPaddingTop();
    // 动态计算的 child view 布局坐标
    int leftPos = 0;
    int rightPos = 0;
    int topPos = 0;
    int bottomPos = 0;
    // 换行所在的子控件索引
    int wrapIndex = 0;

    for (int i = 0; i < getChildCount(); i++) {
      final View child = getChildAt(i);
      // 不指定控件的宽高
      child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
      int childWidth = child.getMeasuredWidth();
      int childHeight = child.getMeasuredHeight();
      // 将每次子控件宽度和子控件列边距进行统计叠加，如果大于设定的宽度则需要换行，高度即Top坐标也需重新设置
      initLeftTopX += childWidth + mColumnMargin;

      leftPos = getLeftPosition(i - wrapIndex, i);
      rightPos = leftPos + childWidth;
      // 换行判断，initLeftTopX 置为当前 View 放在坐边时的右边X，initLeftTopY 增加一个 childHeight 的高度
      if (initLeftTopX >= totalChildWidth) {
        initLeftTopX = childWidth + mColumnMargin;
        initLeftTopY += childHeight + mRowMargin;
        wrapIndex = i;

        leftPos = getPaddingLeft();
        rightPos = leftPos + childWidth;
      }
      topPos = initLeftTopY;
      bottomPos = topPos + childHeight;

      mLayoutPositions.put(child, new LayoutPosition(leftPos, topPos, rightPos, bottomPos));
    }

    // 设置容器控件所占区域大小
    setMeasuredDimension(measureParentWidth, bottomPos + getPaddingBottom());
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);
      LayoutPosition layoutPos = (LayoutPosition) mLayoutPositions.get(child);
      if (layoutPos != null) {
        child.layout(layoutPos.leftPos, layoutPos.topPos, layoutPos.rightPos, layoutPos.bottomPos);
      } else {
        Log.i(TAG, "child layout error");
      }
    }
  }

  /**
   * 递归调用获取当前 View 需要放置的左边X坐标
   *
   * @param IndexInRow View 在该行中的索引
   * @param childIndex View 在整个布局中的索引
   * @return View 需要放置的左边X坐标
   */
  public int getLeftPosition(int IndexInRow, int childIndex) {
    if (IndexInRow > 0) {
      //  当前 View 放置的左边X坐标 = 上个 View 的左边距 + 当前 View 的宽度 + 列边距
      return getLeftPosition(IndexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).getMeasuredWidth() + mColumnMargin;
    }
    return getPaddingLeft();
  }

  /**
   * 记录当前 View 的布局坐标
   */
  private class LayoutPosition {

    public int leftPos;
    public int topPos;
    public int rightPos;
    public int bottomPos;

    public LayoutPosition(int leftPos, int topPos, int rightPos, int bottomPos) {
      this.leftPos = leftPos;
      this.topPos = topPos;
      this.rightPos = rightPos;
      this.bottomPos = bottomPos;
    }
  }

  /**
   * 参照 RecyclerView 的 Adapter 写的 Adapter，用于动态加入布局
   */
  public static abstract class Adapter<VH extends ViewHolder> {

    private InnerCallBack mDataCallBack;

    public abstract VH onCreateViewHolder();

    public abstract void onBindViewHolder(VH holder, int position);

    public abstract int getItemCount();

    public void notifyDataSetChanged() {
      mDataCallBack.onChange();
    }

    public void setDataCallBack(InnerCallBack dataCallBack) {
      mDataCallBack = dataCallBack;
    }
  }

  /**
   * 参照 RecyclerView 的 ViewHolder 写的 ViewHolder
   */
  public static abstract class ViewHolder {

    public final View itemView;

    public ViewHolder(View itemView) {
      if (itemView == null) {
        throw new IllegalArgumentException("itemView may not be null");
      }
      this.itemView = itemView;
    }
  }

  private class InnerCallBack implements CallBack {

    @Override
    public void onChange() {
      // 更新布局
      removeAllViews();
      updateLayout();
    }
  }

  /**
   * 内部回调
   */
  protected interface CallBack {

    /**
     * adapter 数据集发生变化时通知 AutoFitViewGroup 更新布局
     */
    void onChange();
  }
}
