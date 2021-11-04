package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.CommentWare;
import com.huawei.courselearningdemo.model.QueryWare;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.repository.CourseWareRepository;
import com.huawei.courselearningdemo.repository.CommentWareRepository;
import com.huawei.courselearningdemo.repository.QueryWareRepository;

public class CourseViewModel extends ViewModel {
    private MutableLiveData<Course> courseData ;
    private MutableLiveData<List<CourseWare>> courseWareData ;
    private MutableLiveData<List<CommentWare>> commentWareDate;
    private MutableLiveData<List<QueryWare>> queryWareDate;

    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private CourseWareRepository courseWareRepository=CourseWareRepository.getCourseRepository();
    private CommentWareRepository commentWareRepository = CommentWareRepository.getCommentWareRepository();
    private QueryWareRepository queryWareRepository = QueryWareRepository.getQueryWareRepository();



    public CourseViewModel(){
        courseData = courseRepository.getCourseLiveData();
        courseWareData = courseWareRepository.getCourseWareLiveData();
        commentWareDate = commentWareRepository.getCommentWareLiveData();
        queryWareDate = queryWareRepository.getQueryWareLiveData();
    }

    public void setCourse(Course c){
        courseData.setValue(c);
        courseWareRepository.loadCourseWareData(c);
        commentWareRepository.loadCommentWareData(c);
        queryWareRepository.loadQueryWareData(c);
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

    public LiveData<List<CommentWare>> getCommentWareData(){
        return commentWareDate;
    }

    public LiveData<List<QueryWare>> getQueryWareData(){
        return queryWareDate;
    }

}
