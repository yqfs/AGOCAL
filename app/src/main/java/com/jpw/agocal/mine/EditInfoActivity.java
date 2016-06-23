package com.jpw.agocal.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.frame.base.utl.activity.TitleBaseActivity;
import com.jpw.agocal.R;
import com.jpw.agocal.mine.delegate.EditInfoActivityDelegate;

public class EditInfoActivity extends TitleBaseActivity<EditInfoActivityDelegate> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected Class<EditInfoActivityDelegate> getDelegateClass() {
        return EditInfoActivityDelegate.class;
    }
}
