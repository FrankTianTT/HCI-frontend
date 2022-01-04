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
    private static String Uid = null;
    private String queryKey = "";
    private Integer currentPage = 1;
    private MutableLiveData<LoadState> stateData;
    private MutableLiveData<List<Course>> courseListData;
    private MutableLiveData<Boolean> courseOrderFlag;
    private CourseOrderRepository courseOrderRepository = CourseOrderRepository.getCourseRepository();
    private CourseRepository courseRepository = CourseRepository.getCourseRepository();

    public HomeViewModel() {
        courseListData = courseRepository.getCourseListLiveData();
        stateData = courseRepository.getStateDataLiveData();
        courseOrderFlag = courseOrderRepository.getCourseOrderFlagLiveData();

    }

    public void setUid(String uid) {
        Uid = uid;
        query();
    }

    public String getUid() {
        return Uid;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey.trim();
    }

    public LiveData<LoadState> getState() {
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

    public LiveData<Boolean> getCourseOrderFlag() {
        return courseOrderFlag;
    }

    public void getNextPageData() {
        currentPage++;
        loadData();
    }

    public void query() {
        currentPage = 1;
        courseListData.setValue(new ArrayList<>());
        loadData();
    }

    public void update(Course course) {
        List<Course> courses = this.courseListData.getValue();
        for (int i = 0; i < courses.size(); i++) {
            if (course.getId().equals(courses.get(i).getId())) {
                courses.get(i).setStarred(course.isStarred());
                courses.get(i).setBought(course.isBought());
                break;
            }
        }

    }

    private void loadData() {
        stateData.setValue(LoadState.LOADING);
        courseRepository.loadCourseListData(currentPage, Uid, queryKey);
        currentPage = courseRepository.getCurrentPage();
    }

}
