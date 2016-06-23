/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.frame.base.utl.mvp.delegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.utl.R;
import com.frame.base.utl.dialog.CommonLoadingDialog;
import com.frame.base.utl.jump.PanelManager;
import com.frame.base.utl.util.other.ToastUtil;
import com.frame.base.utl.view.header.TitleHeaderBar;

import butterknife.ButterKnife;

/**
 * View delegate base class
 * 视图层代理的基类
 *@author kymjs (http://www.kymjs.com/) on 10/23/15.
 */
public abstract class AppDelegate implements IDelegate {

    protected final SparseArray<View> mViews = new SparseArray<View>();

    protected View rootView;

    // 头部标题栏
    protected TitleHeaderBar mTitleHeaderBar;
    // activity主布局
    private ViewGroup mContentViewContainer;

    public abstract int getRootLayoutId();
    //0:底部导航加头部title
    // 1：底部导航无头部title
    // 2：无底部导航加头部title
    // 3：无底部导航无头部title
    protected int pageStyle = 0;

    // 加载状态框
    private CommonLoadingDialog mLoadingDialog;

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageStyle = pageStyle();
        switch (pageStyle){
            case 0:
                rootView = inflater.inflate(R.layout.activity_title_base_navigation, container, false);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.base_notitle_activity, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.base_title_activity, container, false);
                break;
            case 3:
                rootView = inflater.inflate(R.layout.base_empty_activity, container, false);
                break;
            default:
                rootView = inflater.inflate(R.layout.activity_title_base_navigation, container, false);
                break;
        }

        init();

        int rootLayoutId = getRootLayoutId();
//        rootView = inflater.inflate(rootLayoutId, container, false);
        View view = inflater.inflate(rootLayoutId, container, false);
        if(view != null){
            mContentViewContainer.addView(view);
        }
        ButterKnife.bind(this ,rootView);
        //整体视图
        initView();
        //头部
        initHeaderView();

        initPresenter();
    }
    private void init() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.id_toolbar);
        if(toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        mContentViewContainer = (ViewGroup) rootView.findViewById(R.id.hai_content);
        if(pageStyle == 1 || pageStyle == 3){
            return;
        }
        mTitleHeaderBar = (TitleHeaderBar) rootView.findViewById(R.id.ly_header_bar_title_wrap);
        if (enableDefaultBack()) {
            //使用默认的返回按钮
            mTitleHeaderBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果子类实现了点击返回按钮的操作，则不做面板的返回操作；否则做面板的返回操作；
                    // 子类实现的返回操作优先级高于超类的面板返回操作
                    if (!processClickBack()) {
                        returnBack();
                    }
                }
            });
        } else {
            // 否则隐藏返回按钮
            mTitleHeaderBar.getLeftViewContainer().setVisibility(View.GONE);
        }
    }
    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void initWidget() {
    }

    public <T extends View> T bindView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) rootView.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }

    public <T extends View> T get(int id) {
        return (T) bindView(id);
    }

    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(listener);
        }
    }

    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }
    /**
     * 设置标题
     */
    protected void setHeaderTitle(String title) {
        if(mTitleHeaderBar == null){
            new Throwable("没有标题");
        }
        mTitleHeaderBar.setTitle(title);
    }
    public <T extends Activity> T getActivity() {
        return (T) rootView.getContext();
    }

    public abstract void initView();

    public abstract int pageStyle();

    public boolean enableDefaultBack(){
        return false;
    }

    /**
     * 点击返回按钮后的处理（子类实现，默认不处理）
     *
     * @return true 处理，false 不处理
     */
    protected boolean processClickBack() {
        return false;
    }
    /**
     * 返回上一面板
     */
    protected void returnBack() {
        PanelManager.getInstance().back();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    /**
     * 初始化头部视图
     */
    protected void initHeaderView(){}



    public void showLoadingDialog() {
        hideLoadingDialog();
        mLoadingDialog = new CommonLoadingDialog(getActivity());
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void setCancelable(boolean flag){
        if(mLoadingDialog != null){
            mLoadingDialog.setCancelable(flag);
        }
    }

    public boolean loadingDialogIsShowing() {
        if (mLoadingDialog == null) {
            return false;
        }
        return mLoadingDialog.isShowing();
    }

    protected void onPause() {
        hideLoadingDialog();
    }

    public void onDestroy(){
        hideLoadingDialog();
    }

    public void initPresenter(){}

    public void startDataRequest(){}
}
