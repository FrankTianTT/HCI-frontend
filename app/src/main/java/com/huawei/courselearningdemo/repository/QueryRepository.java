package com.huawei.courselearningdemo.repository;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.QueryDao;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryRepository {
    private QueryDao queryDao = RetrofitClient.getQueryDao();
    private final MutableLiveData<List<Query>> queryData = new MutableLiveData<>();

    private static QueryRepository uniqueInstance = null;

    public static QueryRepository getQueryWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new QueryRepository();
        }
        return uniqueInstance;
    }

    private QueryRepository(){

    }
    public MutableLiveData<List<Query>> getQueryLiveData(){
        return queryData;
    }

    public void loadQueryData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<Query>> queryCall = queryDao.getQuery(course.getId(), uid);
        queryCall.enqueue(new Callback<List<Query>>() {
            @Override
            public void onResponse(Call<List<Query>> call, Response<List<Query>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    queryData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Query>> call, Throwable t) {
                LogUtil.e(queryCall, "loadQueryWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }

    public void addQueryWareData(Course course, String queryContent){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();

    }
}
