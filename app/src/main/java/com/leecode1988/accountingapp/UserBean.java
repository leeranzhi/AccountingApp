package com.leecode1988.accountingapp;

import cn.bmob.v3.BmobUser;

/**
 * 用户信息Bean
 * 对BmobUser进行扩展
 * author:LeeCode
 * create:2019/6/8 13:14
 */
public class UserBean extends BmobUser {
    //个性签名
    private String signature;
    //年龄
    private Integer age;
    //性别
    private Integer gender;
    //头像Url
    private String avatarUrl;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    private String userPhone;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
