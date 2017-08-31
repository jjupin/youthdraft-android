package com.youthdraft.youthdraftcoach.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjupin on 11/15/16.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titleList;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        fragmentList = new ArrayList<Fragment>();
        titleList = new ArrayList<String>();
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addFragment(Fragment frag, String title) {
        fragmentList.add(frag);
        titleList.add(title);
    }
}
