package com.jpw.agocal.mine.delegate;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

/**
 * 标签
 * Created by Administrator on 2016/6/23.
 */
public class MineTagActivityDelegate extends AppDelegate{
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_mine_tag;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("标签");
    }

    @Override
    public void initView() {
    //tag_list_item
    }

    @Override
    public int pageStyle() {
        return 2;
    }
}
