package com.jpw.agocal.mine.delegate;

import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

import butterknife.Bind;

/**
 * 修改用户名
 * Created by Administrator on 2016/6/23.
 */
public class MotifyNickNameActivityDelegate extends AppDelegate {

    @Bind(R.id.et_nick_name)
    EditText etNickName;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_motify_nick_name;
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("修改用户名");
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 2;
    }
}
