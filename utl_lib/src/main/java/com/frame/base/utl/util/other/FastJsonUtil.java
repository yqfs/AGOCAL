package com.frame.base.utl.util.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;

/**
 * 封装 fastjson 的解析 object、list 的方法
 *
 * @author WilliamChik on 15/10/16.
 */
public class FastJsonUtil {

  static {
    // 由于 fastjson 生成反序列化的 asm 代码有问题，需要手动关闭 asm 来规避某些情况下可能出现的 bug
    ParserConfig.getGlobalInstance().setAsmEnable(false);
  }

  /**
   * 将 json 字符串解析为一个 JavaBean
   */
  public static <T> T parseObject(String jsonString, Class<T> cls) {
    T t = null;
    try {
      t = JSON.parseObject(jsonString, cls);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return t;
  }

  /**
   * 将 json 字符串解析为一个 JavaBean，注意此方法支持解析泛型
   */
  public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
    T t = null;
    try {
      t = JSON.parseObject(jsonString, typeReference);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return t;
  }
}
