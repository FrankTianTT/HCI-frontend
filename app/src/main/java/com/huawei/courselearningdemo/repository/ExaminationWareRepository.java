package com.huawei.courselearningdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.ExaminationWareDao;
import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.model.Question;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExaminationWareRepository {
    private ExaminationWareDao examinationWareDao = RetrofitClient.getExaminationWareDao();
    private final MutableLiveData<List<ExaminationWare>> examinationWareData = new MutableLiveData<>();
    private final MutableLiveData<List<Question>> questionData = new MutableLiveData<>();

    private static ExaminationWareRepository uniqueInstance = null;

    public static ExaminationWareRepository getExaminationWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new ExaminationWareRepository();
        }
        return uniqueInstance;
    }

    private ExaminationWareRepository(){

    }
    public MutableLiveData<List<Question>> getQuestionWareData(){
        return questionData;
    }
    public MutableLiveData<List<ExaminationWare>> getExaminationWareData(){
        return examinationWareData;
    }

//    public void loadExaminationWareData(Course course){
//        String uid = null;
//        User user = UserLocalRepository.getUser();
//        if(user!=null && user.getUid()!=null)
//            uid = user.getUid();
//        if(course==null || course.getId()==null)
//            return;
//        Call<List<ExaminationWare>> examCall = examinationWareDao.getTestPaper(course.getId());
//        examCall.enqueue(new Callback<List<ExaminationWare>>() {
//            @Override
//            public void onResponse(Call<List<ExaminationWare>> call, Response<List<ExaminationWare>> response) {
//                if(response.body() == null)
//                    ToastUtil.showShortToast("后端数据异常...");
//                else
//                    examinationWareData.setValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<ExaminationWare>> call, Throwable t) {
//                LogUtil.e(examCall, "loadQueryWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
//                ToastUtil.showShortToast("网络错误，请稍后重试！");
//            }
//        });
//    }

    public void loadExamiantionWareData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<ExaminationWare>> examCall = examinationWareDao.getTestPaper(course.getId());
        examCall.enqueue(new Callback<List<ExaminationWare>>() {
            @Override
            public void onResponse(Call<List<ExaminationWare>> call, Response<List<ExaminationWare>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    examinationWareData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ExaminationWare>> call, Throwable t) {
                LogUtil.e(examCall, "loadQueryWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }
    public void loadQuestionWareData(int testId){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();

        Call<List<Question>> questionCall = examinationWareDao.getQuestion(testId);
        questionCall.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    System.out.println(response.body());
                    questionData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                LogUtil.e(questionCall, "loadQueryWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }

//    public void addCommentData(Course course, Comment comment){
//        String uid = null;
//        User user = UserLocalRepository.getUser();
//        if(user!=null && user.getUid()!=null)
//            uid = user.getUid();
//        if(course==null || course.getId()==null)
//            return;
//
//        Call<Comment> commentCall = commentDao.addComment(comment);
//
//        commentCall.enqueue(new Callback<Comment>() {
//            @Override
//            public void onResponse(Call<Comment> call, Response<Comment> response) {
//                if(response.body() == null || response.body().getUid() == null)
//                    LogUtil.e("addCommentData", "response Null Error!");
//            }
//
//            @Override
//            public void onFailure(Call<Comment> call, Throwable t) {
//                LogUtil.e("addCommentData", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
//            }
//        });
//
//    }
//
//    public void addQueryData(Course course, Query query){
//        String uid = null;
//        User user = UserLocalRepository.getUser();
//        if(user!=null && user.getUid()!=null)
//            uid = user.getUid();
//        if(course==null || course.getId()==null)
//            return;
//
//        Call<Query> queryCall = queryDao.addQuery(query);
//
//        queryCall.enqueue(new Callback<Query>() {
//            @Override
//            public void onResponse(Call<Query> call, Response<Query> response) {
//                if(response.body() == null || response.body().getUid() == null)
//                    LogUtil.e("userLoginToServer", "response Null Error!");
//            }
//
//            @Override
//            public void onFailure(Call<Query> call, Throwable t) {
//                LogUtil.e("userLoginToServer", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
//            }
//        });
//
//    }
}
