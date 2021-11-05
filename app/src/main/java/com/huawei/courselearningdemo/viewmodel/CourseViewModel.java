package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.DiscussWare;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.repository.CourseWareRepository;
import com.huawei.courselearningdemo.repository.DiscussWareRepository;
import com.huawei.courselearningdemo.repository.ExaminationWareRepository;

public class CourseViewModel extends ViewModel {
    private MutableLiveData<Course> courseData ;
    private MutableLiveData<List<CourseWare>> courseWareData ;
    private MutableLiveData<List<DiscussWare>> discussWareDate;
    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private CourseWareRepository courseWareRepository=CourseWareRepository.getCourseRepository();
    private DiscussWareRepository discussWareRepository=DiscussWareRepository.getDiscussWareRepository();
    private ExaminationWareRepository examinationWareRepository=ExaminationWareRepository.getExaminationWareRepository();


    public CourseViewModel(){
        courseData = courseRepository.getCourseLiveData();
        courseWareData = courseWareRepository.getCourseWareLiveData();
        discussWareDate = discussWareRepository.getDiscussWareLiveData();
        //examinationWareDate = examinationWareRepository.getExaminationWareLiveData();
    }

    public void setCourse(Course c){
        courseData.setValue(c);
        courseWareRepository.loadCourseWareData(c);
        discussWareRepository.loadDiscussWareData(c);
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

    public LiveData<List<DiscussWare>> getDiscussWareData(){
        return discussWareDate;
    }

}
