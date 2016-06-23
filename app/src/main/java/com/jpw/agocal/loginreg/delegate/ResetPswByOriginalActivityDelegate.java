package com.jpw.agocal.loginreg.delegate;

import android.widget.EditText;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

import butterknife.Bind;

/**
 * 通过原始密码修改密码
 * Created by Administrator on 2016/6/23.
 */
public class ResetPswByOriginalActivityDelegate extends AppDelegate {
    @Bind(R.id.txt_original_psw)
    EditText txtOriginalPsw;
    @Bind(R.id.txt_new_psw)
    EditText txtNewPsw;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_reset_psw_by_original;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("修改密码");
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 2;
    }

}
