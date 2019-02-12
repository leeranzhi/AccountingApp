package com.leecode1988.accountingapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(pagerAdapter.getLastIndex());
    }
}
