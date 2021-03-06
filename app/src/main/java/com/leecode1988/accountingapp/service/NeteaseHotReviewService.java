package com.leecode1988.accountingapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.util.Log;
import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.leecode1988.accountingapp.activity.MainActivity;
import com.leecode1988.accountingapp.util.SPUtil;
import com.leecode1988.accountingapp.bean.HotReview;
import com.leecode1988.accountingapp.network.HotReviewSevice;

import com.leecode1988.accountingapp.util.ServiceNotifyUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 后台服务用于更新热评
 */
public class NeteaseHotReviewService extends Service {
    private static final String TAG = "NeteaseHotReviewService";


    public NeteaseHotReviewService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        ServiceNotifyUtil.getServiceNotify(this, MainActivity.class, "ReviewServiceUpdate", "热评更新服务");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        update();
        Log.d(TAG, "onStartCommand: ");
        return START_STICKY;
    }


    @Override public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
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
            .subscribe(new Consumer<HotReview>() {
                @Override
                public void accept(HotReview hotReview) {
                    Log.d(TAG, "accept: " + hotReview);
                    //存在本地
                    SPUtil.save("hotImage", hotReview.getImages());
                    Glide.with(getApplicationContext()).load(hotReview.getImages());
                    SPUtil.save("hotText", hotReview.getComment_content());
                    SPUtil.save("text_title", hotReview.getTitle());
                    stopForeground(true);
                }
            }, new Consumer<Throwable>() {
                @Override public void accept(Throwable throwable) {
                    stopForeground(true);
                    Notification resultNotify = ServiceNotifyUtil.getNotification(NeteaseHotReviewService.this, MainActivity.class, "reviewServiceUpdate", "热评更新");
                    getNotificationManager().notify(2, resultNotify);
                    Log.d(TAG, "accept:  throwable " + throwable.getMessage());
                }
            });
    }


    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}
