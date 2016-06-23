package com.frame.base.utl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabLayout 与 ViewPager 共用时的 Fragment 数据适配器，继承 FragmentStatePagerAdapter，Fragment 移除视线时不会自动销毁 Fragment 对象，
 *
 * @author WilliamChik on 15/9/23.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

  private List<Fragment> mTabFragments = new ArrayList<Fragment>();

  private List<String> mTabTitles = new ArrayList<String>();

  public TabFragmentAdapter(FragmentManager fm, List<Fragment> tabFragments, List<String> tabTitles) {
    super(fm);
    mTabFragments = tabFragments;
    mTabTitles = tabTitles;
  }

  @Override
  public Fragment getItem(int position) {
    return mTabFragments.get(position);
  }

  @Override
  public int getCount() {
    return mTabFragments.size();
  }

  public void addFragmentData(List<Fragment> tabFragments) {
    mTabFragments.addAll(tabFragments);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mTabTitles.get(position);
  }
}
