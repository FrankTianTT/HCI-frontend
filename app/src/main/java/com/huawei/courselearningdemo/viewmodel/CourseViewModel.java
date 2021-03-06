package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CourseOrder;
import com.huawei.courselearningdemo.model.CourseWare;
import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.model.Question;
import com.huawei.courselearningdemo.model.QuestionList;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.repository.CourseOrderRepository;
import com.huawei.courselearningdemo.repository.CourseRepository;
import com.huawei.courselearningdemo.repository.CourseWareRepository;
import com.huawei.courselearningdemo.repository.CommentRepository;
import com.huawei.courselearningdemo.repository.ExaminationWareRepository;
import com.huawei.courselearningdemo.repository.QueryRepository;
import com.huawei.courselearningdemo.repository.UserLocalRepository;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.ToastUtil;

public class CourseViewModel extends ViewModel {
    private MutableLiveData<Course> courseData ;
    private MutableLiveData<List<CourseWare>> courseWareData ;
    private MutableLiveData<List<Comment>> commentWareDate;
    private MutableLiveData<List<Query>> queryWareDate;
    private MutableLiveData<List<ExaminationWare>> examinationWareData;
    private MutableLiveData<List<Question>> questionData;
    private MutableLiveData<Double> courseScore;
    private String queryContent;
    private String commentContent;
    private Integer commentScore;
    private int testId;

    private CourseRepository courseRepository=CourseRepository.getCourseRepository();
    private CourseWareRepository courseWareRepository=CourseWareRepository.getCourseRepository();
    private CommentRepository commentRepository = CommentRepository.getCommentWareRepository();
    private QueryRepository queryRepository = QueryRepository.getQueryWareRepository();
    private ExaminationWareRepository examinationWareRepository=ExaminationWareRepository.getExaminationWareRepository();
    private CourseOrderRepository courseOrderRepository = CourseOrderRepository.getCourseRepository();

    public CourseViewModel(){
        courseData = courseRepository.getCourseLiveData();
        courseWareData = courseWareRepository.getCourseWareLiveData();
        commentWareDate = commentRepository.getCommentLiveData();
        queryWareDate = queryRepository.getQueryLiveData();
        examinationWareData = examinationWareRepository.getExaminationWareData();
        questionData = examinationWareRepository.getQuestionWareData();
        courseScore = courseRepository.getCourseScore();
    }

    public void setCourse(Course c){
        courseData.setValue(c);
        courseWareRepository.loadCourseWareData(c);
        commentRepository.loadCommentData(c);
        queryRepository.loadQueryData(c);
        examinationWareRepository.loadExamiantionWareData(c);
        courseRepository.loadCourseScore(c);
    }
    public void setExaminationWareData(ExaminationWare exam){
        examinationWareRepository.loadQuestionWareData(exam.getId());
    }
    public void loadQuestion(){
        examinationWareRepository.loadQuestionWareData(examinationWareRepository.getTestId());
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
    public void loadTest(Course c){
        courseRepository.loadCourseData(c.getId());
    }

    public LiveData<Course> getCourseData(){
        return courseData;
    }

    public LiveData<Double> getCourseScore() {
        return courseScore;
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

    public LiveData<List<ExaminationWare>> getExaminationWareData(){
        return examinationWareData;
    }

    public LiveData<List<Question>> getQuestionData(){
        return questionData;
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
    public ExaminationWare addTest(Course course,String title){
        ExaminationWare exam = new ExaminationWare();
        exam.setCourseId(course.getId());
        exam.setTitle(title);
        examinationWareRepository.addExamData(exam);
        return exam;
    }
    public void judge(List<String> answers){
        int testId = examinationWareRepository.getTestId();
        examinationWareRepository.judge(answers,testId);
    }
    public void addQuestion(Question question){
        examinationWareRepository.addQuestion(question);
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

    public void reply(Integer queryId, String content){
        queryRepository.replyQuery(queryId, content);
    }

    public void createCourseOrder(Course course, String uid){
        // ?????????????????????
        CourseOrder order = new CourseOrder();
        order.setUserId(uid);
        order.setCourseId(course.getId());
        order.setCourseName(course.getName());
        order.setCost(course.getCost());
        // ???????????????????????????
        order.setStatus(Constant.ORDER_STATUS_UNPAID);
        // ?????????????????????????????????
        courseOrderRepository.createCourseOrder(order);
        course.setBought(true);
    }

    public void addStar(Course course){
        course.setStarred(true);
        courseRepository.addStar(course.getId());

    }

    public void cancelStar(Course course){
        course.setStarred(false);
        courseRepository.cancelStar(course.getId());
    }

}
