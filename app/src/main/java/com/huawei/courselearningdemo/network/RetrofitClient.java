package com.huawei.courselearningdemo.network;

import com.huawei.courselearningdemo.dao.CourseDao;
import com.huawei.courselearningdemo.dao.CourseOrderDao;
import com.huawei.courselearningdemo.dao.CourseWareDao;
import com.huawei.courselearningdemo.dao.NotifyDao;
import com.huawei.courselearningdemo.dao.UserDao;
import com.huawei.courselearningdemo.utils.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.HOST) //设置网络请求的Url地址
            .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
            .build();
    public static CourseDao courseDao = retrofit.create(CourseDao.class);
    public static CourseWareDao courseWareDao = retrofit.create(CourseWareDao.class);
    public static CourseOrderDao courseOrderDao = retrofit.create(CourseOrderDao.class);
    public static UserDao userDao = retrofit.create(UserDao.class);
    public static NotifyDao notifyDao = retrofit.create(NotifyDao.class);

    public static CourseDao getCourseDao(){return courseDao;}
    public static CourseWareDao getCourseWareDao(){return courseWareDao;}
    public static CourseOrderDao getCourseOrderDao(){return courseOrderDao;}
    public static UserDao getUserDao(){return userDao;}
    public static NotifyDao getNotifyDao(){return notifyDao;}

}
