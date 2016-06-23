package com.frame.base.utl.view.web;

import android.webkit.JavascriptInterface;

/**
 * JS 桥接工具，定义各种 H5 需要的 jsbrige 接口
 *
 * @author YANGQIYUN on 15/11/19.
 */
public class JsbridgeUtil {

  /**
   * 打开对应商品的详情页
   *
   * @param goods_id 商品 id
   */
  @JavascriptInterface
  public void openDetails(String goods_id) {
//    if (!TextUtils.isEmpty(goods_id)) {
//      Bundle bundle = new Bundle();
//      bundle.putString(Constants.DETAIL_GOODS_ID, goods_id);
//      PanelManager.getInstance().switchPanel(PanelForm.ID_GOOD_DETAIL, bundle, null);
//    }
  }

//  /**
//   * 打开代金券页面
//   */
//  @JavascriptInterface
//  public void openMyCoupon() {
//    PanelManager.getInstance().switchPanel(PanelForm.ID_MY_COUPON, null, null);
//  }
//
//  /**
//   * 返回平台数据
//   */
//  @JavascriptInterface
//  public String getPlatform() {
//    return "android";
//  }
//
//  /**
//   * 关闭当前页面
//   */
//  @JavascriptInterface
//  public void finish() {
//    PanelManager.getInstance().back();
//  }
//
//  /**
//   * 打开品牌产品列表
//   *
//   * @param brandId 品牌 id
//   */
//  @JavascriptInterface
//  public void openBrandList(String brandId) {
//    if (!TextUtils.isEmpty(brandId)) {
//      Bundle bundle = new Bundle();
//      bundle.putString(Constants.CATEGORY_BRAND_ID, brandId);
//      PanelManager.getInstance().switchPanel(PanelForm.ID_BRAND_LIST, bundle, null);
//    }
//  }
}
