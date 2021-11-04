package com.huawei.courselearningdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.QueryWareDao;
import com.huawei.courselearningdemo.dao.QueryWareDao;
import com.huawei.courselearningdemo.model.QueryWare;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.QueryWare;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryWareRepository {
    private QueryWareDao queryWareDao = RetrofitClient.getQueryWareDao();
    private final MutableLiveData<List<QueryWare>> queryWareData = new MutableLiveData<>();

    private static QueryWareRepository uniqueInstance = null;

    public static QueryWareRepository getQueryWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new QueryWareRepository();
        }
        return uniqueInstance;
    }

    private QueryWareRepository(){

    }
    public MutableLiveData<List<QueryWare>> getQueryWareLiveData(){
        return queryWareData;
    }

    public void loadQueryWareData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<QueryWare>> queryWareCall = queryWareDao.getQueryWare(course.getId(), uid);
        queryWareCall.enqueue(new Callback<List<QueryWare>>() {
            @Override
            public void onResponse(Call<List<QueryWare>> call, Response<List<QueryWare>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    queryWareData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<QueryWare>> call, Throwable t) {
                LogUtil.e(queryWareCall, "loadQueryWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }
}
