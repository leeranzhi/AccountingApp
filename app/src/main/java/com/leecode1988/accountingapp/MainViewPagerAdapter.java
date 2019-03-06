package com.leecode1988.accountingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/12 15:05
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "MainViewPagerAdapter";

    LinkedList<MainFragment> fragments = new LinkedList<>();
    LinkedList<String> dates = new LinkedList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragment();
    }

    private void initFragment() {
        dates = GlobalUtil.getInstance().databaseHelper.getAvaliableDate();
        if (!dates.contains(DateUtil.getFormatterDate())) {
            dates.addLast(DateUtil.getFormatterDate());
        }

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

    public void reload() {
        for (MainFragment fragment : fragments) {
            fragment.reload();
        }
    }

    public int getTotalCost(int index) {
        return fragments.get(index).getTotalCost();
    }

    public String getDateStr(int index) {
        return dates.get(index);
    }
}
