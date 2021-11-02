package com.huawei.courselearningdemo.dao;

import java.util.List;

import com.huawei.courselearningdemo.model.CourseOrder;
import com.huawei.courselearningdemo.model.ResultVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CourseOrderDao {
    /**
     * 创建购买课程的订单
     */
    @POST("/course_order/create")
    Call<ResultVO<CourseOrder>> createCourseOrder(@Body CourseOrder courseOrder);

    /**
     * 根据uid查询用户所有的订单
     */
    @GET("/course_order/uid/{uid}")
    Call<List<CourseOrder>> getCourseOrders(@Path(value = "uid") String uid);
}
