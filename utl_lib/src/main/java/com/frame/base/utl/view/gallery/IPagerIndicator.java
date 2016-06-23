package com.frame.base.utl.view.gallery;

/**
 * 图片轮播容器切换图片时相关操作的回调接口，
 *
 * @author YANGQIYUN on 15/10/12.
 */
public interface IPagerIndicator {

  void setNum(int num);

  int getTotal();

  void setSelected(int index);

  int getCurrentIndex();
}
