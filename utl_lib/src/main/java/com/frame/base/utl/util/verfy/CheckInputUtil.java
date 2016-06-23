package com.frame.base.utl.util.verfy;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 检查输入是否合法，手机、密码等
 *
 * @author YANGQIYUN on 15/10/19.
 */
public class CheckInputUtil {


  /**
   * 检查手机号的输入是否合法
   *
   * @param phoneNum 手机号
   * @return true 合法，false 非法
   */
  public static boolean checkPhoneNum(String phoneNum) {
    String pn = phoneNum.trim();
    if (TextUtils.isEmpty(pn)) {
      return false;
    }

    // 匹配数字，长度 11 位
    String patternStr = "^\\d{11}$";
    return Pattern.matches(patternStr, pn);
  }

  /**
   * 检查密码是否合法
   *
   * @param password 密码
   * @return true 合法，false 非法
   */
  public static boolean checkPassword(String password) {
    String pwd = password.trim();
    if (TextUtils.isEmpty(pwd)) {
      return false;
    }

    // 匹配任何字类字符，包括下划线。与“[A-Za-z0-9_]”等效，长度 6 ~ 18 位
    String patternStr = "^\\w{6,18}$";
    return Pattern.matches(patternStr, pwd);
  }

  /**
   * 检查用户名、昵称是否合法
   */
  public static boolean checkNickname(String name){
    if (TextUtils.isEmpty(name.trim())){//不含空格
      return false;
    }
    if (name.trim().contains(" ")){
      return false;
    }

    // 匹配任何字类字符，包括下划线。与“[A-Za-z0-9_]”等效，长度 4~20 位
    String patternStr = "^\\w{4,20}$";
    return Pattern.matches(patternStr, name);
  }

  /**
   * 检测是否有emoji表情
   *
   * @param source
   * @return
   */
  public static boolean containsEmoji(String source) {
    int len = source.length();
    for (int i = 0; i < len; i++) {
      char codePoint = source.charAt(i);
      if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
        return true;
      }
    }
    return false;
  }

  /**
   * 判断是否是Emoji
   *
   * @param codePoint 比较的单个字符
   * @return
   */
  private static boolean isEmojiCharacter(char codePoint) {
    return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
           (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
           ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                                                                && (codePoint <= 0x10FFFF));
  }
}
