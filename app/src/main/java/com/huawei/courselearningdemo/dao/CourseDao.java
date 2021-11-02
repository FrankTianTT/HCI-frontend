package com.huawei.courselearningdemo.dao;

import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.PageInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CourseDao {
    /**
     * 查询一门课程
     * @param courseId 课程编号
     * @param uid 用户编号
     * @return 课程
     */
    @GET("/course/{courseId}")
    Call<Course> getCourse(@Path(value = "courseId") Integer courseId, @Query(value = "uid")String uid);

    /**
     * 根据关键字分页查询课程
     * @param page 页码数
     * @param uid 用户编号
     * @param key 关键字
     * @return 符合条件的课程列表分页信息
     */
    @GET("/course/all/{page}")
    Call<PageInfo<Course>> getAllCourses(@Path(value = "page") Integer page, @Query(value = "uid")String uid, @Query(value = "key")String key);

    /**
     * 根据课程类型分页查询课程
     * @param page 页码数
     * @param uid 用户编号
     * @param type 课程类型
     * @return 符合条件的课程列表分页信息
     */
    @GET("/course/by_type/{page}")
    Call<PageInfo<Course>> getCoursesByType(@Path(value = "page") Integer page, @Query(value = "uid")String uid, @Query(value = "type")String type);

    /**
     * 查询用户已购买的课程
     * @param uid 用户编号
     * @return 符合条件的课程列表分页信息
     */
    @GET("/course/uid/{uid}")
    Call<List<Course>> getCourseByUid(@Path(value = "uid") String uid);
}