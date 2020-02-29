package com.leecode1988.accountingapp.app;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * App全局设置
 * author:LeeCode
 * create:2019/6/2 16:40
 */
public class MyApplication extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, "8509b314f8fe7e67b2a3cc095ef8351c");
    }


    public static Context getContext() {
        return context;
    }
}
