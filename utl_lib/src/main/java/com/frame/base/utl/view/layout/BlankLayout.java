package com.frame.base.utl.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.utl.R;
import com.frame.base.utl.jump.PanelForm;
import com.frame.base.utl.jump.PanelManager;

/**
 * 统一的空白布局
 *
 * @author WilliamChik on 15/11/4.
 */
public class BlankLayout extends LinearLayout {

  // 空白文本
  private TextView mBlankTv;

  // 空白按钮
  private TextView mBlankBtn;

  private OnBlankButtonClickListener mOnBlankButtonClickListener;

  public BlankLayout(Context context) {
    super(context);
    init(context);
  }

  public BlankLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public BlankLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setOnBlankButtonClickListener(OnBlankButtonClickListener onBlankButtonClickListener) {
    mOnBlankButtonClickListener = onBlankButtonClickListener;
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.activity_blank_layout, this);
    mBlankTv = (TextView) findViewById(R.id.tv_blank_layout_txt);
    mBlankBtn = (TextView) findViewById(R.id.tv_black_layout_btn);
    mBlankBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnBlankButtonClickListener != null) {
          mOnBlankButtonClickListener.OnBlankButtonClick();
        } else {
          PanelManager.getInstance().switchPanel(PanelForm.ID_HOME, null, null);
        }
      }
    });
  }

  /**
   * 设置空白文案
   *
   * @param txtResId 文案资源 id
   */
  public void setBlankText(int txtResId) {
    mBlankTv.setText(txtResId);
  }

  /**
   * 设置空白文案
   *
   * @param text 文案文本
   */
  public void setBlankText(String text) {
    mBlankTv.setText(text);
  }

  public interface OnBlankButtonClickListener {

    void OnBlankButtonClick();
  }

  /**
   * 设置按钮文案
   * @param text
   */
  public void setBtnText(String text){
    mBlankBtn.setText(text);
  }

  public void setBtnText(int textResId){
    mBlankBtn.setText(textResId);
  }

  /**
   * 隐藏按钮
   */
  public void hideBlankBtn() {
    mBlankBtn.setVisibility(INVISIBLE);
  }

}
