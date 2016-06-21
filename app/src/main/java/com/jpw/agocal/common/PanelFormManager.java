package com.jpw.agocal.common;

import com.frame.base.utl.jump.PanelForm;
import com.frame.base.utl.jump.PanelInfo;
import com.jpw.agocal.MainActivity;
/**
 * Created by YANGQIYUN on 2016/5/19.
 */
public class PanelFormManager extends PanelForm {

    //注意 这里的 panel id 必须和下面 panelform 表中的各个panel的index一致，pannel id 都大于 0
    // 欢迎页
    public static final int ID_WELCOME = 1;
    public static final int ID_MAIN = 2;

    // 首页
    public static final int ID_HOME = 3;
    // 我的
    public static final int ID_MINE = 4;

    // 我的车辆列表
    public static final int ID_CART_LIST = 5;
    // 我的车辆列表
    public static final int ID_CART_ADD = 6;

    //登录
    public static final int ID_LOGIN = 7;
    //注册
    public static final int ID_REGISTER = 8;
    //忘记密码
    public static final int ID_FORGET_PSW = 9;
    //修改密码
    public static final int ID_RESET_PSW = 10;
    //修改密码通过手机
    public static final int ID_RESET_PSW_BY_PHONE = 11;

    //个人中心
    public static final int ID_PERSONAL_CENTER = 12;
    //个性设置
    public static final int ID_MINE_SETTING = 13;
    //意见反馈
    public static final int ID_SETTING_FEEDBACK = 14;
    //昵称
    public static final int ID_NICK_NAME = 15;
    //消息推送
    public static final int ID_PUSH_MSG = 16;
    //绑定手机
    public static final int ID_BIND_PHONE = 17;

    //违章信息列表
    public static final int ID_ILLAGEL_LIST = 18;

    //城市选择器
    public static final int ID_CITY_SELECTOR = 19;

    //用户协议
    public static final int ID_MSG_PUSH_APP = 20;

    //功能介绍
    public static final int ID_AGREEMENT_APP = 21;

    public PanelFormManager getInstance(){
        if(panelform == null){
            initPanelform();
        }
        return this;
    }


    private static int killPathForm[][] = {{ID_WELCOME, ID_MAIN},};

    @Override
    public void initPanelform() {
        panelform = new PanelInfo[]{
                new PanelInfo(ID_HOME, MainActivity.class.getName(), "home", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_MINE, MainActivity.class.getName(), "mine", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_CART_LIST, MainActivity.class.getName(), "carlist", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_LOGIN, MainActivity.class.getName(), "login", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_REGISTER, MainActivity.class.getName(), "register", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_PERSONAL_CENTER, MainActivity.class.getName(), "personal", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_MINE_SETTING, MainActivity.class.getName(), "minesetting", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_ILLAGEL_LIST, MainActivity.class.getName(), "illagellist", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_CART_ADD, MainActivity.class.getName(), "addcart", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_SETTING_FEEDBACK, MainActivity.class.getName(), "feedback", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_NICK_NAME, MainActivity.class.getName(), "nickname", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_FORGET_PSW, MainActivity.class.getName(), "forgetpsw", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_PUSH_MSG, MainActivity.class.getName(), "pushmsg", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_BIND_PHONE, MainActivity.class.getName(), "bindphone", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_RESET_PSW, MainActivity.class.getName(), "resetpswbyoriginal", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_RESET_PSW_BY_PHONE, MainActivity.class.getName(), "resetpswbyphone", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_CITY_SELECTOR, MainActivity.class.getName(), "cityselector", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_MSG_PUSH_APP, MainActivity.class.getName(), "apppresent", PanelInfo.PANEL_LEVEL_FIRST),
                new PanelInfo(ID_AGREEMENT_APP, MainActivity.class.getName(), "useragreementActivity", PanelInfo.PANEL_LEVEL_FIRST),
        };
    }
}
