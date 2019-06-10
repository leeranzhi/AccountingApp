package com.leecode1988.accountingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class AccountCenterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AccountCenterActivity";

    private String avatarUrl;
    private TextView textNickName;
    private TextView textSignature;
    private ImageView imageAvatar;
    private Button btLoginOut;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_center);
        initView();
    }

    private void initView() {
        initToolbar();
        textNickName = findViewById(R.id.text_nickname);
        textSignature = findViewById(R.id.text_signature);
        imageAvatar = findViewById(R.id.image_avatar);
        btLoginOut = findViewById(R.id.bt_login_out);

        BmobUser user = (BmobUser) getIntent().getSerializableExtra("user");
        textNickName.setText(user.getUsername());

        btLoginOut.setOnClickListener(this);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("个人信息");
    }

    public static void actionStart(Context context, BmobUser user) {
        Intent intent = new Intent(context, AccountCenterActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_out:
                BmobUser.logOut();
                Toast.makeText(this, "退出登录成功", Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
    }
}
