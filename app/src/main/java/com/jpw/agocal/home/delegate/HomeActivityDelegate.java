package com.jpw.agocal.home.delegate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.frame.base.utl.mvp.delegate.AppDelegate;
import com.jpw.agocal.R;
import com.jpw.agocal.home.adapter.FragmentAdapter;
import com.jpw.agocal.home.fragment.FootPrintListFragment;
import com.jpw.agocal.home.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 首页
 * Created by Administrator on 2016/6/22.
 */
public class HomeActivityDelegate extends AppDelegate {

    @Bind(R.id.home_tab_im)
    RadioButton homeTabIm;
    @Bind(R.id.home_tab_foot)
    RadioButton homeTabFoot;
    @Bind(R.id.home_tab_team)
    RadioButton homeTabTeam;
    @Bind(R.id.home_tab_map)
    RadioButton homeTabMap;
    @Bind(R.id.home_tab_mine)
    RadioButton homeTabMine;
    @Bind(R.id.home_page)
    ViewPager homePage;
    @Bind(R.id.home_tab_parent)
    RadioGroup homeTabParent;

    private FragmentAdapter adapter;

    @Override
    public void initPresenter() {
        super.initPresenter();
    }

    @Override
    public int pageStyle() {
        return 3;
    }

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.create(inflater, container, savedInstanceState);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {

        List<Fragment> mFragmentList = new ArrayList<Fragment>();

        FootPrintListFragment mFriendFg = new FootPrintListFragment();
        FootPrintListFragment mContactsFg = new FootPrintListFragment();
        FootPrintListFragment mChatFg = new FootPrintListFragment();
        FootPrintListFragment mChatFg1 = new FootPrintListFragment();
        MineFragment mChatFg2 = new MineFragment();
        mFragmentList.add(mChatFg);
        mFragmentList.add(mFriendFg);
        mFragmentList.add(mContactsFg);
        mFragmentList.add(mChatFg1);
        mFragmentList.add(mChatFg2);
        adapter = new FragmentAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager(), mFragmentList);
        homePage.setAdapter(adapter);
        homePage.setOnPageChangeListener(new TabOnPageChangeListener());

        homeTabParent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_tab_im:
                        homePage.setCurrentItem(0);//选择某一页
                        break;
                    case R.id.home_tab_foot:
                        homePage.setCurrentItem(1);
                        break;
                    case R.id.home_tab_team:
                        homePage.setCurrentItem(2);
                        break;
                    case R.id.home_tab_map:
                        homePage.setCurrentItem(3);
                        break;
                    case R.id.home_tab_mine:
                        homePage.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    /**
     * 页卡改变事件
     */
    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {

        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    homeTabIm.setChecked(true);
                    break;
                case 1:
                    homeTabFoot.setChecked(true);
                    break;
                case 2:
                    homeTabTeam.setChecked(true);
                    break;
                case 3:
                    homeTabMap.setChecked(true);
                    break;
                case 4:
                    homeTabMine.setChecked(true);
                    break;
            }
        }
    }
}
