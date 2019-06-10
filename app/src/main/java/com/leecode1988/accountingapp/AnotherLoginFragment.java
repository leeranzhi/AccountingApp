package com.leecode1988.accountingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leecode1988.accountingapp.customview.FixedEditText;

/**
 * 邮箱注册登录Fragment
 * author:LeeCode
 * create:2019/6/10 12:22
 */
public class AnotherLoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.another_fragment_login, container, false);
        FixedEditText mail = view.findViewById(R.id.edit_email);
        mail.setFixedText("邮箱");
        FixedEditText password = view.findViewById(R.id.edit_password);
        password.setFixedText("密码");
        return view;
    }
}
