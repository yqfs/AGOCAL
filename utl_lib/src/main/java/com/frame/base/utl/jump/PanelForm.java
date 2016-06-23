package com.frame.base.utl.jump;

/**
 * 面板表格，用于组织面板间跳转
 *
 * @author WilliamChik on 15/7/18.
 */
public abstract class PanelForm {
  // 首页
  public static final int ID_HOME = 3;

  public static PanelInfo[] panelform;


  public static String getPanelName(int panelId) {
    for (PanelInfo p : panelform) {
      if (panelId == p.panelId) {
        return p.panelName;
      }
    }

    return panelform[0].panelName;
  }

  public static int getPanelIdByPanelName(String className) {
    for (PanelInfo a : panelform) {
      if (className.equals(a.panelName)) {
        return a.panelId;
      }
    }
    return -1;
  }

  public static int getPanelIdByShortName(String className) {
    for (PanelInfo a : panelform) {
      if (className.equals(a.pushName)) {
        return a.panelId;
      }
    }
    return -1;
  }

  public static int getPanelLevel(int panelId) {
    for (PanelInfo a : panelform) {
      if (panelId == a.panelId) {
        return a.panelLevel;
      }
    }

    return PanelInfo.PANEL_LEVEL_INVALID;
  }

  public abstract void initPanelform();
}
