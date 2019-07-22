package com.leecode1988.accountingapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.leecode1988.accountingapp.app.MyApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * 本地SP存储工具类
 * author:LeeCode
 * create:2019/6/2 16:31
 */
public class SPUtil {
    private static final int MODE = MODE_PRIVATE;
    private static final String spName = "accountData";
    private static SPUtil spUtil;
    private SharedPreferences sharedPreferences;

    private SPUtil() {
        sharedPreferences = MyApplication.getContext().getSharedPreferences(spName, MODE);
    }

    public static SPUtil getInstance() {
        if (spUtil == null) {
            spUtil = new SPUtil();
        }
        return spUtil;
    }

    /**
     * 存储数据
     *
     * @param key
     * @param object
     */
    public static void save(String key, Object object) {
        SharedPreferences.Editor editor = SPUtil.getInstance().sharedPreferences.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        }
        if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        }
        if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        }
        if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        }
        if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        }

        editor.apply();
    }

    /**
     * 获取本地存储的值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(String key, Object defaultValue) {
        SharedPreferences sp = SPUtil.getInstance().sharedPreferences;
        if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        }
        if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        }
        if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        }
        if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        }
        if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        }
        return null;
    }

    /**
     * 清除所有数据
     */
    public static void clearAll() {
        clear(MyApplication.getContext(), spName);
    }

    /**
     * 清除指定文件的数据
     *
     * @param context
     * @param fileName
     */
    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
