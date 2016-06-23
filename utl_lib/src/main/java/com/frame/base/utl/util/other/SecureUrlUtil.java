package com.frame.base.utl.util.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 安全url工具类，提供url抽取，url检查等功能，都是只允许白名单中的阿里系domain
 *
 * @author donghuai.cl
 */
public class SecureUrlUtil {

  /**
   * 阿里系domain白名单
   */
  public static final String[] SECURE_DOMAIN =
      {"etao.com", "tb.cn", "taobao.com", "tmall.com", "tmall.hk", "alipay.com", "laiwang.com", "alibaba.com",
       "alibaba-inc.com", "alimama.com", "aliyun.com", "xiami.com"};

  static {
    Arrays.sort(SECURE_DOMAIN);
  }

  /**
   * domain后缀
   */
  public static final String DOMAIN_SUFFIX = "com|cn|hk|com\\.cn|net";
  /**
   * domain允许包含的字符
   */
  public static final String DOAMIN_CHARACTER = "[a-zA-Z0-9\\-]";

  /**
   * web url的正则表达式
   */
  public static final String WEB_URL_PATTERN_STR =
      "(?:(?:(?:http|https|Http|Https):\\/\\/)?" + "(?:" + DOAMIN_CHARACTER + "{1,64}\\.)*" + "(" + DOAMIN_CHARACTER
      + "{1,64}\\.(?:" + DOMAIN_SUFFIX + ")))" // named host
      + "(?:[\\/|\\?](?:(?:[" + "a-zA-Z0-9" + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
      + "\\-\\.\\+\\!\\*\\'\\(\\)\\_])|(?:\\%[a-fA-F0-9]{2}))*)?";

  /**
   * 抽取url的pattern
   */
  public static final Pattern FIND_URL_PATTERN = Pattern.compile(WEB_URL_PATTERN_STR);
  /**
   * 检查url是否安全的pattern
   */
  public static final Pattern CHECK_URL_PATTERN = Pattern.compile("^" + WEB_URL_PATTERN_STR + "$");

  /**
   * 从字符串中抽取所有的安全url
   *
   * @param str 字符串
   * @return 字符串中包含的所有安全url
   */
  public static List<String> findUrlsfromString(String str) {
    if (str == null) {
      return Collections.emptyList();
    }

    List<String> urls = new ArrayList<String>();

    Matcher matcher = FIND_URL_PATTERN.matcher(str);

    while (matcher.find() && matcher.groupCount() == 1) {
      if (isSecureDomain(matcher.group(1))) {
        urls.add(matcher.group(0));
      }
    }

    return urls;
  }

  /**
   * 检查url是否是安全的url
   *
   * @param url 要检查的url
   * @return url是否安全
   */
  public static boolean isSecureUrl(String url) {
    if (url == null) {
      return false;
    }

    Matcher matcher = CHECK_URL_PATTERN.matcher(url);

    return matcher.find() && matcher.groupCount() == 1 && isSecureDomain(matcher.group(1));

  }

  /**
   * 是否白名单中允许的安全domain
   *
   * @param domain 要检查的domain
   * @return domain是否安全
   */
  public static boolean isSecureDomain(String domain) {
    return Arrays.binarySearch(SECURE_DOMAIN, domain) >= 0;
  }
}
