package com.leecode1988.accountingapp.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.leecode1988.accountingapp.adapter.ListViewAdapter;
import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.bean.RecordBean;
import com.leecode1988.accountingapp.util.DateUtil;
import com.leecode1988.accountingapp.util.GlobalUtil;
import com.leecode1988.accountingapp.activity.AddRecordActivity;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/12 14:48
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements AdapterView.OnItemLongClickListener {
    private static final String TAG = "MainFragment";
    private View rootView;
    private TextView textView;
    private ListView listView;
    private String date;
    private ListViewAdapter listViewAdapter;
    private LinkedList<RecordBean> records;

    public MainFragment(String date) {
        this.date = date;
        records = GlobalUtil.getInstance().databaseHelper.queryRecords(date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        textView = rootView.findViewById(R.id.day_text);
        listView = rootView.findViewById(R.id.list_view);
        listViewAdapter = new ListViewAdapter(getActivity());
        listView.setAdapter(listViewAdapter);
        reload();

        textView.setText(DateUtil.getDateYear(date) + DateUtil.getDateTitle(date));
        listView.setOnItemLongClickListener(this);
    }

    public void reload() {
        records = GlobalUtil.getInstance().databaseHelper.queryRecords(date);
        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(getActivity());
        }

        listViewAdapter.setData(records);

//        listView.setAdapter(listViewAdapter);
        if (rootView == null) {
            Log.d(TAG, "--->遭遇到了一些错误");
        } else {
            if (listViewAdapter.getCount() > 0) {
                rootView.findViewById(R.id.no_record_today).setVisibility(View.INVISIBLE);
            } else {
                rootView.findViewById(R.id.no_record_today).setVisibility(View.VISIBLE);
            }
        }
    }

    public int getTotalCost() {
        double totalCost = 0;
        for (RecordBean record : records) {
            if (record.getType() == 1) {
                totalCost += record.getAmount();
            }
        }
        return (int) totalCost;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                   long id) {
        Log.d(TAG, "index" + position + "clicked");
        showDialog(position);
        return false;
    }

    private void showDialog(int index) {
        final String[] options = {"删除", "编辑"};
        final RecordBean selectedRecord = records.get(index);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.create();

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, options[which] + "onClicked");
                //0-->删除
                //1-->编辑
                if (which == 0) {
                    String uuid = selectedRecord.getUuid();
                    GlobalUtil.getInstance().databaseHelper.removeRecord(uuid);
                    reload();
                    if (listViewAdapter.getCount() == 0) {
                        if (date != DateUtil.getFormatterDate()) {

                        }
                    }
                    GlobalUtil.getInstance().mainActivity.updateHeader();
                } else if (which == 1) {
                    //addRecordActivity
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);

                    Bundle extra = new Bundle();
                    extra.putSerializable("record", selectedRecord);
                    intent.putExtras(extra);

                    startActivityForResult(intent, 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        Log.d(TAG, "--->onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--->onDestroy");
    }
}
