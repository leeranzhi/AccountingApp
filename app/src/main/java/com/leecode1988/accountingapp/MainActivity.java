package com.leecode1988.accountingapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private FloatingActionButton fbAddRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(pagerAdapter.getLastIndex());
        fbAddRecord = findViewById(R.id.add_amount);
        fbAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecordActivity.actionStart(MainActivity.this, "test");
            }
        });

        GlobalUtil.getInstance().setContext(getApplicationContext());

    }
}
