package com.jpw.agocal.mine.delegate;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

/**
 * Created by Administrator on 2016/6/23.
 */
public class OboutOursActivityDelegate extends AppDelegate{
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_obout_ours;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("关于我们");
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 2;
    }
}
