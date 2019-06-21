package com.leecode1988.accountingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.activity.base.BaseActivity;

/**
 * 启动页
 * author:LeeCode
 * create:2019/6/18 0:22
 */
public class StartActivity extends BaseActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNeteaseHotReview();
            }
        }, 0);
    }

    /**
     * 网易云音乐热评界面
     */
    private void goToNeteaseHotReview() {
        Intent intent = new Intent(StartActivity.this, NeteaseHotReviewActivity.class);
        startActivity(intent);
        finish();
        //取消界面跳转时的动画，是启动页logo与网易云热评界面衔接
        overridePendingTransition(0, 0);
    }

    /**
     * 屏蔽物理返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
