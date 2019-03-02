package com.leecode1988.accountingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddRecordActivity";
    private String userInput = "";
    private TextView amountText;

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
        handleDot();
        handleBackspace();
        handleDone();
        handleTypeChanged();
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
                Log.d(TAG, "TypeChanged: ");
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
                    Log.d(TAG, "final amount is " + amount);
                } else {
                    
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
}
