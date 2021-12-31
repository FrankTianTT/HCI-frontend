package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import com.huawei.courselearningdemo.model.CourseOrder;


import com.huawei.courselearningdemo.repository.CourseOrderRepository;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.utils.LoadState;
import com.huawei.courselearningdemo.utils.ToastUtil;

public class HomeViewModel extends ViewModel {
    private String uid = null;
    private String queryKey = "";
    private Integer currentPage = 1;
    private MutableLiveData<LoadState> stateData;
    private MutableLiveData<List<Course>> courseListData;
    private MutableLiveData<Boolean> courseOrderFlag;
    private CourseOrderRepository courseOrderRepository= CourseOrderRepository.getCourseRepository();
    private CourseRepository courseRepository= CourseRepository.getCourseRepository();

    public HomeViewModel(){
        courseListData = courseRepository.getCourseListLiveData();
        stateData = courseRepository.getStateDataLiveData();
        courseOrderFlag = courseOrderRepository.getCourseOrderFlagLiveData();

    }

    public void setUid(String uid) {
        this.uid = uid;
        query();
    }

    public String getUid() {
        return uid;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey.trim();
    }

    public LiveData<LoadState> getState(){
        return stateData;
    }

    public LiveData<List<Course>> getData() {
        if (courseListData == null) {
            courseListData = new MutableLiveData<>();
            stateData = new MutableLiveData<>();
            loadData();
        }
        return courseListData;
    }

    public LiveData<Boolean> getCourseOrderFlag(){
        return courseOrderFlag;
    }
    public void getNextPageData(){
        currentPage ++;
        loadData();
    }

    public void query(){
        currentPage = 1;
        courseListData.setValue(new ArrayList<>());
        loadData();
    }

    private void loadData(){
        stateData.setValue(LoadState.LOADING);
        courseRepository.loadCourseListData(currentPage,uid,queryKey);
        currentPage = courseRepository.getCurrentPage();
    }

}
