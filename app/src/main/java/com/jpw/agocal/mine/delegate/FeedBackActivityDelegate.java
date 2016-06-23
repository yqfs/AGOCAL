package com.jpw.agocal.mine.delegate;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 意见反馈
 * Created by Administrator on 2016/6/23.
 */
public class FeedBackActivityDelegate extends AppDelegate{
    @Bind(R.id.et_feed)
    EditText etFeed;
    @Bind(R.id.btn_submit_feed)
    Button btnSubmitFeed;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("意见");
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 2;
    }

    @OnClick(R.id.btn_submit_feed)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit_feed:
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
