package com.leecode1988.accountingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/12 14:48
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

    private View rootView;
    private TextView textView;
    private ListView listView;
    private String date;
    private ListViewAdapter listViewAdapter;
    private LinkedList<RecordBean> records = new LinkedList<>();

    public MainFragment(String date) {
        this.date = date;
        records = GlobalUtil.getInstance().databaseHelper.queryRecords(date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        textView = rootView.findViewById(R.id.day_text);
        listView = rootView.findViewById(R.id.list_view);
        textView.setText(date);
        listViewAdapter = new ListViewAdapter(getContext());
        reload();
    }

    public void reload() {
        records = GlobalUtil.getInstance().databaseHelper.queryRecords(date);
        listViewAdapter.setData(records);

        if (listViewAdapter.getCount() > 0) {
            rootView.findViewById(R.id.no_record_today).setVisibility(View.INVISIBLE);
        }
    }
}
