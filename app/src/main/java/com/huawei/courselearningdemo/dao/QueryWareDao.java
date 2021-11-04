package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.QueryWare;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QueryWareDao {
    /**
     * 获取课程的讨论列表
     * @param courseId 课程编号
     * @param uid 用户编号
     * @return 符合条件的课件列表
     */
    @GET("/comment/query/{courseId}")
    Call<List<QueryWare>> getQueryWare(@Path(value = "courseId") Integer courseId, @Query(value = "uid") String uid);

    @POST("/add/query")
    Call<QueryWare> addQuery(@Body QueryWare queryWare);
}
