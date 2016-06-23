package com.jpw.agocal.home.delegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

/**
 * Created by Administrator on 2016/6/22.
 */
public class MineFragmentDelegate extends AppDelegate{
    @Override
    public void initPresenter() {
        super.initPresenter();
    }

    @Override
    public int pageStyle() {
        return 3;
    }

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.create(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_mine;
    }
}
