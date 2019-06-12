package com.leecode1988.accountingapp.activity;

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
import com.leecode1988.accountingapp.activity.base.BaseActivity;
import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.bean.RecordBean;
import com.leecode1988.accountingapp.Util.DateUtil;
import com.leecode1988.accountingapp.Util.GlobalUtil;
import com.leecode1988.accountingapp.Util.NumberUtil;
import com.leecode1988.accountingapp.view.listviewitems.BarChartItem;
import com.leecode1988.accountingapp.view.listviewitems.ChartItem;
import com.leecode1988.accountingapp.view.listviewitems.LineChartItem;
import com.leecode1988.accountingapp.view.listviewitems.PieChartItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnnualStatisticsActivity extends BaseActivity {
    private static final String TAG = "AnnualStatistics";

    private TextView balance_text, balance_amount_text;
    private TextView income_text, expense_text;

    //年度第一天
    private String firstDay;
    //年度最后一天
    private String lastDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_statistics);
        initView();
    }

    private void initView() {
        initToolbar();

        setTitle("年度账目统计");
        ListView listView = findViewById(R.id.list_view);

        balance_text = findViewById(R.id.balance_text);
        balance_amount_text = findViewById(R.id.balance_amount_text);
        income_text = findViewById(R.id.income_text);
        expense_text = findViewById(R.id.expense_text);

        //获取当前时间所在年份的起始结束范围
        firstDay = DateUtil.getYearFirstDay(DateUtil.getFormatterDate());
        lastDay = DateUtil.getYearLastDay(DateUtil.getFormatterDate());
        Log.d(TAG, "------>" + lastDay);

        //查询范围内的账单
        //结果按时间Date排序
        LinkedList<RecordBean> recordList = GlobalUtil.getInstance().databaseHelper.queryRecordsByKey(firstDay, lastDay);
        Log.d(TAG, "------>" + recordList.size());

        String[] split = firstDay.split("-");
        balance_text.setText(split[0] + "年余额");
        double totalCost = 0.0;
        double totalInCome = 0.0;
        for (RecordBean record : recordList) {
            if (record.getType() == 1) {
                totalCost += record.getAmount();
            } else {
                totalInCome += record.getAmount();
            }
        }
        expense_text.setText("¥ " + String.valueOf(totalCost));
        income_text.setText("¥ " + String.valueOf(totalInCome));
        //计算结果保留小数点后两位
        // 0.0001->0.00
        // 11.223->11.22
        double balanceAmount = Double.valueOf(NumberUtil.formatDouble(totalInCome - totalCost));
        Log.d(TAG, "" + balanceAmount);
        if (balanceAmount >= 0.0) {
            balance_amount_text.setText("¥ " + balanceAmount);
        } else {
            balance_amount_text.setText("-¥ " + -balanceAmount);
        }

        ArrayList<ChartItem> list = new ArrayList<>();
        list.add(new LineChartItem(generateDataLine(recordList), getApplicationContext()));
        list.add(new BarChartItem(generateDataBar(recordList), getApplicationContext()));
        list.add(new PieChartItem(generateDataPie(recordList), getApplicationContext(), split[0] + "年度\n总览"));

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

        //初始化开始查找位置
        int j = 0;
        for (int i = 1; i <= Integer.valueOf(lastDay.split("-")[1]); i++) {
            double amount = 0.0;
            while (j < recordList.size()) {
                if (Integer.valueOf(recordList.get(j).getDate().split("-")[1]) == i) {
                    if (recordList.get(j).getType() == 1) {
                        amount += recordList.get(j).getAmount();
                    }
                    j++;
                }
                //如果j>i,则直接结束内层循环
                //接着将amount添加到values, 从下一个i重新开始找
                else if (Integer.valueOf(recordList.get(j).getDate().split("-")[1]) > i) {
                    break;
                }
                //事实上不会走这个分支
                else {
                    j++;
                    break;
                }
            }
            Log.d(TAG, "" + (float) amount);
            values.add(new Entry(i, (float) amount));
        }


        //设置折线图的属性
        LineDataSet d1 = new LineDataSet(values, "支出折线图");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(true);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);

        return new LineData(sets);

    }

    /**
     * 生成条形图
     *
     * @param recordList
     * @return
     */
    private BarData generateDataBar(LinkedList<RecordBean> recordList) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 1; i <= Integer.valueOf(lastDay.split("-")[1]); i++) {
            double amount = 0.0;
            for (int j = 0; j < recordList.size(); j++) {
                if (Integer.valueOf(recordList.get(j).getDate().split("-")[1]) == i) {
                    if (recordList.get(j).getType() == 1) {
                        amount += recordList.get(j).getAmount();
                    }
                }
                //如果j>i,则直接结束内层循环，从下一个i重新开始找
                else if (Integer.valueOf(recordList.get(j).getDate().split("-")[1]) > i) {
                    break;
                }
            }
            entries.add(new BarEntry(i, (float) amount));
        }

        BarDataSet d = new BarDataSet(entries, "支出柱状图");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * 生成饼状图
     */
    private PieData generateDataPie(LinkedList<RecordBean> recordList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        String[] title = GlobalUtil.getCostTitle();
        for (int i = 0; i < title.length; i++) {
            double amount = 0.0;
            for (int j = 0; j < recordList.size(); j++) {
                if (recordList.get(j).getCategory().equals(title[i])) {
                    if (recordList.get(j).getType() == 1) {
                        amount += recordList.get(j).getAmount();
                    }
                }
            }
            if (amount != 0.0) {
                entries.add(new PieEntry((float) amount, title[i]));
            }
        }

        PieDataSet d = new PieDataSet(entries, "");
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS);

        return new PieData(d);
    }

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, AnnualStatisticsActivity.class);
        intent.putExtra("param1", data);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
