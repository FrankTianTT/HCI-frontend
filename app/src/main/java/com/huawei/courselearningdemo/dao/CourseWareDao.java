package com.huawei.courselearningdemo.dao;

import java.util.List;

import com.huawei.courselearningdemo.model.CourseWare;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CourseWareDao {
    /**
     * 获取课程的课件列表
     * @param courseId 课程编号
     * @param uid 用户编号
     * @return 符合条件的课件列表
     */
    @GET("/course_ware/course/{courseId}")
    Call<List<CourseWare>> getCourseWare(@Path(value = "courseId") Integer courseId, @Query(value = "uid") String uid);
}
