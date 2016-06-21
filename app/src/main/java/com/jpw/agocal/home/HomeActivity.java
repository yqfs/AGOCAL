package com.jpw.agocal.home;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jpw.agocal.R;
import com.jpw.agocal.home.Fragment.FootPrintListFragment;
import com.jpw.agocal.home.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends FragmentActivity implements FootPrintListFragment.OnFragmentInteractionListener {

    @Bind(R.id.id_viewPager)
    ViewPager idViewPager;
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
    @Bind(R.id.id_radioGroup)
    RadioGroup radioGroup;

    private ViewPager pager;
    private FragmentAdapter adapter;

    private Resources res;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        pager = (ViewPager) findViewById(R.id.id_viewPager);
        List<Fragment> mFragmentList = new ArrayList<Fragment>();

        FootPrintListFragment mFriendFg = new FootPrintListFragment();
        FootPrintListFragment mContactsFg = new FootPrintListFragment();
        FootPrintListFragment mChatFg = new FootPrintListFragment();
        FootPrintListFragment mChatFg1 = new FootPrintListFragment();
        FootPrintListFragment mChatFg2 = new FootPrintListFragment();
        mFragmentList.add(mChatFg);
        mFragmentList.add(mFriendFg);
        mFragmentList.add(mContactsFg);
        mFragmentList.add(mChatFg1);
        mFragmentList.add(mChatFg2);
        adapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener((ViewPager.OnPageChangeListener) new TabOnPageChangeListener());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_tab_im:
                        pager.setCurrentItem(0);//选择某一页
                        break;
                    case R.id.home_tab_foot:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.home_tab_team:
                        pager.setCurrentItem(2);
                        break;
                    case R.id.home_tab_map:
                        pager.setCurrentItem(3);
                        break;
                    case R.id.home_tab_mine:
                        pager.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
