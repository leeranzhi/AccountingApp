package com.leecode1988.accountingapp.activity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.leecode1988.accountingapp.util.SPUtil;
import com.leecode1988.accountingapp.bean.HotReview;
import com.leecode1988.accountingapp.network.HotReviewSevice;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 后台服务用于更新热评
 * 暂时不用了
 */
@Deprecated
public class NeteaseHotReviewService extends Service {
    public NeteaseHotReviewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        update();
        return super.onStartCommand(intent, flags, startId);
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
                .subscribe(new Consumer<HotReview>() {
                    @Override
                    public void accept(HotReview hotReview) throws Exception {
                        //存在本地
                        SPUtil.save("hotImage", hotReview.getImages());
                        Glide.with(getApplicationContext()).load(hotReview.getImages());
                        SPUtil.save("hotText", hotReview.getComment_content());
                        SPUtil.save("text_title", hotReview.getTitle());
                    }
                });
    }
}
