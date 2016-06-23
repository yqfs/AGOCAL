package com.frame.base.utl.view.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 订制的 WebView
 *
 * @author YANGQIYUN on 15/11/19.
 */
public class BaseWebView extends WebView {

  private WebViewClientListener mWebViewClientListener;

  public void setWebViewClientListener(WebViewClientListener webViewClientListener) {
    mWebViewClientListener = webViewClientListener;
  }

  public BaseWebView(Context context) {
    super(context);
    init();
  }

  public BaseWebView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    WebSettings webSet = getSettings();
    webSet.setJavaScriptEnabled(true);
    webSet.setDomStorageEnabled(true);
    if (Build.VERSION.SDK_INT >= 21) {
      // 5.0 以下 webview 默认允许 mixed content 模式，即 https 页面下可以加载 http 的资源，但 5.0 以上则默认不允许这一模式，
      // 需要手动打开，出于安全考量还是建议服务端的 H5 页面的资源统一改为 https，详见：
      // https://developer.android.com/about/versions/android-5.0-changes.html#BehaviorWebView
      webSet.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }
    webSet.setDefaultTextEncodingName("utf-8");
    // 这是个漏洞，移除系统自带的JS接口，安全问题，详见：http://blog.csdn.net/leehong2005/article/details/11808557
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
      removeJavascriptInterface("searchBoxJavaBridge_");
    }
    setWebChromeClient(new HaiWebChromeClient());
    setWebViewClient(new HaiWebViewClient());
  }

  @Override
  public void destroy() {
    // destroy 前先做一些清除操作
    removeAllViews();
    if (getParent() != null) {
      ((ViewGroup) getParent()).removeView(this);
    }
    super.destroy();
  }

  private class HaiWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      if (mWebViewClientListener != null) {
        mWebViewClientListener.onProgressChanged(view, newProgress);
      }
    }
  }

  private class HaiWebViewClient extends WebViewClient {

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      if (mWebViewClientListener != null) {
        mWebViewClientListener.onPageStarted(view, url, favicon);
      }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      // 安全问题，详见：http://blog.csdn.net/leehong2005/article/details/11808557
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        removeJavascriptInterface("accessibilityTraversal");
        removeJavascriptInterface("accessibility");
      }
      if (mWebViewClientListener != null) {
        mWebViewClientListener.onPageFinished(view, url);
      }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
      // 打开 https 页面时接受证书
      handler.proceed();
    }
  }

  /**
   * 由于自己实现了 WebViewClient 和 WebChromeClient，所以需要给业务方开放对应的接口
   */
  public static class WebViewClientListener {

    /**
     * 对应 {@link WebViewClient#onPageStarted(WebView, String, Bitmap)}
     *
     * @param view    The WebView that is initiating the callback.
     * @param url     The url to be loaded.
     * @param favicon The favicon for this page if it already exists in the
     *                database.
     */
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    /**
     * 对应 {@link WebViewClient#onPageFinished(WebView, String)}
     *
     * @param view The WebView that is initiating the callback.
     * @param url  The url of the page.
     */
    public void onPageFinished(WebView view, String url) {
    }

    /**
     * 对应 {@link WebChromeClient#onProgressChanged(WebView, int)}
     *
     * @param view        The WebView that initiated the callback.
     * @param newProgress Current page loading progress, represented by
     *                    an integer between 0 and 100.
     */
    public void onProgressChanged(WebView view, int newProgress) {
    }
  }
}
