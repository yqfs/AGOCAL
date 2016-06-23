package com.jpw.agocal.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.frame.base.utl.activity.TitleBaseActivity;
import com.frame.base.utl.fragment.listenter.OnFragmentInteractionListener;
import com.jpw.agocal.home.delegate.HomeActivityDelegate;

public class HomeActivity extends TitleBaseActivity<HomeActivityDelegate>
        implements OnFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected Class<HomeActivityDelegate> getDelegateClass() {
        return HomeActivityDelegate.class;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
