package com.leecode1988.accountingapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leecode1988.accountingapp.customview.FixedEditText;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * 登录界面Fragment
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private View view;
    private EditText editPhone, editPhoneKey;
    private Button btLogin;
    private Button btSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        FixedEditText phone = view.findViewById(R.id.phone);
        phone.setFixedText("手机号 ");
        FixedEditText phoneKey = view.findViewById(R.id.phone_key);
        phoneKey.setFixedText("验证码 ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editPhone = view.findViewById(R.id.phone);
        editPhoneKey = view.findViewById(R.id.phone_key);
        btLogin = view.findViewById(R.id.submit);
        btSend = view.findViewById(R.id.send);
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 11 && RegularUtil.matchPhone(s.toString().trim())) {
                    btSend.setBackground(getResources().getDrawable(R.drawable.login_bt_bg));
                    btSend.setClickable(true);
                } else {
                    btSend.setBackground(getResources().getDrawable(R.drawable.edit_bg));
                    btSend.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btSend.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btSend.setClickable(false);
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
                    Toast.makeText(getContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobSMS.requestSMSCode(phone, "记账小助手", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "发送验证码成功", Toast.LENGTH_SHORT).show();
                            //发送成功则开启倒计时
                            //倒计时对象,总共的时间,每隔多少秒更新一次时间
                            final MyCountDownTimer timer = new MyCountDownTimer(btSend, 60000, 1000);
                            timer.start();

                        } else {
                            Toast.makeText(getContext(), "发送验证码失败" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.submit: {
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = editPhoneKey.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //注册或者登录，如果注册过则直接登录
                BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<UserBean>() {
                    @Override
                    public void done(UserBean user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "登录成功" + user.getUsername(), Toast.LENGTH_SHORT).show();
                            //保存至本地
                            SPUtil.save("userToken", user.getSessionToken());
                            Log.d(TAG, "----->" + user.getSessionToken());
                            AccountCenterActivity.actionStart(getContext(), user);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "登录失败" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            default:
                break;
        }
    }
}
