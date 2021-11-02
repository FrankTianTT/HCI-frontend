package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.repository.CourseRepository;

public class StudyViewModel extends ViewModel {

    private MutableLiveData<List<Course>> studyCourseListData;
    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private String uidData=null;



    public StudyViewModel(){
        studyCourseListData = courseRepository.getStudyCourseListLiveData();

    }
    public void setUid(String uid) {
        uidData=uid;
        loadStudyCourseListData();
    }

//    public String getUid() {
//        return uid;
//    }

    public LiveData<List<Course>> getStudyCourseListLiveData() {
        return studyCourseListData;
    }

    public void loadStudyCourseListData(){
        courseRepository.loadStudyCourseListData(uidData);
    }

}
