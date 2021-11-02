package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.ResultVO;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotifyDao {
    /**
     * 发送用户购买课程成功的推送通知
     * @param uid 用户
     * @param courseId 课程编号
     */
    @POST("/notify/course_order_success")
    Call<ResultVO<String>> sendCourseOrderSuccessNotify(@Query(value = "uid") String uid, @Query(value = "courseId") Integer courseId);
}
