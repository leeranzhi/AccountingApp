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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initView();

        setTitle("账目统计");
        ListView listView = findViewById(R.id.list_view);

        String firstDay = DateUtil.getMonthFirstDay(DateUtil.getFormatterDate());
        String lastDay = DateUtil.getMonthLastDay(DateUtil.getFormatterDate());
        Log.d(TAG, "------>"+lastDay);
        LinkedList<RecordBean> recordList = GlobalUtil.getInstance().databaseHelper.queryRecordsByKey(firstDay,lastDay);
        Log.d(TAG,  "------>" + recordList.size());

        ArrayList<ChartItem> list = new ArrayList<>();
        list.add(new LineChartItem(generateDataLine(1), getApplicationContext()));
        list.add(new BarChartItem(generateDataBar(2), getApplicationContext()));
        list.add(new PieChartItem(generateDataPie(), getApplicationContext()));

        ChartAdapter adapter = new ChartAdapter(getApplicationContext(), list);
        listView.setAdapter(adapter);
    }

    private void initView() {
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
     * @param cnt
     * @return
     */
    private LineData generateDataLine(int cnt) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        //设置折线图的属性
        LineDataSet d1 = new LineDataSet(values, "New DataSet" + cnt + ",(1)");
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
