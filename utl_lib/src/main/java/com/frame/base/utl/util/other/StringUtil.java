package com.frame.base.utl.util.other;

import android.text.Html;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtil {
    private static final String EMPTY = "";
    private static final int INDEX_NOT_FOUND = -1;

    /**
     * <p>返回字符串的副本，忽略前导空白和尾部空白，如字符串为null，返回null</p>
     *
     * @param str 字符串的
     * @return 此字符串移除了前导和尾部空白的副本；如果字符串为null，返回null
     */
    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    /**
     * <p>在字符串中查找匹配字符串，如果存在，返回其后的字符串，如果不存在返回""</p>
     *
     * @param str       待查找字符串
     * @param separator 匹配字符串
     * @return 新的字符串
     */
    public static String substringAfter(final String str, final String separator) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>在字符串中查找匹配字符串，如果存在，返回它之前的字符串，如果不存在返回""</p>
     *
     * @param str       待查找的字符串
     * @param separator 匹配字符串
     * @return 结果字符串
     */
    public static String substringBefore(final String str, final String separator) {
        if (TextUtils.isEmpty(str) || separator == null) {
            return str;
        }
        if (TextUtils.isEmpty(separator)) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>将输入的字符串按照分隔符转换为数组</p>
     * <p>如果输入为{@code null} 则返回 {@code null}.</p>
     * <p/>
     *
     * @param str            待分割的字符串，可能为null
     * @param separatorChars 分隔符
     * @return 分割后的数组，如果输入为null，则返回null
     */
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * <p>将输入的字符串按照分隔符转换为数组，分割后的数组大小不超过最大值</p>
     * <p>如果输入为{@code null} 则返回 {@code null}.</p>
     * <p/>
     *
     * @param str            待分割的字符串，可能为null
     * @param separatorChars 分隔符
     * @param max            数组的最大大小
     * @return 分割后的数组，如果输入为null，则返回null
     */
    public static String[] split(final String str, final String separatorChars, final int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that return a maximum array
     * length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChars    the separate character
     * @param max               the maximum number of elements to include in the
     *                          array. A zero or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are
     *                          treated as empty token separators; if {@code false}, adjacent
     *                          separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        final List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 检查字符串相等
     *
     * @param value1
     * @param value2
     * @return
     */
    public static boolean isNotSame(final String value1, final String value2) {
        String v1 = value1 == null ? EMPTY : value1;
        String v2 = value2 == null ? EMPTY : value2;
        return !v1.equals(v2);
    }

    /**
     * @param html
     * @return
     */
    public static String unescapeHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    /**
     * <p>map的join操作</p>
     *
     * @param map           待join的操作
     * @param kvSeparator   map的每一项的连接符
     * @param itemSeparator map条目的连接符
     * @return 拼接后的字符串
     */
    public static String join(Map<String, String> map, String kvSeparator, String itemSeparator) {
        if (map == null) {
            return EMPTY;
        }
        List<String> tempRecord = new ArrayList<String>();
        for (Entry<String, String> entry : map.entrySet()) {
            tempRecord.add(entry.getKey() + kvSeparator + entry.getValue());
        }
        return TextUtils.join(itemSeparator, tempRecord);
    }
}
