package com.leecode1988.accountingapp.network;


import com.leecode1988.accountingapp.bean.HotReview;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * author:LeeCode
 * create:2019/6/18 1:42
 */
public interface HotReviewSevice {

    @GET("/")
    Observable<HotReview> getHotReview();
}
