package com.leecode1988.accountingapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private FloatingActionButton fbAddRecord;
    private TickerView amountText;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalUtil.getInstance().setContext(getApplicationContext());

        amountText = findViewById(R.id.amount_text);
        amountText.setCharacterLists(TickerUtils.provideNumberList());
        dateText = findViewById(R.id.date_text);

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(this);

        viewPager.setCurrentItem(pagerAdapter.getLastIndex());
        fbAddRecord = findViewById(R.id.add_amount);
        fbAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        pagerAdapter.reload();
    }


    @Override
    public void onPageSelected(int position) {
        String amount = String.valueOf(pagerAdapter.getTotalCost(position));
        amountText.setText(amount);
        String date = pagerAdapter.getDateStr(position);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
