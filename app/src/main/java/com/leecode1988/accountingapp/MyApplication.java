package com.leecode1988.accountingapp;

import android.app.Application;
import android.content.Context;

/**
 * author:LeeCode
 * create:2019/6/2 16:40
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
