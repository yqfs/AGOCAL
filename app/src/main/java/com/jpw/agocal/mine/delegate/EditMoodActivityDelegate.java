package com.jpw.agocal.mine.delegate;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

/**
 * 编辑心情
 * Created by Administrator on 2016/6/23.
 */
public class EditMoodActivityDelegate extends AppDelegate{
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_edit_mood;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("编辑心情");
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 2;
    }
}
