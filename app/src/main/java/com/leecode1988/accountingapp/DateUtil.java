package com.leecode1988.accountingapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author:LeeCode
 * create:2019/2/9 20:49
 */
public class DateUtil {

    /**
     * 时间戳转为 HH:mm
     * @param timeStamp
     * @return String
     */
    //unix time->11:11
    public static String getFormattedTime(long timeStamp) {
        //yyyy-MM-dd HH:mm:ss
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(timeStamp));
    }

    /**
     * 获取当前时间 年-月-日
     * @return String类型
     */
    //Date date->2019-02-09
    public static String getFormatterDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * String类型date转换为Date型
     * @param date
     * @return
     */
    private static Date strToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getWeekDay(String date) {
        String[] weekdays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekdays[index];
    }

    public static String getDateTitle(String date) {
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int monthsIndex = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return months[monthsIndex] + String.valueOf(day) + "号";
    }
}
