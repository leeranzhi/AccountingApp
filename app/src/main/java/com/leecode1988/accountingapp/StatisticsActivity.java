package com.leecode1988.accountingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leecode1988.accountingapp.listviewitems.BarChartItem;
import com.leecode1988.accountingapp.listviewitems.ChartItem;
import com.leecode1988.accountingapp.listviewitems.LineChartItem;
import com.leecode1988.accountingapp.listviewitems.PieChartItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StatisticsActivity extends BaseActivity {
    private static final String TAG = "StatisticsActivity";

    private TextView balance_text, balance_amount_text;
    private TextView income_text, expense_text;
    private String lastDay;
    private String firstDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initView();
    }

    private void initView() {
        initToolbar();

        setTitle("账目统计");
        ListView listView = findViewById(R.id.list_view);

        balance_text = findViewById(R.id.balance_text);
        balance_amount_text = findViewById(R.id.balance_amount_text);
        income_text = findViewById(R.id.income_text);
        expense_text = findViewById(R.id.expense_text);

        //获取当前时间所在月份的起始结束范围
        firstDay = DateUtil.getMonthFirstDay(DateUtil.getFormatterDate());
        lastDay = DateUtil.getMonthLastDay(DateUtil.getFormatterDate());
        Log.d(TAG, "------>" + lastDay);
        //查询范围内的账单
        LinkedList<RecordBean> recordList = GlobalUtil.getInstance().databaseHelper.queryRecordsByKey(firstDay, lastDay);
        Log.d(TAG, "------>" + recordList.size());

        String[] split = firstDay.split("-");
        balance_text.setText(split[1] + "月余额");
        double totalCost = 0;
        double totalInCome = 0;
        for (RecordBean record : recordList) {
            if (record.getType() == 1) {
                totalCost += record.getAmount();
            } else {
                totalInCome += record.getAmount();
            }
        }
        expense_text.setText("¥ " + String.valueOf(totalCost));
        income_text.setText("¥ " + String.valueOf(totalInCome));
        //计算结果保留小数点后一位
        // 0.0001->0.0
        // 11.223->11.2
        double balanceAmount = Double.valueOf(NumberUtil.formatDouble(totalInCome - totalCost));
        Log.d(TAG, "" + balanceAmount);
        if (balanceAmount >= 0.0) {
            balance_amount_text.setText("¥ " + balanceAmount);
        } else {
            balance_amount_text.setText("-¥ " + -balanceAmount);
        }

        ArrayList<ChartItem> list = new ArrayList<>();
        list.add(new LineChartItem(generateDataLine(recordList), getApplicationContext()));
        list.add(new BarChartItem(generateDataBar(2), getApplicationContext()));
        list.add(new PieChartItem(generateDataPie(), getApplicationContext()));

        ChartAdapter adapter = new ChartAdapter(getApplicationContext(), list);
        listView.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private class ChartAdapter extends ArrayAdapter<ChartItem> {


        ChartAdapter(@NonNull Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            ChartItem charItem = getItem(position);
            return charItem != null ? charItem.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

    }

    /**
     * 生成折线图
     *
     * @param recordList
     * @return
     */
    private LineData generateDataLine(LinkedList<RecordBean> recordList) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 1; i <= Integer.valueOf(lastDay.split("-")[2]); i++) {
            double amount = 0.0;
            for (int j = 0; j < recordList.size(); j++) {
                if (Integer.valueOf(recordList.get(j).getDate().split("-")[2]) != i) {
                    
                }
            }
            values.add(new Entry(i, (float) amount));

        }


        for (int i = 0; i < 12; i++) {
            values.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }


        //设置折线图的属性
        LineDataSet d1 = new LineDataSet(values, "支出折线图");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);

        return new LineData(sets);

    }

    /**
     * 生成条形图
     *
     * @param cnt
     * @return
     */
    private BarData generateDataBar(int cnt) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet" + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * 生成饼状图
     */
    private PieData generateDataPie() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter" + (i + 1)));
        }

        PieDataSet d = new PieDataSet(entries, "");
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        return new PieData(d);
    }

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, StatisticsActivity.class);
        intent.putExtra("param1", data);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
