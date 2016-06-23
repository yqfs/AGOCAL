package com.frame.base.utl.jump;

/**
 * 面板信息类
 *
 * @author WilliamChik on 15/7/18.
 */
public class PanelInfo {

  // 该等级的panel在整个app中只能有一个（根界面panel）
  public static final int PANEL_LEVEL_ROOT = 0;
  // 该等级的panel在整个app中只能有一个（login界面panel）
  public static final int PANEL_LEVEL_LOGIN = 1;
  // 该等级的panel在整个app中可以有多个
  public static final int PANEL_LEVEL_FIRST = 2;
  // 该等级的panel在整个app中可以有多个
  public static final int PANEL_LEVEL_SECONDARY = 3;
  // 需要强制登陆的等级
  public static final int PANEL_LEVEL_FORCE_LOGIN = 4;
  public static final int PANEL_LEVEL_INVALID = 200;
  public static final int LAUNCH_SINGLE_INSTANCE = 10;

  /**
   * 指定panel在app中的等级：
   * 根界面/入口panel是{@link #PANEL_LEVEL_ROOT}，且只有上述界面的等级是{@link #PANEL_LEVEL_ROOT}；
   * 一级panel是{@link #PANEL_LEVEL_FIRST}
   * 二级panel是{@link #PANEL_LEVEL_SECONDARY}
   */
  public int panelLevel;
  public int panelId;
  public String panelName;
  public String pushName;

  /**
   * 默认构造方法，默认panel的等级是{@link #PANEL_LEVEL_SECONDARY}
   *
   * @param panelId   面板id
   * @param panelName 面板名称（通常用activity的类名表示）
   */
  public PanelInfo(int panelId, String panelName) {
    panelLevel = PANEL_LEVEL_SECONDARY;
    this.panelId = panelId;
    this.panelName = panelName;
  }

  /**
   * @param pushName   push到达名称
   * @param panelLevel panel等级
   */
  public PanelInfo(int panelId, String panelName, String pushName, int panelLevel) {
    this(panelId, panelName);
    this.pushName = pushName;
    this.panelLevel = panelLevel;
  }
}

