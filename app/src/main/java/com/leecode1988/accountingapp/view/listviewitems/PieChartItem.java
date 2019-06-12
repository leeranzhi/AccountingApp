package com.leecode1988.accountingapp.view.listviewitems;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leecode1988.accountingapp.R;

/**
 * author:LeeCode
 * create:2019/5/26 23:18
 */
public class PieChartItem extends ChartItem {
    private final Typeface mTf;
    private final SpannableString mCenterText;

    public PieChartItem(ChartData<?> cd, Context context,String centerText) {
        super(cd);

        mTf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        mCenterText = generateCenterText(centerText);
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @Override
    public View getView(int position, View convertView, Context context) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item_piechart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //进行配置
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setHoleRadius(52f);
        holder.chart.setTransparentCircleRadius(57f);
        holder.chart.setCenterText(mCenterText);
        holder.chart.setCenterTextTypeface(mTf);
        holder.chart.setCenterTextSize(9f);
        holder.chart.setUsePercentValues(true);
        holder.chart.setExtraOffsets(5, 10, 50, 10);

        mChartData.setValueFormatter(new PercentFormatter());
        mChartData.setValueTypeface(mTf);
        mChartData.setValueTextSize(9f);
        mChartData.setValueTextColor(Color.WHITE);
        //设置数据
        holder.chart.setData((PieData) mChartData);

        Legend legend = holder.chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);

        //刷新图表
//        holder.chart.invalidate();
        holder.chart.animateY(900);

        return convertView;
    }

    /**
     * 生成中心文字
     *
     * @return
     */
    private SpannableString generateCenterText(String text) {
        SpannableString s = new SpannableString(text);

        s.setSpan(new ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[3]), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(2.0f), 0, s.length(), 0);

        return s;
    }

    private static class ViewHolder {
        PieChart chart;
    }
}
