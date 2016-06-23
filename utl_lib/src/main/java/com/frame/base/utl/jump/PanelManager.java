package com.frame.base.utl.jump;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.frame.base.utl.R;
import com.frame.base.utl.activity.SwipeBackActivity;
import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.log.DebugLog;
import com.frame.base.utl.navi.NavigationBar;
import com.frame.base.utl.navi.NavigationBarItem;
import com.frame.base.utl.util.other.LoginComponent;
import com.frame.base.utl.util.other.SoftInputUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 跳转中心工具
 *
 * @author WilliamChik on 15/7/18.
 */
public class PanelManager {

  public static final String TAG = "【HaiPanelManager】---->";

  /**
   * 非导航页跳转到导航页时必须带上的标志，导航页 doubleClickExitBaseActivity 在 onNewIntent() 方法中拿到该标志，
   * 确定是从非导航页跳转到首页还是其他导航页，并设置相关的转场的动画，
   * 跳转到首页则赋值 true，跳转到首页以外的页面则赋值 false
   */
  public static final String NAVI_TO_HOME_TAG = "navi_to_home_tag";

  private static final int PANELSWITCH_NOCONTEXT = 2;
  private static final int PANELSWITCH_NOTFOUND = 1;
  private static final int PANELSWITCH_OK = 0;
  private static final int PANELSWITCH_SAME = -1;
  private static final int PANELSWITCH_TOROOT = -3;
  private static final int PANELSWITCH_TOLOGIN = -4;
  private static final String WPARTNER_ID = "wpartner";

  // 成交之前的各个详情页
  private static List<String> needConvertJumpReferList;
  // 需要转换JumpRefer的目标页面
  private static List<String> needConvertJumpReferTargetList;
//  // 首页的4个tab的页面ID
//  private static List<Integer> mTabsId;
  // 打开过的panel id顺序队列
  private ArrayList<Integer> panelpathlist = null;
  // 打开过的panel实例顺序队列
  private ArrayList<IPanel> panelcontext = null;
  // 屏蔽超快点击 导致switch多次的问题
  private boolean switching = false;

  // 初始化需要转换的成交埋点的名称
  static {
    needConvertJumpReferList = new ArrayList<String>();

    // 展示商品的webview
    needConvertJumpReferTargetList = new ArrayList<String>();

    // tab的5个标签
//    buildTabIds();
  }

  /**
   * 初始化导航栏5个页面的id队列
   */
  private static void buildTabIds() {
//    mTabsId = new ArrayList<Integer>();
//    for (NavigationBarItem navItem : NavigationBar.navigationBarItems) {
//      mTabsId.add(navItem.panelId);
//    }
  }

  private PanelManager() {
    panelpathlist = new ArrayList<Integer>();
    panelcontext = new ArrayList<IPanel>();
  }

  /**
   * 获得面板管理器实例对象 (静态内部类单例模式)
   */
  public static PanelManager getInstance() {
    return SingleTonHolder.instance;
  }

  private static class SingleTonHolder {

    private static final PanelManager instance = new PanelManager();
  }

  /**
   * 销毁面板管理器实例对象时的相关操作
   */
  public void destroy() {
    panelpathlist.clear();
    panelcontext.clear();
  }

  /**
   * 返回当前最前面的Panel
   * return 如果最前Panel不存在，则返回null，否则返回最前Panel
   */
  public IPanel getCurrentPanel() {
    if (panelpathlist.size() == 0) {
      return null;
    }

    IPanel panel = null;
    for (int i = panelpathlist.size() - 1; i >= 0; --i) {
      panel = panelcontext.get(i);
      if (panel == null) {
        break;
      } else if (panel.getPanelStatus() == IPanel.PanelStatus.KILLING) {
        continue;
      } else {
        break;
      }
    }
    return panel;
  }

  /**
   * 将Panel id和Panel实例，解绑，从PanelList中Remove掉
   * 在{@link com.frame.base.utl.activity.SwipeBackActivity}的finish()和onDestroy()方法中调用，解除PanelManager对该Activity的管理
   *
   * @param panelId panel id
   */
  public synchronized void unbindPanel(int panelId) {
    if (panelcontext.size() == 0 || panelpathlist.size() == 0) {
      return;
    }

    //get panel's index
    int index = panelcontext.size() - 1;
    while (index >= 0) {
      if (panelcontext.get(index).getPanelID() == panelId) {
        panelcontext.remove(index);
        panelpathlist.remove(index);
        break;
      }
      index--;
    }
  }

  /**
   * 通过Panel实例判断是否已经在panelpath中, 正在killing的panel不算
   *
   * @param ipanel， 在PanelForm类中定义
   * @return void
   */
  private boolean isPanelInPath(IPanel ipanel) {
    return panelcontext.contains(ipanel) && ipanel.getPanelStatus() == IPanel.PanelStatus.OK;
  }

  /**
   * 通过Panel实例判断是否是panelpath的第1个, killing的panel也算
   *
   * @param ipanel， 在PanelForm类中定义
   * @return void
   */
  private boolean isFirstPanelInPath(IPanel ipanel) {

    if (panelcontext.size() > 0) {
      if (panelcontext.get(panelcontext.size() - 1) == ipanel) {
        return true;
      }
    }

    return false;
  }

  /**
   * 将Panel id和Panel实例绑定，
   * 在{@link com.frame.base.utl.activity.SwipeBackActivity}onCreate()方法中调用，将每一个启动的activity放到PanelManager中进行管理
   *
   * @param panelid Panel id， 在PanelForm类中定义
   * @param ipanel  Panel实例
   */
  public synchronized void bindPanel(int panelid, IPanel ipanel) {
    if (panelpathlist.size() == 0) {
      panelpathlist.add(panelid);
      panelcontext.add(ipanel);
      return;
    } else if (!isPanelInPath(ipanel)) {
      panelpathlist.add(panelid);
      panelcontext.add(ipanel);
      return;
    }

    switching = false;
  }

  /**
   * Panel切换，重新起一个路径，以前的路径全部被干掉，但根panel和登录panel会被保存
   *
   * @param dstid  目标panel id
   * @param bundle 切换时所带的bundle参数
   */
  public void switchPanelForNewPath(int dstid, Bundle bundle) {
    int result = switchPanelInner(null, dstid, bundle, false, 0, null, R.anim.app_slide_right_in, R.anim.app_slide_left_out, 0);

    if (result == PANELSWITCH_TOLOGIN) {
      removePanelsForNewPath(true);
    } else if (result == PANELSWITCH_SAME) {
    } else {
      removePanelsForNewPath(false);
    }
  }

  /**
   * 移除panel，保留根panel和登录panel
   *
   * @param withLogin 是否登录
   */
  private void removePanelsForNewPath(boolean withLogin) {
    for (int i = panelpathlist.size() - 1; i >= 0; --i) {
      IPanel ipanel = panelcontext.get(i);
      if (!withLogin) {
        if (ipanel.getPanelLevel() == PanelInfo.PANEL_LEVEL_ROOT || ipanel.getPanelStatus() == IPanel.PanelStatus.KILLING) {
          // 根panel或KILLING状态的panel不用删除
          continue;
        } else {
          removeSpecifyPath(i);
        }
      } else {
        if (ipanel.getPanelLevel() == PanelInfo.PANEL_LEVEL_ROOT || ipanel.getPanelLevel() == PanelInfo.PANEL_LEVEL_LOGIN
            || ipanel.getPanelStatus() == IPanel.PanelStatus.KILLING) {
          // 根panel、KILLING状态或登录level的panel不用删除
          continue;
        } else {
          removeSpecifyPath(i);
        }
      }
    }
  }

  /**
   * 删除指定索引的panel
   * 这里不用手动在panelpathlist和panelcontext队列中删除panel的信息，因为panel在onDestroy时会自动调用{@link #unbindPanel(int)}方法，
   * 该方法会自动在上述队列中删除panel的信息
   *
   * @param index panel索引
   */
  private void removeSpecifyPath(int index) {
    if (panelcontext.size() == 0 || (index >= panelcontext.size() || index < 0)) {
      return;
    }

    IPanel ipanel = panelcontext.get(index);
    Activity sa = BaseApplication.activeActivity;
    /**
     * Don't remove panel immediately, do remove when this panel's onDestroy() method is invoked.
     * IMPORTANT CONDITION: Activity will be finished when back() method is invoked.
     */
    ipanel.setPanelStatus(IPanel.PanelStatus.KILLING);

    //结束activity
    sa.finish();
    sa = null;

    // TaoLog.d(// TaoLog.PANELMANAGER_TAG, String.format("removeSpecifyPath(): after remove %d panel %s  p:%d  c:%d", index, panelname, panelpathlist.size(),panelcontext.size()));
  }

  /**
   * Panel切换，会有result返回
   *
   * @param dstid       目标panelid
   * @param bundle      切换时所带的bundle参数
   * @param requestcode result的requestcode
   */
  public void switchPanelForResult(Context context, int dstid, Bundle bundle, int requestcode) {
    switchPanelInner(context, dstid, bundle, true, requestcode, null, R.anim.app_slide_right_in, R.anim.app_slide_left_out, 0);
  }

  /**
   * Panel切换，一般的 ativity 跳转建议用这个方法
   */
  public void switchPanel(int dstid, Bundle bundle, JumpRefer jumpRefer) {
    switchPanel(null, dstid, bundle, jumpRefer, R.anim.app_slide_right_in, R.anim.app_slide_left_out, 0);
  }

  /**
   * 带转场动画的 activity 跳转
   */
  public void switchPanel(int dstid, Bundle bundle, int enterAnim, int exitAnim) {
    switchPanel(null, dstid, bundle, null, enterAnim, exitAnim, 0);
  }

  /**
   * 可指定 activity 的启动方式的 activity 跳转
   */
  public void switchPanel(int dstid, Bundle bundle, int flagActivity) {
    switchPanel(null, dstid, bundle, null, R.anim.app_slide_right_in, R.anim.app_slide_left_out, flagActivity);
  }

  @Deprecated
  public void switchPanel(Context context, int dstid, Bundle bundle, JumpRefer jumpRefer) {
    switchPanel(context, dstid, bundle, jumpRefer, R.anim.app_slide_right_in, R.anim.app_slide_left_out, 0);
  }

  /**
   * Panel切换
   *
   * @param context      上下文
   * @param dstid        目标panel id
   * @param bundle       切换时所带的bundle参数
   * @param jumpRefer    附带的jumpRefer对象
   * @param enterAnim    指定的进场动画
   * @param exitAnim     指定的退场动画
   * @param flagActivity 手动设置的 intent 启动方式
   */
  public void switchPanel(Context context, int dstid, Bundle bundle, JumpRefer jumpRefer, int enterAnim, int exitAnim,
                          int flagActivity) {
    int result = switchPanelInner(context, dstid, bundle, false, 0, jumpRefer, enterAnim, exitAnim, flagActivity);
    if (result == PANELSWITCH_NOCONTEXT) {
      // 如果通过panelManager打开不了，就通过老土的方式打开
      Class targetClass = JumpUtil.getTargetClassById(dstid);
      if (targetClass == null) {
        return;
      }

      Intent intent = new Intent(BaseApplication.getContext(), targetClass);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if (bundle != null) {
        intent.putExtras(bundle);
      }
      BaseApplication.getContext().startActivity(intent);
      setTransitionAnimation(dstid, enterAnim, exitAnim);
    }

  }

  public void switchPanelWithFinish(int dstid, Bundle bundle) {
    switchPanelWithFinish(dstid, bundle, R.anim.app_slide_right_in, R.anim.app_slide_left_out);
  }

  /**
   * Panel切换
   * 切换之后，上一个的panel会finish
   *
   * @param dstid     目标panelid
   * @param bundle    切换时所带的bundle参数
   * @param enterAnim 指定的进场动画
   * @param exitAnim  指定的退场动画
   */
  public void switchPanelWithFinish(int dstid, Bundle bundle, int enterAnim, int exitAnim) {
    int result = switchPanelInner(null, dstid, bundle, false, 0, null, enterAnim, exitAnim, 0);
    if (result == PANELSWITCH_OK) {
      // 注意此时 panelpathlist 中并不会包含新启动的 activity，所以 panelpathlist 的列尾元素应该是当前的 activity，这正是我们需要 finish 的 activity
      removeSpecifyPath(getLatestOKIndexId());
    }
  }

  /**
   * 实现activity的跳转。
   *
   * @param context      context，建议传入activity
   * @param dstid        目标页面id
   * @param bundle       bundle参数
   * @param bresult      是否需要startActivityForResult
   * @param requestcode  startActivityForResult的值
   * @param enterAnim    指定的进场动画
   * @param exitAnim     指定的退场动画
   * @param flagActivity 手动设置的 intent 启动方式
   */
  private int switchPanelInner(Context context, int dstid, Bundle bundle, boolean bresult, int requestcode, JumpRefer jumpRefer,
                               int enterAnim, int exitAnim, int flagActivity) {
    if (switching) {
      return PANELSWITCH_SAME;
    }

    Intent intent = new Intent();
    // 设置intent的启动方式
    if (context == null) {
      if (BaseApplication.activeActivity != null) {
        context = BaseApplication.activeActivity;
//        if (mTabsId.contains(dstid)) {
//          // 如果跳转的目标页面是导航页面，则设置 activity 置前的 flag
//          intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        }
      } else {
        context = BaseApplication.getContext();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      }
    } else {
//      if (mTabsId.contains(dstid)) {
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//      }
    }
    // 如果手动设置了 intent 的启动方式，则采用该方式来启动 intent
    if (flagActivity != 0) {
      intent.setFlags(flagActivity);
    }

    try {
      Class<?> dstClassName = Class.forName(PanelForm.getPanelName(dstid));
      intent.setClass(context, dstClassName);

      if (bundle != null) {
        convertJumpRefer(dstClassName, context, bundle, jumpRefer);
        intent.putExtras(bundle);
      }

      // 如果是需要登录，则需要做登录判断
      if (PanelForm.getPanelLevel(dstid) == PanelInfo.PANEL_LEVEL_LOGIN) {
        // 非强制登录
        loginFlow(context, bresult, requestcode, intent, dstid, enterAnim, exitAnim);
      } else if (PanelForm.getPanelLevel(dstid) == PanelInfo.PANEL_LEVEL_FORCE_LOGIN) {
        // 强制登录
        forceLoginFlow(context, bundle, bresult, requestcode, intent, dstid, enterAnim, exitAnim);
      } else {
        // 无需登录
        doStartFlow(bresult, context, intent, requestcode, dstid, enterAnim, exitAnim);
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return PANELSWITCH_NOTFOUND;
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return PANELSWITCH_OK;
  }

  /**
   * 强制登录
   *
   * @throws InstantiationException    InstantiationException
   * @throws IllegalAccessException    IllegalAccessException
   * @throws NoSuchMethodException     NoSuchMethodException
   * @throws InvocationTargetException InvocationTargetException
   */
  private void forceLoginFlow(Context context, Bundle bundle, boolean bresult, int requestcode, Intent intent, int dstid,
                              int enterAnim, int exitAnim)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    if (!LoginComponent.getInstance().isLogin()) {
      LoginComponent.getInstance().login(context, new LoginResultImpl(bresult, context, intent, requestcode, dstid));
    } else {
      doStartFlow(bresult, context, intent, requestcode, dstid, enterAnim, exitAnim);
    }
  }

  /**
   * 非强制登录
   */
  private void loginFlow(Context context, boolean bresult, int requestcode, Intent intent, int dstid, int enterAnim,
                         int exitAnim) {
    if (!LoginComponent.getInstance().isLogin()) {
      LoginComponent.getInstance().login(context, new LoginResultImpl(bresult, context, intent, requestcode, dstid),
                                         new LoginResultImpl(bresult, context, intent, requestcode, dstid));
    } else {
      doStartFlow(bresult, context, intent, requestcode, dstid, enterAnim, exitAnim);
    }
  }

  /**
   * 对JumpRefer进行转换。 如果当前页面是详情页，并且下个页面是浏览器，则会把当前的attachment传给oldJumpRefer
   *
   * @param dstClass 目标页面
   * @param context  当前页面
   */
  private void convertJumpRefer(Class<?> dstClass, Context context, Bundle bundle, JumpRefer jumpRefer) {
    JumpRefer oldJumpRefer = null;

    if (context instanceof Activity) {
      Intent intent = ((Activity) context).getIntent();
      if (intent != null) {
        oldJumpRefer = intent.getParcelableExtra("jumpRefer");
      }
    }

    if (jumpRefer == null) {
      jumpRefer = bundle.getParcelable("jumpRefer");
    }

    if (jumpRefer != null) {
      bundle.putParcelable("jumpRefer", jumpRefer);
    }

    String wpartner = bundle.getString(WPARTNER_ID);
    if (!TextUtils.isEmpty(wpartner) && jumpRefer != null) {
      if (jumpRefer.args != null) {
        jumpRefer.args.contains(WPARTNER_ID);
        jumpRefer.args = rebuildAttachmentByWPartner(jumpRefer.args, wpartner);
      } else {
        jumpRefer.args = WPARTNER_ID + "=" + wpartner;
      }
    }

    if (!(context instanceof Activity)) {
      return;
    }

    String dstName = dstClass.getSimpleName();
    String clsName = context.getClass().getSimpleName();
    boolean needConvert = false;

    if (needConvertJumpReferList.contains(clsName)) {
      needConvert = true;
    }

    if (!needConvert) {
      return;
    }

    if (needConvertJumpReferTargetList.contains(dstName)) {
      // 目标页面是浏览器,传之前的Refer + 当前的attachment
      if (oldJumpRefer != null) {
        if (jumpRefer != null) {
          oldJumpRefer.attachment = jumpRefer.attachment;
        }
        bundle.putParcelable("jumpRefer", oldJumpRefer);
      }
    }
  }

  /**
   * 替换attachment中的wpartnerId（通过正则表达式）
   */
  private String rebuildAttachmentByWPartner(String attachment, String wpartner) {
    Pattern pattern = Pattern.compile("wpartner=([0-9]{1,})");
    Matcher matcher = pattern.matcher(attachment);
    while (matcher.find()) {
      System.out.println(matcher.group(1));
    }
    String result = attachment.replaceAll("wpartner=([0-9]{1,})", "wpartner=" + wpartner);
    //    System.out.println(attachment.replaceAll("wpartner=([0-9]{1,})", "wpartner=" + wpartner));
    return result;
  }


  /**
   * activity跳转
   *
   * @param bresult     是否需要startActivityForResult
   * @param context     context，建议传入activity
   * @param intent      intent
   * @param requestcode startActivityForResult的值
   * @param dstid       目标 panel id
   * @param enterAnim   指定的进场动画
   * @param exitAnim    指定的退场动画
   */
  private void doStartFlow(boolean bresult, Context context, Intent intent, int requestcode, int dstid, int enterAnim,
                           int exitAnim) {
    if (!bresult) {
      if (context instanceof Activity) {
        context.startActivity(intent);
      } else {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      }
    } else {
      if (context instanceof Activity) {
        ((Activity) context).startActivityForResult(intent, requestcode);
      } else {
        DebugLog.e("startActivityForResult Error:", "can not execute startActivityForResult without activity");
      }
    }

    setTransitionAnimation(dstid, enterAnim, exitAnim);
  }

  /**
   * 设置 activity 的转场动画
   *
   * @param dstid     目标 panel id
   * @param enterAnim 指定的进场动画
   * @param exitAnim  指定的退场动画
   */
  private void setTransitionAnimation(int dstid, int enterAnim, int exitAnim) {
    SwipeBackActivity activeActivity = BaseApplication.activeActivity;
    if (activeActivity == null) {
      return;
    }
    int srcPosition = NavigationBar.findPanelPosition(activeActivity.getPanelID());
    int dstPosition = NavigationBar.findPanelPosition(dstid);
    if (srcPosition >= 0 && dstPosition >= 0) {
      // 导航页之间跳转，屏蔽转场动画
      activeActivity.overridePendingTransition(0, 0);
    } else {
      // 非导航页跳转【首页】，设置【左进右退】的转场动画；
      // 非导航页跳转到非首页以外的导航页、非导航页间跳转、导航页跳非导航页，设置【右进左退】的转场动画。
      // 注意：导航页的启动 intent 都设置了 FLAG_ACTIVITY_REORDER_TO_FRONT 的 flag，所以分为两种情况：
      // 1. 如果导航页之前打开过，则再次打开时会以 onNewIntent() 的方式启动，这时需要在导航页的 onNewIntent() 方法中判断是否是从非导航页跳转过来，
      // 并设置 overridePendingTransition() 函数才能让专场动画生效；
      // 2. 如果导航页之前未打开过，则会 onCreate() 的方式启动，需要在这里设置相关的转场动画
      activeActivity.overridePendingTransition(enterAnim, exitAnim);
    }
  }

  /**
   * Panel返回
   * 这里不用手动在panelpathlist和panelcontext队列中删除panel的信息，因为panel在onDestroy时会自动调用{@link #unbindPanel(int)}方法，
   * 该方法会自动在上述队列中删除panel的信息
   */
  public synchronized boolean back() {
    if (panelpathlist.size() <= 0) {
      return false;
    }

    int index = getLatestOKIndexId();
    if (index == -1) {
      closeCurrentActivity();
      return false;
    }

    IPanel ipanel = panelcontext.get(index);
    ipanel.setPanelStatus(IPanel.PanelStatus.KILLING);
    closeCurrentActivity();
    return true;
  }

  private void closeCurrentActivity() {
    Activity sa = BaseApplication.activeActivity;
    sa.finish();
    // activity 过场动画，必须放在finish之后
    sa.overridePendingTransition(R.anim.app_slide_left_in, R.anim.app_slide_right_out);
    // 关闭打开的软键盘
    SoftInputUtil.hideSoftInput(sa);
    sa = null;
  }

  /**
   * 把指定panelId放在队尾（其实即是 Activity 栈的头部）
   */
  public void movePanelToTair(int dstId) {
    for (int i = 0; i < panelpathlist.size() - 1; i++) {
      if (panelpathlist.get(i) == dstId) {
        IPanel ipanel = panelcontext.remove(i);
        panelcontext.add(ipanel);
        panelpathlist.remove(i);
        panelpathlist.add(dstId);
        break;
      }
    }
  }

  public IPanel getPanelByIdDesc(int index) {
    return panelcontext.get(panelpathlist.size() - index);
  }

  /**
   * 返回处于队列最后的OK状态的panel的id
   *
   * @return 如果找不到该panel，则返回-1
   */
  public int getLatestOKPanelId() {
    for (int i = panelpathlist.size() - 1; i >= 0; --i) {
      IPanel ipanel = panelcontext.get(i);
      int lastid = panelpathlist.get(i);
      if (ipanel.getPanelStatus() == IPanel.PanelStatus.OK) {
        return lastid;
      }
    }
    return -1;
  }

  /**
   * 返回处于队列最后的 count 个的OK状态的 panel 实例
   *
   * @return 如果找不到符合数量 count 的 panel 实例，则返回 null
   */
  private List<IPanel> getLatestOKPanels(int count) {
    List<IPanel> panels = new ArrayList<IPanel>(count);
    for (int i = panelcontext.size() - 1; i >= 0; --i) {
      IPanel ipanel = panelcontext.get(i);
      if (ipanel.getPanelStatus() != IPanel.PanelStatus.OK) {
        continue;
      }
      panels.add(ipanel);
      if (panels.size() == count) {
        return panels;
      }
    }
    return null;
  }

  /**
   * 返回处于队列最后的OK状态的panel的索引
   *
   * @return 如果找不到该panel，则返回-1
   */
  private int getLatestOKIndexId() {
    for (int i = panelcontext.size() - 1; i >= 0; --i) {
      IPanel ipanel = panelcontext.get(i);
      if (ipanel.getPanelStatus() == IPanel.PanelStatus.OK) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 登录组件的回调实现
   */
  private class LoginResultImpl implements LoginComponent.LoginResult {

    private boolean bresult;
    private Context context;
    private Intent intent;
    private int requestcode;
    private int dstid;

    public LoginResultImpl(boolean bresult, Context context, Intent intent, int requestcode, int dstid) {
      this.bresult = bresult;
      this.context = context;
      this.intent = intent;
      this.requestcode = requestcode;
      this.dstid = dstid;
    }

    @Override
    public void onLoginResult() {
      // 登录成功，打开目标页面，注意这时候的转场动画应该是关闭页面的转场动画
      doStartFlow(bresult, context, intent, requestcode, dstid, R.anim.app_slide_left_in, R.anim.app_slide_right_out);
    }
  }

}
