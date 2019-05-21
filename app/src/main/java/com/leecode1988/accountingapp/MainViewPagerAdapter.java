package com.leecode1988.accountingapp;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/12 15:05
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter{
    private static final String TAG = "MainViewPagerAdapter";
    private FragmentManager mFragmentManager;
    LinkedList<MainFragment> fragments = new LinkedList<>();
    LinkedList<String> dates = new LinkedList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
        initFragment();
    }

    private void initFragment() {
        dates.clear();
        fragments.clear();

        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate();
        Log.d(TAG, "----before----");
        for (String date : dates) {
            Log.d(TAG, date);
        }
        //如果dates中不包含当天的日期，则放入一个当天的日期
        if (!dates.contains(DateUtil.getFormatterDate())) {
            dates.addLast(DateUtil.getFormatterDate());
        }
        Log.d(TAG, "----after----");
        for (String date : dates) {
            Log.d(TAG, date);
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
        if (!((Fragment) object).isAdded() || !fragments.contains(object)) {
            return POSITION_NONE;
        }
        return fragments.indexOf(object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment instantiateItem = (Fragment) super.instantiateItem(container, position);
        Fragment item = fragments.get(position);
        if (instantiateItem == item) {
            return instantiateItem;
        } else {
            //如果集合中fragment和fragmentManager中的对应下标不对应，那就说明是新添加的，自行add进入
            mFragmentManager.beginTransaction().add(container.getId(), item).commitNowAllowingStateLoss();
            return item;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;
        //如果getItemPosition中的值为POSITION_NONE，执行该方法
        if (fragments.contains(fragment)) {
            super.destroyItem(container, position, fragment);
            return;
        }
        //执行移除操作，List中fragment对象数量发生改变时。
        mFragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int getLastIndex() {
        return fragments.size() - 1;
    }

    public void reload() {
        Log.d(TAG, "fragment" + fragments.size());
        for (MainFragment fragment : fragments) {
            fragment.reload();
        }
    }

    public void update() {
        initFragment();
        notifyDataSetChanged();
    }

    public int getTotalCost(int index) {
        return fragments.get(index).getTotalCost();
    }

    public String getDateStr(int index) {
        return dates.get(index);
    }

}
