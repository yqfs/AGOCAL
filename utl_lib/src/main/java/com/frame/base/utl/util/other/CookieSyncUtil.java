package com.frame.base.utl.util.other;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import java.util.List;

/**
 * cookie同步工具类
 *
 * @author YANGQIYUN on 15/8/21.
 */
public class CookieSyncUtil {

  /**
   * 同步cookies到指定的url
   *
   * @param context 上下文
   * @param url     需要带上cookie的url
   */
  public static void syncCookies(Context context, String url) {
    CookieSyncManager.createInstance(context);
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    cookieManager.removeSessionCookie();

    List<String> cookies = getCookieFromCache();
    if (cookies == null || cookies.size() == 0) {
      return;
    }

    for (String cookie : cookies) {
      // setcookie方法每次只能设置一个cookie，一次设置多个的话，个别机型会无法识别
      // 设置复数个cookie项后，cookie会自动以 “;” 拼接在一起
      cookieManager.setCookie(url, cookie);
    }
    CookieSyncManager.getInstance().sync();
  }

  /**
   * 从缓存读取持久化的 cookies 数据
   */
  public static List<String> getCookieFromCache() {
//    return CacheCenterModule.readFromCache(BaseRequestSender.COOKIES_CACHE_KEY, ArrayList.class);
    return null;
  }

}
