package com.frame.base.utl.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.utl.application.BaseApplication;
import com.frame.base.utl.dialog.DebugControllBoard;
import com.frame.base.utl.jump.IPanel;
import com.frame.base.utl.jump.PanelForm;
import com.frame.base.utl.jump.PanelManager;
import com.frame.base.utl.mvp.delegate.AppHomeDelegate;
import com.frame.base.utl.util.other.DebugModeUtil;

import java.util.Map;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * <Pre>
 *     自己使用继承AppCompatActivity的右滑返回activity
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p/>
 *          Create by 2016/3/6 15:31
 */
public abstract class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase,IPanel,DebugControllBoard.OnFinshListener {

    private DebugControllBoard mDebugControllBoard;

    private SwipeBackActivityHelper mHelper;//右滑删除Activity帮助类

    private String pageName = "";
    private String pageArgs = "";
    private Map<String, String> pageArgsMap;

    private int panelstatus = IPanel.PanelStatus.OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PanelManager.getInstance().bindPanel(getPanelID(), this);
        BaseApplication.onAllActivityCreate(this);

        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);//启动手势
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);//将当前类转换为半透明效果（精髓所在）
        getSwipeBackLayout().scrollToFinishActivity();//关闭当前activity
    }

//    /**
//     * 判断是否需要隐藏actionbar。
//     * 如果不是魅族手机，都需要隐藏。
//     * 如果是魅族手机，则隐藏非导航栏页面
//     */
//    private boolean needHideActionBar() {
//        if (!(this instanceof doubleClickExitBaseActivity)) {
//            return true;
//        }
//        return false;
//    }


    /**
     * 页面埋点相关
     */
    public void createPage(String pageName) {
        createPage(pageName, null);
    }

    protected void createPage(String pageName, String argsString) {
        createPage(pageName, argsString, null);
    }

    protected void createPage(String pageName, String argsString, Map<String, String> argsMap) {
        pageName = (pageName != null && pageName.startsWith("Page_")) ? pageName.replaceAll("Page_", "") : pageName;
        this.pageName = pageName;
        this.pageArgs = argsString;
        if (pageArgsMap != null) {
            if (argsMap != null) {
                pageArgsMap.putAll(argsMap);
            }
        } else {
            pageArgsMap = argsMap;
        }
//    TBS.Page.create(pageName);
//    PageContext.PAGE = pageName;
    }
    @Override
    public View getRootView() {
        return this.findViewById(android.R.id.content);
    }
    @Override
    protected void onDestroy() {

        BaseApplication.onAllActivityDestroy(this);

        if (getRootView() != null) {
            try {
                unbindDrawables(getRootView());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PanelManager.getInstance().unbindPanel(getPanelID());


        super.onDestroy();
    }
    public void unbindDrawables(View view) {
        if (view != null) {
            view.setBackgroundDrawable(null);
        }
        if (view instanceof ViewGroup) {
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public void finish() {
        PanelManager.getInstance().unbindPanel(getPanelID());
        super.finish();
    }

    @Override
    protected void onResume() {
        BaseApplication.activeActivity = this;
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PanelManager.getInstance().movePanelToTair(this.getPanelID());
        overridePendingTransition(0, 0);
    }

    /**
     * 返回上一面板
     */
    protected void returnBack() {
        PanelManager.getInstance().back();
    }
    /**
     * 当前activity实例的panelId，每个activity实例自己实现
     */
//    public abstract int getPanelID();
    public int getPanelID(){
        return PanelForm.getPanelIdByPanelName(this.getClass().getName());
    }
    /**
     * 替代原本重载的onKeyDown函数，各activity实例自己实现
     *
     * @return 不处理返回false 处理返回true
     */
    protected abstract boolean onPanelKeyDown(int keyCode, KeyEvent event);

    @Override
    public int getPanelLevel() {
        return PanelForm.getPanelLevel(getPanelID());
    }

    @Override
    public int getPanelStatus() {
        return panelstatus;
    }

    @Override
    public void setPanelStatus(int panelstatus) {
        this.panelstatus = panelstatus;
    }

    /**
     *
     * sp单位字体不随系统改变
     * */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    /**
     * 判断优先级如下：
     * 1. 先判断是否是长按，是的话返回true；
     * 2. 接着判断新手导航是否showing，是的话返回true；
     * 3. 接着判断toolkitcenterpanel是否显示，是的话返回true；
     * 4. 最后再将按键消息传递给各自的panel，如果处理了则返回true。
     * 5. 在上述情况全部没有处理的情况下 return super.onKeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isFinishing()) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getRepeatCount() == 0 && !onPanelKeyDown(keyCode, event)) {
                returnBack();
            }
//            BaseApplication.doAppRestart();
            return true;
        } else if (DebugModeUtil.DBG && event.getRepeatCount() > 0 && keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // 如果当前是 debug 模式，则长按音量 - 键呼出 debug 工具面板
            if(mDebugControllBoard == null){
                mDebugControllBoard = new DebugControllBoard(this);
                mDebugControllBoard.setOnFinshListener(this);
            }
            mDebugControllBoard.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onFinsh(String apiEnvTag, String staticH5EnvTag, String httpsValidateSwicthTag) {
        BaseApplication.doAppExit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
