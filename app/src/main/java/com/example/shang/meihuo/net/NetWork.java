package com.example.shang.meihuo.net;


import com.example.shang.meihuo.net.api.GankApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络操作类
 *
 */

public class NetWork {

    private static GankApi gankApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static GankApi getGankApi() {
        if (gankApi == null) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .client(okHttpClient)
//                    .baseUrl("http://gank.io/api/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .build();
//            gankApi = retrofit.create(GankApi.class);
        gankApi = new Retrofit.Builder()
                .client(okHttpClient) //    去掉之后，看不出区别，查了下，不像是这么用的
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create()) //与gson的混用
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 与rxjava的混用
                .build()
                .create(GankApi.class);
        }
        return gankApi;
    }
}
