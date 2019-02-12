package com.leecode1988.accountingapp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author:LeeCode
 * create:2019/2/9 20:49
 */
public class DateUtil {

    //unix time->11:11
    public static String getFormattedTime(long timeStamp) {
        //yyyy-MM-dd HH:mm:ss
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(timeStamp));
    }

    //Date date->2019-02-09
    public static String getFormatterDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}
