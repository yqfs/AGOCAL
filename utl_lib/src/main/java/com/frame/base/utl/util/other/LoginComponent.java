package com.frame.base.utl.util.other;

import android.content.Context;

///**
// * 登录组件，封装登录的一些相关操作，与 {@link LoginActivity} 和 {@link FastLoginActivity} 这两个登录相关的类在同一个包下，且关键的更新用户
// * 数据的方法 {@link #loginSucceed(UserDO)} 是包级私有，只能在包级下调用，避免外部类的破坏性调用更新了用户信息。
// *
// * @author WilliamChik on 15/10/20.
// */
public class LoginComponent {

  // 用户信息的持久化 key
  public static final String LOGIN_USER_CACHE_KEY = "login_user_cache_key";

//  // 内存缓存
//  private UserDO mLoginDO;

  // 用户手动登录回调
  private LoginResult mLoginCallBack;

  // 用户取消登录的回调
  private LoginResult mLoginCancelCallBack;

  private LoginComponent() {
  }

  public static LoginComponent getInstance() {
    return SingletonHolder.instance;
  }

  private static class SingletonHolder {

    public static LoginComponent instance = new LoginComponent();
  }

//  public void init() {
//    mLoginDO = CacheCenterModule.readFromCache(LOGIN_USER_CACHE_KEY, UserDO.class);
//  }
//
//  public UserDO getLoginUser() {
//    return mLoginDO;
//  }

  /**
   * 登录成功时本地持久化用户的登录信息
   * 注意，该方法是包级私有，只能在包级下调用，避免外部类的破坏性调用更新了用户信息
   *
   * @param userDO 用户登录信息
   * @return true 登录成功，false 登录失败
   */
//  boolean loginSucceed(UserDO userDO) {
//    if (userDO == null) {
//      return false;
//    }
//
//    mLoginDO = userDO;
//    return CacheCenterModule.writeObjectToCache(LOGIN_USER_CACHE_KEY, userDO);
//  }

  /**
   * 当前是否有用户登录
   */
  public boolean isLogin() {

    return false;
//    if (mLoginDO == null) {
//      return false;
//    }
//
//    return (!TextUtils.isEmpty(mLoginDO.getToken()));
  }

//  /**
//   * 登出
//   */
//  public boolean logout() {
//    if (mLoginDO == null) {
//      return true;
//    }
//
//    mLoginDO.setToken(null);
//    return CacheCenterModule.writeObjectToCache(LOGIN_USER_CACHE_KEY, mLoginDO);
//  }

  /**
   * 手动登录
   */
  public void login(Context ctx, LoginResult callBack) {
    login(ctx, callBack, null);
  }

  /**
   * 手动登录
   *
   * @param ctx            上下文
   * @param callBack       登录成功时的回调
   * @param cancelCallBack 取消登录时的回调
   */
  public void login(Context ctx, LoginResult callBack, LoginResult cancelCallBack) {
    mLoginCallBack = callBack;
    mLoginCancelCallBack = cancelCallBack;

//    Intent intent = new Intent(ctx, LoginActivity.class);
//    if (!(ctx instanceof Activity)) {
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    }
//
//    ctx.startActivity(intent);
//    if (ctx instanceof Activity) {
//      // 设置 activity 转场动画
//      ((Activity) ctx).overridePendingTransition(R.anim.app_slide_right_in, R.anim.app_slide_left_out);
//    }
  }

  /**
   * 此方法只允许手动登录页面调用
   */
  public void onLoginSuccess() {
    if (mLoginCallBack != null) {
      mLoginCallBack.onLoginResult();
      // 完成回调后置空
      mLoginCallBack = null;
    }
  }

  /**
   * 此方法只允许手动登录页面调用
   */
  public void onLoginCancel() {
    if (mLoginCancelCallBack != null) {
      mLoginCancelCallBack.onLoginResult();
      // 完成回调后置空
      mLoginCancelCallBack = null;
    }
  }

  /**
   * 手动登录回调接口
   */
  public interface LoginResult {

    void onLoginResult();
  }

//  /* 操作非登陆认证信息的方法 start */
//  public boolean setNickname(String nickname) {
////    if (mLoginDO == null) {
////      return false;
////    }
////    mLoginDO.setNick_name(nickname);
////    return CacheCenterModule.writeObjectToCache(LoginComponent.LOGIN_USER_CACHE_KEY, mLoginDO);
//  }
  /* 操作非登陆认证信息的方法 end */
}
