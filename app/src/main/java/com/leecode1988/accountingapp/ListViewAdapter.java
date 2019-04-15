package com.leecode1988.accountingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/13 0:03
 */
public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "ListViewAdapter";

    private LinkedList<RecordBean> records = new LinkedList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public ListViewAdapter(Context context) {
        if (context != null) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        } else {

            Log.d(TAG, "此处请注意:----->" + context == null ? "1" : "2");
        }
    }

    public void setData(LinkedList<RecordBean> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        RecordBean record = (RecordBean) getItem(position);
        if (convertView == null) {
            view = mInflater.inflate(R.layout.cell_list_view, parent, false);
            holder = new ViewHolder(view, record);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        setViewContent(holder,record);
        return view;
    }

    private void setViewContent(ViewHolder holder, RecordBean record){
        holder.remarkTv.setText(record.getRemark());

        if (record.getType() == 1) {
            holder.amountTv.setText("- " + record.getAmount());
        } else if (record.getType() == 2) {
            holder.amountTv.setText("+ " + record.getAmount());
        }
        holder.timeTv.setText(DateUtil.getFormattedTime(record.getTimeStamp()));
        holder.categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(record.getCategory()));
    }

}

class ViewHolder {

    TextView remarkTv;
    TextView amountTv;
    TextView timeTv;
    ImageView categoryIcon;

    public ViewHolder(View itemView, RecordBean record) {
        remarkTv = itemView.findViewById(R.id.textView_remark);
        amountTv = itemView.findViewById(R.id.textView_amount);
        timeTv = itemView.findViewById(R.id.textView_time);
        categoryIcon = itemView.findViewById(R.id.imageView_category);
    }
}
