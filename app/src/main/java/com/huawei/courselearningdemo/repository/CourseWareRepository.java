package com.huawei.courselearningdemo.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;


import com.huawei.courselearningdemo.dao.CourseWareDao;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseWareRepository {
    private CourseWareDao courseWareDao = RetrofitClient.getCourseWareDao();
    private final MutableLiveData<List<CourseWare>> courseWareData = new MutableLiveData<>();


    private static CourseWareRepository uniqueInstance = null;

    public static CourseWareRepository getCourseRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new CourseWareRepository();
        }
        return uniqueInstance;
    }

    private CourseWareRepository(){

    }
    public MutableLiveData<List<CourseWare>> getCourseWareLiveData(){
        return courseWareData;
    }


    public void loadCourseWareData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<CourseWare>> courseWareCall = courseWareDao.getCourseWare(course.getId(), uid);
        courseWareCall.enqueue(new Callback<List<CourseWare>>() {
            @Override
            public void onResponse(Call<List<CourseWare>> call, Response<List<CourseWare>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    courseWareData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CourseWare>> call, Throwable t) {
                LogUtil.e(courseWareCall, "loadCourseWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }
}
