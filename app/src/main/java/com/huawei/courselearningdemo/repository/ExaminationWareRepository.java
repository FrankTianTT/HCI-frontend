package com.huawei.courselearningdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.DiscussWareDao;
import com.huawei.courselearningdemo.dao.ExaminationWareDao;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExaminationWareRepository {
    private ExaminationWareDao examinationWareDao = RetrofitClient.getExaminationWareDao();
    private final MutableLiveData<List<ExaminationWare>> examinationWareData = new MutableLiveData<>();

    private static ExaminationWareRepository uniqueInstance = null;

    public static ExaminationWareRepository getExaminationWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new ExaminationWareRepository();
        }
        return uniqueInstance;
    }

    private ExaminationWareRepository(){

    }
    public MutableLiveData<List<ExaminationWare>> getExaminationWareLiveData(){
        return examinationWareData;
    }

    public void loadExaminationWareData(Course course){

    }
}
