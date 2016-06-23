package com.frame.base.utl.jump;


import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;

import com.frame.base.utl.application.BaseApplication;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 页面跳转工具
 *
 * @author YANGQIYUN on 2015/7/27
 */
public class JumpUtil {

  public static final int TARGET_CLASS = 1;
  public static final int TARGET_ID = 2;
  private static final String HAI_PACKAGE_NAME = "com.aiyaya.haigoucang";

  /**
   * 根据页面I别名获取class
   *
   * @return 如果根据id匹配不到class，则返回null
   */
  public static Class getTargetClassByName(String name) {
    if (name == null) {
      return null;
    }

    for (PanelInfo panel : PanelForm.panelform) {
        try {
          return Class.forName(panel.panelName);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
          return null;
        }
    }

    return null;
  }

  /**
   * 根据页面ID获取class
   *
   * @return 如果根据id匹配不到class，则返回null
   */
  public static Class getTargetClassById(int panelId) {
    if (panelId <= 0) {
      return null;
    }

    for (PanelInfo panel : PanelForm.panelform) {
      if (panelId == panel.panelId) {
        try {
          return Class.forName(panel.panelName);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
          return null;
        }
      }
    }

    return null;
  }

  /**
   * 根据panelINFO和页面别名获取目标页面
   */
  public static Object getTargetClassNameOrId(String target, int type) {
    if (TextUtils.isEmpty(target)) {
      return null;
    }

    int targetClassId = 0;
    Class<?> targetClass = null;

    for (PanelInfo panel : PanelForm.panelform) {
      String pushName = panel.pushName;
      if (!TextUtils.isEmpty(pushName)) {
        if (pushName.equals(target)) {
          try {
            targetClass = Class.forName(panel.panelName);
            targetClassId = panel.panelId;
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
    }

    if (type == JumpUtil.TARGET_CLASS) {
      return targetClass;
    } else if (type == JumpUtil.TARGET_ID) {
      return targetClassId;
    }
    return null;
  }

  /**
   * 从map获取参数，拼装成Bundle
   */
  public static Bundle getBundleFromMetaAndMap(Bundle bundle, Map<String, String> paramMap, Class targetClass) {
    ComponentName com = new ComponentName(HAI_PACKAGE_NAME, targetClass.getName());
    ActivityInfo info;
    Bundle metaBunlde = null;
    try {
      info = BaseApplication.getContext().getPackageManager().getActivityInfo(com, PackageManager.GET_META_DATA);
      metaBunlde = info.metaData;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    // 首先判断参数是否满足需求（metaBunlde里面的kv必须全部不为null），如果不满足，直接返回null
    if (metaBunlde != null) {
      for (String paramName : metaBunlde.keySet()) {
        if (paramMap != null && TextUtils.isEmpty(paramMap.get(paramName))) {
          return null;
        }
      }
    }

    // 参数透传
    for (Entry<String, String> entry : paramMap.entrySet()) {
      bundle.putString(entry.getKey(), entry.getValue());
    }
    return bundle;
  }

  /**
   * 从json获取参数,然后根据meta-data来判断参数是否全部满足。如果不满足，则返回null
   * 否则，全部透传。
   */
  public static Bundle getBundleFromMetaAndJson(Bundle bundle, JSONObject jsObj, Class targetClass) {
    ComponentName com = new ComponentName(HAI_PACKAGE_NAME, targetClass.getName());
    ActivityInfo info;
    Bundle metaBunlde = null;
    try {
      info = BaseApplication.getContext().getPackageManager().getActivityInfo(com, PackageManager.GET_META_DATA);
      metaBunlde = info.metaData;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    // 首先判断参数是否满足需求（metaBunlde里面的kv必须全部不为null），如果不满足，直接返回null
    if (metaBunlde != null) {
      for (String paramName : metaBunlde.keySet()) {
        if (jsObj != null && TextUtils.isEmpty(jsObj.optString(paramName))) {
          return null;
        }
      }
    }

    // 参数透传
    Iterator<String> keys = jsObj.keys();
    String key;
    String value;
    while (keys.hasNext()) {
      key = keys.next();
      value = jsObj.optString(key);
      bundle.putString(key, value);
    }

    return bundle;
  }

  /**
   * 浏览器拦截H5业务都在这里做，需要区分一跳还是二跳。如果是一跳，返回时，直接back两次
   */
  public static boolean startUrlFilter(String url, JumpRefer jumpRefer) {
//    EtaoItemUrlHandler itemHandler = new EtaoItemUrlHandler();
//    EtaoWankeUrlHandler wankeHanlder = new EtaoWankeUrlHandler();
//    DefaultUrlHandler defaultHandler = new DefaultUrlHandler();
//    EtaoSchemaHandler schemaHandler = new EtaoSchemaHandler();
//    EtaoWankeCommunityHandler wankeSiteHandler = new EtaoWankeCommunityHandler();
//
//    schemaHandler.setNextFilterHandler(wankeSiteHandler);
//    wankeSiteHandler.setNextFilterHandler(itemHandler);
//    itemHandler.setNextFilterHandler(wankeHanlder);
//    wankeHanlder.setNextFilterHandler(defaultHandler);
//    // 责任链开始拦截并处理
//    if (schemaHandler.handlerFilterRequest(url, jumpRefer)) {
//      return true;
//    }
//
//    ArrayList<IUrlFilter> list = new ArrayList<IUrlFilter>();
//    list.add(new HaitaoUrlHandler());
//    list.add(new TMUrlHandler());
//    for (int i = 0; i < list.size(); i++) {
//      if (list.get(i).handlerFilterRequest(url, jumpRefer)) {
//        return true;
//      }
//    }
    return false;
  }
}
