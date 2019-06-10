package com.leecode1988.accountingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.leecode1988.accountingapp.customview.FixedEditText;

/**
 * 邮箱注册登录Fragment
 * author:LeeCode
 * create:2019/6/10 12:22
 */
public class AnotherLoginFragment extends Fragment implements View.OnClickListener {
    private EditText editMail, editPassword;
    private Button btSignUp, btLogin;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.another_fragment_login, container, false);
        FixedEditText mail = view.findViewById(R.id.edit_email);
        mail.setFixedText("邮箱");
        FixedEditText password = view.findViewById(R.id.edit_password);
        password.setFixedText("密码");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editMail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btSignUp = view.findViewById(R.id.bt_sign_up);
        btLogin = view.findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
        btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sign_up:


                break;
            case R.id.bt_login:



                break;
            default:
                break;
        }
    }
}
