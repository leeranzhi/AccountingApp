package com.leecode1988.accountingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/12 15:05
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    LinkedList<MainFragment> fragments = new LinkedList<>();
    LinkedList<String> dates = new LinkedList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragment();
    }

    private void initFragment() {
        dates.add("2019-2-12");
        dates.add("2019-2-13");
        dates.add("2019-2-14");
        dates.add("2019-2-15");
        for (String date : dates) {
            MainFragment fragment = new MainFragment(date);
            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    public int getLastIndex() {
        return fragments.size() - 1;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
