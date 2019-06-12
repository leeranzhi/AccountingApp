package com.leecode1988.accountingapp;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 上传图片接口
 * author:LeeCode
 * create:2019/6/11 22:45
 */
public interface UploadPhotoService {
    //上传文件
    @Multipart
    @POST("api")
    Observable<ImageResponse> uploadPhoto(@Part("key") String key, @Part MultipartBody.Part file);
}
