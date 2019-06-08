package com.leecode1988.accountingapp;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 倒计时函数
 * author:LeeCode
 * create:2019/6/8 16:15
 */
public class MyCountDownTimer extends CountDownTimer {
    private Button button;

    public MyCountDownTimer(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    /**
     * @param millisInFuture    从开始调用start()到倒计时完成
     *                          并onFinish()方法被调用的毫秒数
     * @param countDownInterval 接受onTick回调的间隔时间
     */
    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * 计时过程
     *
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);
        button.setText(millisUntilFinished / 1000 + "秒");
    }

    @Override
    public void onFinish() {
        button.setText("重新获取");
        button.setClickable(true);
    }
}
