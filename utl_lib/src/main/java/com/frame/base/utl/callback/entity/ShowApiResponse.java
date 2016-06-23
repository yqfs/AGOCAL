package com.frame.base.utl.callback.entity;

/**
 * <Pre>
 *     易源API通用响应数据
 * </Pre>
 *
 * @author YANGQIYUN
 * @version 1.0
 *          <p/>
 *          Create by 2016/3/1 14:29
 */
public class ShowApiResponse<T> {
    public String code;
    public String msg;
    public T result;
}
