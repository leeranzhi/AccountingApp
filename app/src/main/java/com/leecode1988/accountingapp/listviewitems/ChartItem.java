package com.leecode1988.accountingapp.listviewitems;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

/**
 * 图表listView子项的基类
 * author:LeeCode
 * create:2019/5/26 23:18
 */
public abstract class ChartItem {

    //柱状图
    static final int TYPE_BARCHART = 0;
    //折现图
    static final int TYPE_LINECHART = 1;
    //饼状图
    static final int TYPE_PIECHART = 2;

    ChartData<?> mChartData;

    ChartItem(ChartData<?> cd) {
        this.mChartData = cd;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View convertView, Context context);
}
