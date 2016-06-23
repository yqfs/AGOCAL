package com.frame.base.utl.util.other;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * 提供 I/O 相关的处理
 *
 * @author YANGQIYUN on 15/11/24.
 */
public class IoUtil {

  /** 默认缓冲区大小 {@value} */
  public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB

  /**
   * 读取流的所有数据，并关闭流
   *
   * @param is 输入流
   */
  public static void readAndCloseStream(InputStream is) {
    if (is == null) {
      return;
    }

    final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
    try {
      while (is.read(bytes, 0, DEFAULT_BUFFER_SIZE) != -1) {
      }
    } catch (IOException ignored) {
    } finally {
      closeCloseable(is);
    }
  }

  /**
   * 关闭指定的 {@link Closeable}，如 InputSteam 和 OutputStream 等
   */
  public static void closeCloseable(Closeable closeable) {
    if (closeable == null) {
      return;
    }

    try {
      closeable.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
