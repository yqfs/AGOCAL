package com.jpw.agocal.loginreg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.frame.base.utl.activity.TitleBaseActivity;
import com.jpw.agocal.R;
import com.jpw.agocal.loginreg.delegate.ResetPswByOriginalActivityDelegate;
/**
 * 通过原始密码修改密码
 * */
public class ResetPswByOriginalActivity extends TitleBaseActivity<ResetPswByOriginalActivityDelegate> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected Class<ResetPswByOriginalActivityDelegate> getDelegateClass() {
        return ResetPswByOriginalActivityDelegate.class;
    }
}
