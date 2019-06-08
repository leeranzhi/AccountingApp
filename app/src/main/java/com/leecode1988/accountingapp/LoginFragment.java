package com.leecode1988.accountingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leecode1988.accountingapp.customview.FixedEditText;

/**
 * 登录界面Fragment
 */
public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        FixedEditText phone = view.findViewById(R.id.phone);
        phone.setFixedText("手机号");
        FixedEditText phoneKey = view.findViewById(R.id.phone_key);
        phoneKey.setFixedText("验证码");
        return view;
    }

}
