package com.leecode1988.accountingapp;

import android.content.Context;

import java.util.LinkedList;

/**
 * 全局资源管理类，包括文字图片资源，数据库实例资源
 * author:LeeCode
 * create:2019/3/3 21:20
 */
public class GlobalUtil {

    private static final String TAG = "GlobalUtil";
    private static GlobalUtil instance;

    public Context context;

    public RecordDatabaseHelper databaseHelper;
    public MainActivity mainActivity;

    public LinkedList<CategoryResBean> costRes = new LinkedList<>();
    public LinkedList<CategoryResBean> earnRes = new LinkedList<>();

    private static int[] costIconRes = {R.drawable.ic_category_food, R.drawable.ic_category_shopping,
            R.drawable.ic_category_house, R.drawable.ic_category_car, R.drawable.ic_category_fruit,
            R.drawable.ic_category_mobile, R.drawable.ic_category_water, R.drawable.ic_category_hair,
            R.drawable.ic_category_play, R.drawable.ic_category_red, R.drawable.ic_category_love,
            R.drawable.ic_category_hospital, R.drawable.ic_category_other, R.drawable.ic_category_add};

    public static String[] getCostTitle() {
        return costTitle;
    }

    //    private static int[] costIconResBlack = {R.drawable.ic_category_food, R.drawable.ic_category_shopping,
//            R.drawable.ic_category_house, R.drawable.ic_category_car, R.drawable.ic_category_fruit,
//            R.drawable.ic_category_mobile, R.drawable.ic_category_water, R.drawable.ic_category_hair,
//            R.drawable.ic_category_play, R.drawable.ic_category_red, R.drawable.ic_category_love,
//            R.drawable.ic_category_hospital, R.drawable.ic_category_other, R.drawable.ic_category_add};
    private static String[] costTitle = {"三餐", "购物",
            "住房", "交通", "水果",
            "通信", "水电", "理发",
            "娱乐", "红包", "捐赠",
            "医疗", "其他", "新增",};

    private static int[] earnIconRes = {R.drawable.ic_category_salary, R.drawable.ic_category_reward,
            R.drawable.ic_category_get, R.drawable.ic_category_invest, R.drawable.ic_category_life,
            R.drawable.ic_category_pin, R.drawable.ic_category_extra, R.drawable.ic_category_back,
            R.drawable.ic_category_red, R.drawable.ic_category_other, R.drawable.ic_category_add};
//    private static int[] earnIconResBlack = {R.drawable.ic_category_salary, R.drawable.ic_category_reward,
//            R.drawable.ic_category_get, R.drawable.ic_category_invest, R.drawable.ic_category_life,
//            R.drawable.ic_category_pin, R.drawable.ic_category_extra, R.drawable.ic_category_back,
//            R.drawable.ic_category_red, R.drawable.ic_category_other, R.drawable.ic_category_add};
    private static String[] earnTitle = {"工资", "奖金",
            "收钱", "投资收益", "生活费",
            "零花钱", "外快", "退款",
            "红包", "其他收入", "新增"};

    public static GlobalUtil getInstance() {
        if (instance == null) {
            instance = new GlobalUtil();
        }
        return instance;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        databaseHelper = new RecordDatabaseHelper(context, RecordDatabaseHelper.DB_NAME, null, 2);

        if(!costRes.isEmpty()&&!earnRes.isEmpty()) return;

        for (int i = 0; i < costTitle.length; i++) {
            CategoryResBean res = new CategoryResBean();
            res.title = costTitle[i];
//            res.resBlack = costIconResBlack[i];
            res.resWhite = costIconRes[i];
            costRes.add(res);
        }
        for (int i = 0; i < earnTitle.length; i++) {
            CategoryResBean res = new CategoryResBean();
            res.title = earnTitle[i];
//            res.resBlack = earnIconResBlack[i];
            res.resWhite = earnIconRes[i];
            earnRes.add(res);
        }

    }

    public int getResourceIcon(String category) {

        for (CategoryResBean res : costRes) {
            if (res.title.equals(category)) {
                return res.resWhite;
            }
        }

        for (CategoryResBean res : earnRes) {
            if (res.title.equals(category)) {
                return res.resWhite;
            }
        }

        return costRes.get(0).resWhite;
    }


}
