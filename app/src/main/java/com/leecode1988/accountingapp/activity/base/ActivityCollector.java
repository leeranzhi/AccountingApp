package com.leecode1988.accountingapp.activity.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理器
 * author:LeeCode
 * create:2019/5/19 16:38
 */
public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity:activities) {
            if(!activity.isFinishing()) activity.finish();
        }
    }
}
