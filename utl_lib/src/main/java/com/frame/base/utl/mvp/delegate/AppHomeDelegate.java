package com.frame.base.utl.mvp.delegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.utl.R;
import com.frame.base.utl.navi.NavigationBar;

import butterknife.Bind;

/**
 * Created by YANGQIYUN on 2016/5/3.
 */
public class AppHomeDelegate extends AppDelegate{

    // 导航条
//    @Bind(R.id.ngb_navi_bar)
    protected NavigationBar navigationBar;

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initNavigationBar();
        super.create(inflater, container, savedInstanceState);
        navigationBar = bindView(R.id.ngb_navi_bar);
    }

    @Override
    public int getRootLayoutId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 0;
    }
    public void onResume(int panelID) {
        // 更新导航栏信息
        if (navigationBar != null) {
            navigationBar.onResume(panelID);
        }
    }
    /**
     * 设置标题图片
     */
    protected void setHeaderImg(int resId) {
        mTitleHeaderBar.getTitleImageView().setImageResource(resId);
        mTitleHeaderBar.getTitleImageView().setVisibility(View.VISIBLE);
        mTitleHeaderBar.getTitleTextView().setVisibility(View.GONE);
    }
    /**
     * 导航栏初始化
     */
    protected void initNavigationBar(){}
}
