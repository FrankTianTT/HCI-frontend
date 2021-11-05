package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.repository.CourseWareRepository;
import com.huawei.courselearningdemo.repository.CommentRepository;
import com.huawei.courselearningdemo.repository.ExaminationWareRepository;
import com.huawei.courselearningdemo.repository.QueryRepository;
import com.huawei.courselearningdemo.repository.UserLocalRepository;

public class CourseViewModel extends ViewModel {
    private MutableLiveData<Course> courseData ;
    private MutableLiveData<List<CourseWare>> courseWareData ;
    private MutableLiveData<List<Comment>> commentWareDate;
    private MutableLiveData<List<Query>> queryWareDate;
    private String queryContent;
    private String commentContent;
    private Integer commentScore;

    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private CourseWareRepository courseWareRepository=CourseWareRepository.getCourseRepository();
    private CommentRepository commentRepository = CommentRepository.getCommentWareRepository();
    private QueryRepository queryRepository = QueryRepository.getQueryWareRepository();
    private ExaminationWareRepository examinationWareRepository=ExaminationWareRepository.getExaminationWareRepository();

    public CourseViewModel(){
        courseData = courseRepository.getCourseLiveData();
        courseWareData = courseWareRepository.getCourseWareLiveData();
        commentWareDate = commentRepository.getCommentLiveData();
        queryWareDate = queryRepository.getQueryLiveData();
    }

    public void setCourse(Course c){
        courseData.setValue(c);
        courseWareRepository.loadCourseWareData(c);
        commentRepository.loadCommentData(c);
        queryRepository.loadQueryData(c);
    }

    public void setQueryContent(String q){
        queryContent = q;
    }

    public void setCommentContent(String c, Integer score){
        commentContent = c;
        commentScore = score;
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

    public LiveData<List<Comment>> getCommentWareData(){
        return commentWareDate;
    }

    public LiveData<List<Query>> getQueryWareData(){
        return queryWareDate;
    }

    public void addQuery(Course course){
        User user = UserLocalRepository.getUser();

        Query query = new Query();
        query.setCourseId(course.getId());
        query.setQuery(true);
        query.setUid(user.getUid());
        query.setUname(user.getUname());
        query.setContent(queryContent);

        queryRepository.addQueryData(course, query);
    }

    public void addComment(Course course){
        User user = UserLocalRepository.getUser();

        Comment comment = new Comment();
        comment.setCourseId(course.getId());
        comment.setQuery(false);
        comment.setUid(user.getUid());
        comment.setUname(user.getUname());
        comment.setContent(commentContent);
        comment.setScore(commentScore);

        queryRepository.addCommentData(course, comment);
    }

}
