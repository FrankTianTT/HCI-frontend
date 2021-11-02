package com.huawei.courselearningdemo.dao;

import com.huawei.courselearningdemo.model.RechargeOrder;
import com.huawei.courselearningdemo.model.ResultVO;
import com.huawei.courselearningdemo.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserDao {
    @GET("/user/{uid}")
    Call<User> getUser(@Path(value = "uid" )String uid);
    @POST("/user/login")
    Call<User> login(@Body User user);
    @POST("/user/recharge")
    Call<ResultVO<User>> recharge(@Body RechargeOrder rechargeOrder);
    @POST("/user_token/refresh")
    Call<ResultVO<String>> refreshUserToken(@Query(value = "uid") String uid, @Query(value = "token") String token);
    @FormUrlEncoded
    @POST("/user/verify_id_token")
    Call<ResultVO<String>> verifyIdToken(@Field("token") String idToken);
}
