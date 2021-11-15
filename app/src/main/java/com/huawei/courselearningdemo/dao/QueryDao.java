package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.Query;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QueryDao {
    /**
     * 获取课程的讨论列表
     * @param courseId 课程编号
     * @param uid 用户编号
     * @return 符合条件的课件列表
     */
    @GET("/comment/query/{courseId}")
    Call<List<Query>> getQuery(@Path(value = "courseId") Integer courseId, @retrofit2.http.Query(value = "uid") String uid);

    @POST("/comment/add/query")
    Call<Query> addQuery(@Body Query query);

    @POST("/comment/reply/id/{id}/content/{content}")
    Call<Query> replyQuery(@Path(value = "id") Integer id, @Path(value = "content") String content);
}
