package com.leecode1988.accountingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.bean.UserBean;
import com.leecode1988.accountingapp.Util.MyCountDownTimer;
import com.leecode1988.accountingapp.Util.SPUtil;
import com.leecode1988.accountingapp.activity.base.BaseActivity;
import com.leecode1988.accountingapp.view.fragment.AnotherLoginFragment;
import com.leecode1988.accountingapp.view.fragment.LoginFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText editPhone, editPhoneKey;
    private Button btLogin;
    private Button btSend;
    //判断是手机还是邮箱
    private boolean isPhone = true;

    TextView changedMode;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        initToolbar();
        fragments.add(new LoginFragment());
        fragments.add(new AnotherLoginFragment());

        replaceFragment(fragments.get(0));
        changedMode = findViewById(R.id.text_changed_mode);
//        editPhone = findViewById(R.id.phone);
//        editPhoneKey = findViewById(R.id.phone_key);
//        btLogin = findViewById(R.id.submit);
//        btSend = findViewById(R.id.send);
//        editPhone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().length() == 11 && RegularUtil.matchPhone(s.toString().trim())) {
//                    btSend.setBackground(getResources().getDrawable(R.drawable.login_bt_bg));
//                    btSend.setClickable(true);
//                } else {
//                    btSend.setBackground(getResources().getDrawable(R.drawable.edit_bg));
//                    btSend.setClickable(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        btSend.setOnClickListener(this);
//        btLogin.setOnClickListener(this);
//        btSend.setClickable(false);
        changedMode.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("param1", data);
        context.startActivity(intent);
    }

    /**
     * 通过短信验证码进行注册或者登录
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send: {
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobSMS.requestSMSCode(phone, "记账小助手", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            //发送成功则开启倒计时
                            //倒计时对象,总共的时间,每隔多少秒更新一次时间
                            final MyCountDownTimer timer = new MyCountDownTimer(btSend, 60000, 1000);
                            timer.start();

                        } else {
                            Toast.makeText(LoginActivity.this, "发送验证码失败" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.submit: {
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = editPhoneKey.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "登录成功" + userBean.getUsername(), Toast.LENGTH_SHORT).show();
                            //保存至本地
                            SPUtil.save("userToken", userBean.getSessionToken());
                            Log.d(TAG, "----->" + userBean.getSessionToken());
                            AccountCenterActivity.actionStart(LoginActivity.this, userBean);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.text_changed_mode:
                if (!isPhone) {
                    replaceFragment(fragments.get(0));
                } else {
                    replaceFragment(fragments.get(1));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 动态替换登录碎片
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_login, fragment);
        if (fragment instanceof AnotherLoginFragment) {
            if (isPhone) {
                isPhone = false;
            }
        }

        if (fragment instanceof LoginFragment) {
            if (!isPhone) {
                isPhone = true;
            }
        }
        transaction.commit();
    }
}
