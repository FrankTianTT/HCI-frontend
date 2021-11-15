package com.huawei.courselearningdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.ExaminationWareDao;
import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.ExaminationWare;
import com.huawei.courselearningdemo.model.Query;
import com.huawei.courselearningdemo.model.Question;
import com.huawei.courselearningdemo.model.QuestionList;
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
    private final MutableLiveData<QuestionList> questions = new MutableLiveData<>();
    private int testId;

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
    public MutableLiveData<QuestionList> getQuestionsData(){
        return questions;
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
                    System.out.println(response.body());
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
        setTestId(testId);
        Call<QuestionList> questionCall = examinationWareDao.getQuestion(testId);
        questionCall.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    questionData.setValue(response.body().getQuestions());
            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                LogUtil.e(questionCall, "loadQueryWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }

    public void addExamData(ExaminationWare exam){

        Call<ExaminationWare> examCall = examinationWareDao.addExam(exam);

        examCall.enqueue(new Callback<ExaminationWare>() {
            @Override
            public void onResponse(Call<ExaminationWare> call, Response<ExaminationWare> response) {
                if(response.body() == null)
                    LogUtil.e("addExamData", "response Null Error!");
            }

            @Override
            public void onFailure(Call<ExaminationWare> call, Throwable t) {
                LogUtil.e("addCommentData", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
            }
        });

    }
    public void setTestId(int id){
        testId = id;
    }
    public int getTestId(){
        return testId;
    }
    public void judge(List<String> answers,int testId){
        System.out.println(answers);
        System.out.println(testId);


        Call<Integer> judgeCall = examinationWareDao.judge(answers,testId);

        judgeCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body() == null)
                    LogUtil.e("addExamData", "response Null Error!");
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                LogUtil.e("addCommentData", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
            }
        });

    }
    public void addQuestion(Question question){
        Call<String> addQuestionCall = examinationWareDao.addQuestion(question,testId);

        addQuestionCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() == null)
                    LogUtil.e("addExamData", "response Null Error!");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogUtil.e("addCommentData", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
            }
        });

    }
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
