package com.huawei.courselearningdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.CourseWareDao;
import com.huawei.courselearningdemo.dao.DiscussWareDao;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussWareRepository {
    private DiscussWareDao discussWareDao = RetrofitClient.getDiscussWareDao();
    private final MutableLiveData<List<DiscussWare>> discussWareData = new MutableLiveData<>();

    private static DiscussWareRepository uniqueInstance = null;

    public static DiscussWareRepository getDiscussWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new DiscussWareRepository();
        }
        return uniqueInstance;
    }

    private DiscussWareRepository(){

    }
    public MutableLiveData<List<DiscussWare>> getDiscussWareLiveData(){
        return discussWareData;
    }

    public void loadDiscussWareData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<DiscussWare>> discussWareCall = discussWareDao.getDiscussWare(course.getId(), uid);
        discussWareCall.enqueue(new Callback<List<DiscussWare>>() {
            @Override
            public void onResponse(Call<List<DiscussWare>> call, Response<List<DiscussWare>> response) {
                Log.d("loadDiscussWareData_onResponse", response.body().toString());
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    discussWareData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DiscussWare>> call, Throwable t) {
                LogUtil.e(discussWareCall, "loadDiscussWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }
}
