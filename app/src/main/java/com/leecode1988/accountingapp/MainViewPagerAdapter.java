package com.leecode1988.accountingapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/12 15:05
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MainViewPagerAdapter";

    LinkedList<MainFragment> fragments = new LinkedList<>();
    LinkedList<String> dates = new LinkedList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragment();
    }

    private void initFragment() {
        dates.clear();
        fragments.clear();

        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate();
        Log.d(TAG, "----before----");
        for(String date:dates){
            Log.d(TAG,date);
        }
        //如果dates中不包含当天的日期，则放入一个当天的日期
        if (!dates.contains(DateUtil.getFormatterDate())) {
            dates.addLast(DateUtil.getFormatterDate());
        }
        Log.d(TAG, "----after----");
        for(String date:dates){
            Log.d(TAG,date);
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int getLastIndex() {
        return fragments.size() - 1;
    }

    public void reload() {
//        initFragment();
        Log.d(TAG, "fragment" + fragments.size());
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
