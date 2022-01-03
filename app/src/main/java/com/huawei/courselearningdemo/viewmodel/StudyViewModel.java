package com.huawei.courselearningdemo.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.hms.hwid.A;

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

    public void update(Course course) {
        List<Course> courses = this.studyCourseListData.getValue();
        for(int i = 0;i<courses.size();i++) {
            if(course.getId().equals(courses.get(i).getId())) {
                courses.get(i).setStarred(course.isStarred());
                courses.get(i).setBought(course.isBought());
                break;
            }
        }
    }

    public void purchase(Course course) {
        if(course.isBought()) {
            List<Course> studyCourseListDataValue = studyCourseListData.getValue();
            studyCourseListDataValue.add(course);
            studyCourseListData.setValue(studyCourseListDataValue);
        }
    }

    public void addStar(Course course){
        if(course.isStarred()) courseRepository.cancelStar(course.getId());
        else courseRepository.addStar(course.getId());
    }
}
