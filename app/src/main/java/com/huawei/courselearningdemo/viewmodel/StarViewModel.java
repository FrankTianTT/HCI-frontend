package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseOrder;
import com.huawei.courselearningdemo.repository.CourseOrderRepository;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.LoadState;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class StarViewModel extends ViewModel {

    private MutableLiveData<List<Course>> starCourseListData;
    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private String uidData=null;



    public StarViewModel(){
        starCourseListData = courseRepository.getStarCourseListLiveData();

    }
    public void setUid(String uid) {
        uidData=uid;
        loadStarCourseListData();
    }

//    public String getUid() {
//        return uid;
//    }

    public LiveData<List<Course>> getStarCourseListLiveData() {
        return starCourseListData;
    }

    public void loadStarCourseListData(){
        courseRepository.loadStaredClass();
    }


}
