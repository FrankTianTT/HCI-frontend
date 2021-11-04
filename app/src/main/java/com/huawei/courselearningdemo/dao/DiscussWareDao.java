package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiscussWareDao {
    /**
     * 获取课程的讨论列表
     * @param courseId 课程编号
     * @param uid 用户编号
     * @return 符合条件的课件列表
     */
    @GET("/comment/course/{courseId}")
    Call<List<DiscussWare>> getDiscussWare(@Path(value = "courseId") Integer courseId, @Query(value = "uid") String uid);

    @POST("/comment/add")
    Call<DiscussWare> addDiscuss(@Body DiscussWare discussWare);
}
