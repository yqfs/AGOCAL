package com.frame.base.utl.jump;

import android.view.View;

/**
 * 导航面板接口
 *
 * @author WilliamChik on 15/7/18.
 */
public interface IPanel {

  /**
   * 返回panel id
   */
  int getPanelID();

  /**
   * 返回根布局
   */
  View getRootView();

  /**
   * 返回panel等级
   */
  int getPanelLevel();

  /**
   * 返回panel状态，OK or KILLING
   */
  int getPanelStatus();

  /**
   * 设置panel状态
   */
  void setPanelStatus(int panelstatus);

  // 受PanelManager控制的Panel状态机
  final class PanelStatus {

    public static final int OK = 0;
    public static final int KILLING = 1;
  }
}
