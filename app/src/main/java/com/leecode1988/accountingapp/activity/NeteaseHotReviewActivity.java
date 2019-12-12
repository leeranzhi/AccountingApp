package com.leecode1988.accountingapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.util.SPUtil;
import com.leecode1988.accountingapp.activity.base.BaseActivity;
import com.leecode1988.accountingapp.bean.HotReview;
import com.leecode1988.accountingapp.network.HotReviewSevice;

import androidx.annotation.RequiresApi;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网易云音乐热评
 * author:LeeCode
 * create:2019/6/18 0:22
 */
public class NeteaseHotReviewActivity extends BaseActivity {
    private static final String TAG = "NeteaseHotReviewService";
    private TextView textHotReview, textTitle;
    private ImageView imageHotReview;

    private long exitTime;

    private Handler handler = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netease_hotreview);
        initView();
        initAnims();
    }


    private void initView() {
        textHotReview = findViewById(R.id.text);
        textTitle = findViewById(R.id.text_title);
        imageHotReview = findViewById(R.id.image);

        String hotImageUrl = (String) SPUtil.get("hotImage", "");
        String hotText = (String) SPUtil.get("hotText", "");
        String hotTextTitle = (String) SPUtil.get("text_title", "");

        if (!TextUtils.isEmpty(hotImageUrl) && !TextUtils.isEmpty(hotText) && !TextUtils.isEmpty(hotTextTitle)) {
            textHotReview.setText("“" + hotText + "”");
            textTitle.setText("--《" + hotTextTitle + "》");
            Glide.with(this).load(hotImageUrl).into(imageHotReview);
        } else {
            textHotReview.setText("“你如此苍白是不是 已倦于在空中攀登并凝望地面你倦于 孑然一生的漫游在 有不同身世的群星之间你盈缺无常像眼睛含着忧愁是因为 看不到什么值得凝眸吗”");
            textTitle.setText("--《大鱼》");
            Glide.with(getApplicationContext())
                .load("https://p2.music.126.net/aiPQXP8mdLovQSrKsM3hMQ==/1416170985079958.jpg")
                .into(imageHotReview);
        }
        //进行更新新的热评到本地
        update();
    }


    /**
     * 更新本地的热评数据
     */
    private void update() {

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.comments.hk/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
        HotReviewSevice service = retrofit.create(HotReviewSevice.class);

        Observable observable = service.getHotReview();

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HotReview>() {

                @Override
                public void onSubscribe(Disposable d) {

                }


                @Override
                public void onNext(HotReview hotReview) {
                    if (hotReview == null) {
                        return;
                    }
                    //预加载图片
                    Glide.with(getApplicationContext()).load(hotReview.getImages());
                    //存在本地
                    SPUtil.save("hotImage", hotReview.getImages());
                    SPUtil.save("hotText", hotReview.getComment_content());
                    SPUtil.save("text_title", hotReview.getTitle());
                }


                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: " + e.getMessage());
                }


                @Override
                public void onComplete() {

                }
            });
    }


    private void initAnims() {
        //以控件自身所在位置为原点，从下方距离原点200像素的位置移动到原点
        ObjectAnimator tranTextTitle = ObjectAnimator.ofFloat(textTitle, "translationY", 200, 0);
        ObjectAnimator tranText = ObjectAnimator.ofFloat(textHotReview, "translationY", 200, 0);
        //将控件的alpha属性从0变到1
        ObjectAnimator alphaTextTitle = ObjectAnimator.ofFloat(textTitle, "alpha", 0, 1);
        ObjectAnimator alphaText = ObjectAnimator.ofFloat(textHotReview, "alpha", 0, 1);

        final AnimatorSet bottomAnim = new AnimatorSet();
        bottomAnim.setDuration(1000);

        //同时执行空间平移和alpha渐变动画
        bottomAnim.play(tranTextTitle).with(tranText).with(alphaTextTitle).with(alphaText);

        //获取屏幕高度
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        //通过测量，获取image的高度
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        imageHotReview.measure(width, height);
        int imageHeight = imageHotReview.getMeasuredHeight();

        //初始化image的移动和缩放动画
        float transY = (screenHeight - imageHeight) * 0.28f;
        //image向上移动tranY的距离
        ObjectAnimator tranImage = ObjectAnimator.ofFloat(imageHotReview, "translationY", 0, -transY);

        //image在X和Y轴都缩放0.75倍
        ObjectAnimator scaleXImage = ObjectAnimator.ofFloat(imageHotReview, "scaleX", 1f, 0.75f);
        ObjectAnimator scaleYImage = ObjectAnimator.ofFloat(imageHotReview, "scaleY", 1f, 0.75f);

        AnimatorSet imageAnim = new AnimatorSet();
        imageAnim.setDuration(1000);
        imageAnim.play(tranImage).with(scaleXImage).with(scaleYImage);

        imageAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //等待image动画结束后，开始底部text热评的动画
                bottomAnim.start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startMainActivity();
                    }
                }, 2000);
            }
        });
        imageAnim.start();
    }


    private void startMainActivity() {
        MainActivity.actionStart(NeteaseHotReviewActivity.this, "");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
