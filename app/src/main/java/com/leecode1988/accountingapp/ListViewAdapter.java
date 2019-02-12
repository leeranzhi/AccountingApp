package com.leecode1988.accountingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/13 0:03
 */
public class ListViewAdapter extends BaseAdapter {

    private LinkedList<RecordBean> records = new LinkedList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public ListViewAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
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

        if (convertView == null) {
            view = mInflater.inflate(R.layout.cell_list_view, parent, false);
            RecordBean recordBean = (RecordBean) getItem(position);
            holder = new ViewHolder(view, recordBean);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}

class ViewHolder {

    TextView remarkTv;
    TextView amountTv;
    TextView timeTv;
    ImageView categoryIcon;

    public ViewHolder(View itemView, RecordBean record) {
        remarkTv = itemView.findViewById(R.id.textView_remark);
        amountTv = itemView.findViewById(R.id.amount_text);
        timeTv = itemView.findViewById(R.id.textView_time);
        categoryIcon = itemView.findViewById(R.id.imageView_category);
//        remarkTv.setText(record.getCategory());
    }
}
