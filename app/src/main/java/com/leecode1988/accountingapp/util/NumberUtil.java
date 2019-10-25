package com.leecode1988.accountingapp.util;

import java.util.Locale;

/**
 * 数字工具类
 * author:LeeCode
 * create:2019/6/8 18:28
 */
public class NumberUtil {

    //保留double数据小数点后两位
    public static String formatDouble(double number) {
        //TODO 强制指定为US英文格式 . //
        return String.format(Locale.US, "%.2f", number);
        // return String.format("%.2f", number);
    }
}
