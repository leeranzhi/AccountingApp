package com.leecode1988.accountingapp.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leecode1988.accountingapp.util.EncryptUtil.EncryptMD5Util;
import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.bean.UserBean;
import com.leecode1988.accountingapp.util.RegularUtil;
import com.leecode1988.accountingapp.util.SPUtil;
import com.leecode1988.accountingapp.activity.AccountCenterActivity;
import com.leecode1988.accountingapp.view.customview.FixedEditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
        mail.setFixedText("邮箱 ");
        FixedEditText password = view.findViewById(R.id.edit_password);
        password.setFixedText("密码 ");
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
            //注册
            case R.id.bt_sign_up: {
                String email = editMail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "请输入邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegularUtil.matchEmail(email)) {
                    Toast.makeText(getActivity(), "请输入正确格式的邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                //确保用户名和电子邮箱是独一无二的
                UserBean user = new UserBean();
                user.setUsername(email);
                user.setEmail(email);

                //密码进行加密后提交
                user.setPassword(EncryptMD5Util.repeatEncrypt(password, 2));
                user.signUp(new SaveListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "注册失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            //登录
            case R.id.bt_login: {
                String email = editMail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "请输入邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!RegularUtil.matchEmail(email)) {
                    Toast.makeText(getActivity(), "请输入正确格式的邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserBean user = new UserBean();
                user.setUsername(email);
                user.setPassword(EncryptMD5Util.repeatEncrypt(password, 2));
                user.login(new SaveListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "登录成功" + userBean.getUsername(), Toast.LENGTH_SHORT).show();
                            //保存至本地
                            SPUtil.save("userToken", userBean.getSessionToken());
                            AccountCenterActivity.actionStart(getActivity(), userBean);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "登录失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
