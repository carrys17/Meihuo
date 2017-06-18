package com.example.shang.meihuo.net.api;


import com.example.shang.meihuo.model.CategoryResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 代码家的gank.io接口
 */

public interface GankApi {

    /**
     * 根据category获取Android、iOS等干货数据
     * banner也是调用这个（轮播图的图片）
     * @param category  类别
     * @param count     条目数目
     * @param page      页数
     */
    @GET("data/{category}/{count}/{page}")
    Observable<CategoryResult> getCategoryData(@Path("category") String category, @Path("count") int count, @Path("page") int page);
}
