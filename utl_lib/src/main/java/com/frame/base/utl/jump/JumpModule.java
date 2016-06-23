package com.frame.base.utl.jump;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.frame.base.utl.application.BaseApplication;

import java.util.HashMap;

/**
 * 跳转逻辑模块
 *
 * @author WilliamChik on 2015/8/21
 */
public class JumpModule {

  private static final String[] whiteDomainList =
      new String[]{"etao.com", "taobao.com", "tmall.com", "tmall.hk", "alipay.com", "laiwang.com", "alibaba.com",
                   "alibaba-inc.com", "alimama.com", "xiami.com", "aliyun.com", "tb.cn"};


//  public static boolean jumpToPageByEtaoSchema(String url) {
//    return jumpToPageByEtaoSchema(url, null);
//  }

//  /**
//   * 通过一淘唤起协议来跳转
//   *
//   * @param url etao 唤起的schema
//   */
//  public static boolean jumpToPageByEtaoSchema(String url, JumpRefer jumpRefer) {
//    if (TextUtils.isEmpty(url)) {
//      return false;
//    }
//    if (url.startsWith("http://")) {
//      jumpToWebview(url, jumpRefer);
//      return true;
//    }
//    String pageName = URLUtil.getPageShortName(url);
//    if (!TextUtils.isEmpty(pageName)) {
//      return jumpToPageByShortName(pageName, url, jumpRefer);
//    }
//    return false;
//  }
//
//  private static void jumpToWebview(String url, JumpRefer jumpRefer) {
//    Bundle bundle = new Bundle();
//    bundle.putString("url", url);
//    PanelManager.getInstance().switchPanel(PanelForm.ID_AUCTION_WEBVIEW, bundle, jumpRefer);
//  }

//  /**
//   * 根据页面短名称和url 进行跳转
//   */
//  public static boolean jumpToPageByShortName(String pageName, String url, JumpRefer jumpRefer) {
//    Map<String, String> map = ScanningHandleModule.parseUri(url);
//    Map<String, String> urlParam = new HashMap<String, String>();
//
//    if (gotoMBGenie(pageName, map)) {
//      return true;
//    }
//
//    String needLogin = map.get("needLogin");
//    if ("1".equals(needLogin)) {
//      if (!LoginInfo.getInstance().isLogin()) {
//        if (TaoApplication.activeActivity != null) {
//          LoginComponent.getInstance().login(TaoApplication.activeActivity, new LoginResultImpl(pageName, url, jumpRefer));
//          return true;
//        }
//      }
//    }
//
//    Class targetClass = (Class) JumpUtil.getTargetClassNameOrId(pageName, JumpUtil.TARGET_CLASS);
//    int targetId = PanelForm.getPanelIdByShortName(pageName);
//
//    if (targetId > 0) {
//
//      if (targetId == PanelForm.ID_AUCTION_WEBVIEW) {
//        try {
//          String tempUrl = URLDecoder.decode(map.get("url"), "utf-8");
//          urlParam = ScanningHandleModule.parseUri(tempUrl);
//          map.put("url", StringUtil.substringBefore(tempUrl, "?"));
//        } catch (UnsupportedEncodingException e) {
//          e.printStackTrace();
//        }
//      }
//
//      Bundle bundle = JumpUtil.getBundleFromMetaAndMap(new Bundle(), (HashMap<String, String>) map, targetClass);
//      if (targetId == PanelForm.ID_AUCTION_WEBVIEW) {
//        String targetUrl = (String) bundle.get("url");
//        String decoderUrl = targetUrl;
//        if (urlParam.containsKey("ttid")) {
//          bundle.putBoolean("ttid", true);
//          urlParam.remove("ttid");
//        }
//        if (urlParam.size() > 0) {
//          decoderUrl = targetUrl + "?" + StringUtil.join(urlParam, "=", "&");
//        }
//        if (!TextUtils.isEmpty(decoderUrl)) {
//          String domain = URLUtil.getDomainFromUrl(decoderUrl);
//          if (TextUtils.isEmpty(domain)) {
//            return false;
//          }
//          if (!validDomain(domain)) {
//            return false;
//          }
//        }
//        bundle.putString("url", decoderUrl);
//      }
//      if (bundle != null) {
//        bundle.putString("src", "saomiao");
//        PanelManager.getInstance().switchPanel(targetId, bundle, jumpRefer);
//        return true;
//      }
//
//    }
//    return false;
//  }

//  /**
//   * 跳转到天猫魔盒
//   */
//  private static boolean gotoMBGenie(String pageName, Map<String, String> map) {
//    if (pageName.equals("MBGenie")) {
//      String targetPage = "";
//      if ("taskList".equals(map.get("target"))) {
//        targetPage = MBGenieSDK.MBGENIEDK_GAIN_CPA_PAGE;
//      } else {
//        targetPage = MBGenieSDK.MBGENIESDK_MAIN_PAGE;
//      }
//
//      if (TaoApplication.activeActivity != null) {
//        MBGenieSDK.startMBGenieActivityWithPage(TaoApplication.activeActivity, targetPage);
//        return true;
//      }
//    }
//    return false;
//  }

//  /**
//   * 跳转到用户反馈
//   */
//  private static boolean goToFeedBack(String pageName) {
//    if (pageName.equals("feedBack")) {
//      if (HaiApplication.activeActivity != null) {
//        Bundle bundle = new Bundle();
//        bundle.putString("url", "http://m.etao.com/myetao/feedback.php");
//        bundle.putString("title", "用户反馈");
////        PanelManager.getInstance().switchPanel(PanelForm.ID_FEED_BACK, bundle, new JumpRefer());
//        return true;
//      }
//    }
//    return false;
//  }

  private static boolean validDomain(String domain) {
    for (String whiteDomain : whiteDomainList) {
      if (domain.equals(whiteDomain)) {
        return true;
      }
    }
    return false;
  }

//  /**
//   * 根据页面ID 和传入的 参数来跳转
//   *
//   * @return true:成功     false: 失败
//   */
//  public static boolean jumpByTargetId(int targetClassId, HashMap<String, String> paramMap, JumpRefer jumpRefer) {
//    Class targetClass = (Class) JumpUtil.getTargetClassById(targetClassId);
//    if (targetClass != null) {
//      Bundle bundle = new Bundle();
//      bundle = JumpUtil.getBundleFromMetaAndMap(bundle, paramMap, targetClass);
//      if (bundle != null) {
//        PanelManager.getInstance().switchPanel(targetClassId, bundle, jumpRefer);
//        return true;
//      }
//    }
//    return false;
//  }

//  /**
//   * 根据页面ID 和传入的 参数来跳转
//   *
//   * @return true:成功     false: 失败
//   */
//  public static boolean jumpByTargetId(int targetClassId, HashMap<String, String> paramMap) {
//    Class targetClass = (Class) JumpUtil.getTargetClassById(targetClassId);
//    if (targetClass != null) {
//      Bundle bundle = new Bundle();
//      bundle = JumpUtil.getBundleFromMetaAndMap(bundle, paramMap, targetClass);
//      if (bundle != null) {
//        PanelManager.getInstance().switchPanel(targetClassId, bundle, new JumpRefer());
//        return true;
//      }
//    }
//    return false;
//  }

  /**
   * 打开非HTTP HTTPS 的协议
   */
  public static void openAppSchama(Activity activity, boolean needFinish, String url) {
    try {
      Intent intent = new Intent();
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setAction("android.intent.action.VIEW");
      Uri CONTENT_URI_BROWSERS = Uri.parse(url);
      intent.setData(CONTENT_URI_BROWSERS);
      BaseApplication.getContext().startActivity(intent);
      if (needFinish) {
        if (activity != null) {
          activity.finish();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
//
//  /**
//   * 打开非HTTP HTTPS 的协议
//   */
//  public static void openAppSchama(String url) {
//    try {
//      openAppSchama(null, false, url);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

//  /**
//   * 登陆回调操作
//   */
//  private static class LoginResultImpl implements LoginComponent.LoginResult {
//
//    private String mPageName;
//    private String mUrl;
//    private JumpRefer mJumpRefer;
//
//    public LoginResultImpl(String pageName, String url, JumpRefer jumpRefer) {
//      mPageName = pageName;
//      mUrl = url;
//      mJumpRefer = jumpRefer;
//    }
//
//    @Override
//    public void onLoginResult() {
//      if (LoginInfo.getInstance().isLogin()) {
//        jumpToPageByShortName(mPageName, mUrl, mJumpRefer);
//      } else {
//
//      }
//    }
//  }


}

