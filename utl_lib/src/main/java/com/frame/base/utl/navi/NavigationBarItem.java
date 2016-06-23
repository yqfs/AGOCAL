package com.frame.base.utl.navi;

import com.frame.base.utl.application.BaseApplication;

/**
 * 导航条信息项
 *
 * @author WilliamChik on 2015/7/23
 */
public class NavigationBarItem {

  private final int NAVI_SELECT_COLOR = BaseApplication.getContext().getResources().getColor(com.frame.base.utl.R.color.app_main_bg_color);

  private final int NAVI_UNSELECT_COLOR = BaseApplication.getContext().getResources().getColor(com.frame.base.utl.R.color.app_font_color_a1);

  // 选中的颜色值
  public int selectedColor;
  // 未选中的颜色值
  public int unSelectedColor;
  // 导航项id
  public int panelId;
  // 导航标题
  public String title;
  // 是否锁定
  public boolean locked;
  // 未选中时的图标资源id
  public int norIconImgId;
  // 选中时的图标资源id
  public int pressIconImgId;
  // 埋点名称
  public String tbsName;
  // 需要显示的数量
  public int infoNum;
  // 特别需要显示的图标
  public int specialImgId;

  public NavigationBarItem(int selectedColor, int unSelectedColor, int panelId, String title, boolean locked,
                              int norIconImgId, int pressIconImgId, String tbsName) {
    this.selectedColor = selectedColor;
    this.unSelectedColor = unSelectedColor;
    this.panelId = panelId;
    this.title = title;
    this.locked = locked;
    this.norIconImgId = norIconImgId;
    this.pressIconImgId = pressIconImgId;
    this.tbsName = tbsName;
    this.infoNum = 0;
    this.specialImgId = 0;
  }

  public NavigationBarItem(int panelId, String title, boolean locked,
                           int norIconImgId, int pressIconImgId, String tbsName) {
    this.selectedColor = NAVI_SELECT_COLOR;
    this.unSelectedColor = NAVI_UNSELECT_COLOR;
    this.panelId = panelId;
    this.title = title;
    this.locked = locked;
    this.norIconImgId = norIconImgId;
    this.pressIconImgId = pressIconImgId;
    this.tbsName = tbsName;
    this.infoNum = 0;
    this.specialImgId = 0;
  }

  public NavigationBarItem(int selectedColor, int unSelectedColor, int panelId, String title, boolean locked,
                           int norIconImgId, int pressIconImgId, String tbsName, int specialImgId) {
    this(selectedColor, unSelectedColor, panelId, title, locked, norIconImgId, pressIconImgId, tbsName);
    this.specialImgId = specialImgId;
  }

  public NavigationBarItem(int selectedColor, int unSelectedColor, int panelId, String title, boolean locked,
                           int norIconImgId, int pressIconImgId, String tbsName, int infoNum, int specialImgId) {
    this(selectedColor, unSelectedColor, panelId, title, locked, norIconImgId, pressIconImgId, tbsName, specialImgId);
    this.infoNum = infoNum;
  }

  /**
   * 0 表示不显示小红点
   * 1 表示显示小红点
   * 其他的就是图片资源文件
   */
  public void setSpecialImageId(int id) {
    specialImgId = id;
  }

  public void hideRedPoint() {
    specialImgId = 0;
  }

  public void showRedPoint() {
    specialImgId = -1;
  }
}
