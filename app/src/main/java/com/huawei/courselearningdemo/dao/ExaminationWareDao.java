package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Question;
import com.huawei.courselearningdemo.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExaminationWareDao {
    @GET("/test/getTestPaper/{courseId}")
    Call<List<ExaminationWare>> getTestPaper(@Path(value = "courseId") Integer courseId);

    @GET("/test/getQuestion/{testId}")
    Call<List<Question>> getQuestion(@Path(value = "testId") Integer testId);
}
