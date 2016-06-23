package com.jpw.agocal.mine.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;
import com.jpw.agocal.loginreg.ResetPswByOriginalActivity;
import com.jpw.agocal.mine.EditMoodActivity;
import com.jpw.agocal.mine.MotifyNickNameActivity;
import com.jpw.agocal.view.ListItemTextView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人--编辑资料
 * Created by Administrator on 2016/6/23.
 */
public class EditInfoActivityDelegate extends AppDelegate{

    @Bind(R.id.btn_mine_name)
    ListItemTextView btnMineName;
    @Bind(R.id.btn_mine_data)
    ListItemTextView btnMineData;
    @Bind(R.id.btn_mine_sex)
    ListItemTextView btnMineSex;
    @Bind(R.id.btn_mine_city)
    ListItemTextView btnMineCity;
    @Bind(R.id.btn_mine_tag)
    ListItemTextView btnMineTag;
    @Bind(R.id.btn_mine_mood)
    ListItemTextView btnMineMood;
    @Bind(R.id.btn_motify_psw)
    TextView btnMotifyPsw;

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.create(inflater, container, savedInstanceState);
    }

    @Override
    public void startDataRequest() {
        super.startDataRequest();
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setHeaderTitle("编辑资料");
    }

    @Override
    public void initView() {

    }

    @Override
    public int pageStyle() {
        return 2;
    }

    @OnClick({R.id.btn_mine_name,R.id.btn_mine_data,R.id.btn_mine_sex,R.id.btn_mine_city,
            R.id.btn_mine_tag,R.id.btn_mine_mood,R.id.btn_motify_psw})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_mine_name:
                //用户名
                Intent intent1 = new Intent(getActivity(), MotifyNickNameActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.btn_mine_data:
                //出生年月
                break;
            case R.id.btn_mine_sex:
                //性别
                break;
            case R.id.btn_mine_city:
                //城市
                break;
            case R.id.btn_mine_tag:
                //标签
                Intent intent2 = new Intent(getActivity(), MotifyNickNameActivity.class);
                getActivity().startActivity(intent2);
                break;
            case R.id.btn_mine_mood:
                //心情
                Intent intent3 = new Intent(getActivity(), EditMoodActivity.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.btn_motify_psw:
                //修改密码
                Intent intent = new Intent(getActivity(), ResetPswByOriginalActivity.class);
                getActivity().startActivity(intent);
                break;
            default:
                break;
        }
    }
}
