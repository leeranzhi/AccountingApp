package com.leecode1988.accountingapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配工具类
 * author:LeeCode
 * create:2019/6/7 17:49
 */
public class RegularUtil {

    /**
     * 11位手机号匹配
     *
     * @param phone
     * @return
     */
    public static boolean matchPhone(String phone) {
        String pattern = "0?(13|14|15|17|18|19)[0-9]{9}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phone);
        return m.matches();
    }

    /**
     * Email匹配
     * @param email
     * @return
     */
    public static boolean matchEmail(String email) {
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        return m.matches();
    }

}

