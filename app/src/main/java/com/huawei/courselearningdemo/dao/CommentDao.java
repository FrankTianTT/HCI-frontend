package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentDao {
    /**
     * 获取课程的讨论列表
     * @param courseId 课程编号
     * @param uid 用户编号
     * @return 符合条件的课件列表
     */
    @GET("/comment/remark/{courseId}")
    Call<List<Comment>> getComment(@Path(value = "courseId") Integer courseId, @Query(value = "uid") String uid);

    @POST("/comment/add/remark")
    Call<Comment> addComment(@Body Comment comment);
}
