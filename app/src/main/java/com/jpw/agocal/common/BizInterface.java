package com.jpw.agocal.common;

/**
 * <Pre>
 *     网络请求相关配置
 * </Pre>
 *
 * @author YANGQIYUN
 * @version 1.0
 *          <p/>
 *          Create by 2016/1/27 15:24
 */
public interface BizInterface {
    /**
     * 开发者API密钥
     */
    String API_KEY = "4720bdbcfb3aa457eefd38d2f8fa580f";

    /**
     *首页banner接口
     */
    String HOME_BANNER_URL = "/?ctrl=Goods&act=homeRollPic&m=interface";

    /**
     *我的车辆列表
     */
    String CART_MINE_LIST_URL = "/car/myList";

    /**
     *我的车辆列表
     */
    String ILLAGEL_LIST_URL = "/check/illegal";

    /**
     *我的车辆列表
     */
    String CART_REMOVE_URL = "/car/remove";

    /**
     *车辆信息
     */
    String CART_INFO_URL = "/car/carInfo";

    /**
     *保存新车http://d-apichebao.bishe.com/car/eidtInfo
     */
    String CART_INFO_EDIT_URL = "/car/eidtInfo";


    /**
     *注册
     */
    String LOGIN_REGISTER_URL = "/Login/register";
    /**
     *修改昵称
     */
    String USER_EDIT_INFO_URL = "/Users/editInfo";
    /**
     *修改密码
     */
    String LOGIN_RESETPWD_URL = "/Login/resetpwd";

    /**
     *消息推送设置
     */
    String USER_SET_URL = "/Users/setting";

    /**
     *用户首页
     */
    String USER_INDEX_URL = "/Users/index";
    /**
     *登录
     */
    String LOGIN_LOGIN_URL = "/Login/login";
    /**
     *绑定手机
     */
    String LOGIN_BIND_URL = "/Login/bind";

    /**
     *忘记密码
     */
    String LOGIN_FORGET_URL = "/Login/forget";

    /**
     *短信接口
     */
    String SMS_AUTCODE_URL = "/Sms/sendAutCode";

    /**
     *意见反馈
     */
    String FEEDBACK_OPINION_URL = "/Feedback/opinion";

    /**
     *功能介绍
     */
    String MSG_PUSH_APP_URL = "http://s1.chebao.bishe.com/msg-push-app.html";

    /**
     *用户使用协议
     */
    String AGREEMENT_APP_URL = "http://s1.chebao.bishe.com/agreement-app.html";

    /**
     *用户注册协议
     */
    String REGISTER_ARGREEMENT_APP_URL = "http://s1.chebao.bishe.com/register-argreement-app.html";

    /**
     *banner详情
     */
    String BANNER_DTL_URL = "http://s1.chebao.bishe.com/agreement-app.html";


}
