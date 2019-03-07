package com.leecode1988.accountingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener, CategoryRecyclerAdapter.OnCategoryClickListener {

    private static final String TAG = "AddRecordActivity";
    private String userInput = "";
    private TextView amountText;

    private EditText editText;
    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter adapter;

    private String category = GlobalUtil.getInstance().costRes.get(0).title;
    private RecordBean.RecordType type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
    private String remark = category;

    RecordBean record = new RecordBean();
    private boolean inEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        initView();
    }

    private void initView() {
        findViewById(R.id.keyboard_one).setOnClickListener(this);
        findViewById(R.id.keyboard_two).setOnClickListener(this);
        findViewById(R.id.keyboard_three).setOnClickListener(this);
        findViewById(R.id.keyboard_four).setOnClickListener(this);
        findViewById(R.id.keyboard_five).setOnClickListener(this);
        findViewById(R.id.keyboard_six).setOnClickListener(this);
        findViewById(R.id.keyboard_seven).setOnClickListener(this);
        findViewById(R.id.keyboard_eight).setOnClickListener(this);
        findViewById(R.id.keyboard_nine).setOnClickListener(this);
        findViewById(R.id.keyboard_zero).setOnClickListener(this);
        amountText = findViewById(R.id.textView_addView_amount);
        editText = findViewById(R.id.edit_text_mark);

        RecordBean record = (RecordBean) getIntent().getSerializableExtra("record");
        if (record != null) {
            Log.d(TAG, "getIntent " + record.getRemark());
            inEditMode = true;
            this.record = record;
            this.category = record.getCategory();
            this.remark = category;

            amountText.setText(record.getAmount() + "");
            userInput = String.valueOf(record.getAmount());
            type = record.getType() == 1 ? RecordBean.RecordType.RECORD_TYPE_EXPENSE :
                    RecordBean.RecordType.RECORD_TYPE_INCOME;
        }

        editText.setText(remark);

        handleDot();
        handleBackspace();
        handleDone();
        handleTypeChanged();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new CategoryRecyclerAdapter(getApplicationContext(), category);
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(AddRecordActivity.this, 4);
        recyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();

        adapter.setOnCategoryClickListener(this);

    }


    private void handleDot() {
        findViewById(R.id.keyboard_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Dot: ");

                if (!userInput.contains(".")) {
                    userInput += ".";

                }
            }
        });
    }

    private void handleTypeChanged() {
        findViewById(R.id.keyboard_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                    type = RecordBean.RecordType.RECORD_TYPE_INCOME;
                } else {
                    type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
                }

                adapter.changedType(type);
                category = adapter.getSelected();
                remark = category;
                editText.setText(category);
            }
        });
    }

    private void handleBackspace() {
        findViewById(R.id.keyboard_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userInput.length() > 0) {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }

                if (userInput.length() > 0 && userInput.charAt(userInput.length() - 1) == '.') {
                    userInput = userInput.substring(0, userInput.length() - 1);

                }
                updateAmountText();
            }
        });
    }

    private void handleDone() {
        findViewById(R.id.keyboard_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userInput.equals("")) {
                    double amount = Double.valueOf(userInput);

                    record.setAmount(amount);
                    record.setType(type == RecordBean.RecordType.RECORD_TYPE_EXPENSE ? 1 : 2);

                    record.setCategory(adapter.getSelected());
                    record.setRemark(editText.getText().toString());

                    if (inEditMode) {
                        GlobalUtil.getInstance().databaseHelper.editRecord(record.getUuid(), record);
                    } else {
                        GlobalUtil.getInstance().databaseHelper.addRecord(record);
                    }

                    finish();
                    Log.d(TAG, "final amount is " + amount);
                } else {
                    Toast.makeText(AddRecordActivity.this, "金额不能为0！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String input = button.getText().toString();

        Log.d(TAG, input);

        if (userInput.contains(".")) {
            //小数点后两位数,
            // 分割.前后的位数
            if (userInput.split("\\.").length == 1 || userInput.split("\\.")[1].length() < 2) {
                userInput += input;
            }
        } else {
            userInput += input;
        }

        updateAmountText();
    }

    private void updateAmountText() {
        Log.d(TAG, "userInput is " + userInput);

        if (userInput.contains(".")) {

            if (userInput.split("\\.").length == 1) {
                amountText.setText(userInput + "00");
            } else if (userInput.split("\\.")[1].length() == 1) {
                amountText.setText(userInput + "0");
            } else if (userInput.split("\\.")[1].length() == 2) {
                amountText.setText(userInput);
            }

        } else {
            //""
            if (userInput.equals("")) {
                amountText.setText("0.00");
            } else {
                amountText.setText(userInput + ".00");
            }

        }
    }

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, AddRecordActivity.class);
        intent.putExtra("param1", data);
        context.startActivity(intent);
    }

    @Override
    public void onClick(String category) {
        this.category = category;
        editText.setText(category);
        Log.d(TAG, "Category:" + category);
    }
}
