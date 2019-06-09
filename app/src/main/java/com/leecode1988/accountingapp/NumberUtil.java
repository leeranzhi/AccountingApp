package com.leecode1988.accountingapp;

/**
 * 数字工具类
 * author:LeeCode
 * create:2019/6/8 18:28
 */
public class NumberUtil {

    //保留double数据小数点后两位
    public static String formatDouble(double number) {
        return String.format("%.2f", number);
    }
}
