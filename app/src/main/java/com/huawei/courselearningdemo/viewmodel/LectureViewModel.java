package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.repository.CourseWareRepository;

import java.util.List;

public class LectureViewModel extends ViewModel {
    private MutableLiveData<Course> courseData ;
    private MutableLiveData<List<CourseWare>> courseWareData ;
    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private CourseWareRepository courseWareRepository=CourseWareRepository.getCourseRepository();


    public LectureViewModel(){
        courseData = courseRepository.getCourseLiveData();
        courseWareData = courseWareRepository.getCourseWareLiveData();

    }
    public void setCourse(Course c){
        courseData.setValue(c);
        courseWareRepository.loadCourseWareData(c);
    }
    public void loadCourse(Course c){
        courseRepository.loadCourseData(c.getId());

    }

    public LiveData<Course> getCourseData(){

        return courseData;
    }

    public LiveData<List<CourseWare>> getCourseWareData(){

        return courseWareData;
    }
}
