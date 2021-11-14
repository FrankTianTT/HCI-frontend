package com.huawei.courselearningdemo.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.repository.CourseRepository;

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
        Log.d("star-bug", "before load" + starCourseListData.toString());

        loadStarCourseListData();
        Log.d("star-bug", "after load" + starCourseListData.toString());

    }

//    public String getUid() {
//        return uid;
//    }

    public LiveData<List<Course>> getStarCourseListLiveData() {
        return starCourseListData;
    }

    public void loadStarCourseListData(){
        courseRepository.loadStaredCourse();
    }


}
