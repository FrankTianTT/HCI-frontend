package com.huawei.courselearningdemo.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import com.huawei.courselearningdemo.dao.CourseDao;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.PageInfo;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LoadState;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRepository {
    private CourseDao courseDao = RetrofitClient.getCourseDao();
    private final MutableLiveData<LoadState> stateData = new MutableLiveData<>();
    private final MutableLiveData<Course> courseData = new MutableLiveData<>();
    private final MutableLiveData<List<Course>> courseListData = new MutableLiveData<>();
    private final MutableLiveData<List<Course>> studyCourseListData = new MutableLiveData<>();
    private Integer currentPage = 1;
    private static CourseRepository uniqueInstance = null;

    public static CourseRepository getCourseRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new CourseRepository();
        }
        return uniqueInstance;
    }

    private CourseRepository(){

    }
    public Integer getCurrentPage(){
        return currentPage;
    }
    public MutableLiveData<Course> getCourseLiveData(){
        return courseData;
    }
    public MutableLiveData<List<Course>> getCourseListLiveData(){
        return courseListData;
    }
    public MutableLiveData<LoadState> getStateDataLiveData(){
        return stateData;
    }
    public MutableLiveData<List<Course>> getStudyCourseListLiveData() {
        return studyCourseListData;
    }

    public void loadCourseData(Integer courseId){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        Call<Course> courseCall = courseDao.getCourse(courseId, uid);
        courseCall.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("Backend abnormal data ...");
                else {
                    courseData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                LogUtil.e(courseCall, "loadCourse failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("Network Error，retry it！");
            }
        });

    }
    public void loadCourseListData(Integer currentPageNum, String uid, String queryKey){
        stateData.setValue(LoadState.LOADING);
        Call<PageInfo<Course>> courseCall =  courseDao.getAllCourses(currentPageNum, uid, queryKey);
        // 异步调用
        courseCall.enqueue(new Callback<PageInfo<Course>>() {
            @Override
            public void onResponse(Call<PageInfo<Course>> call, Response<PageInfo<Course>> response) {
                LogUtil.i(courseCall, "Get response");
                if (response.body() == null){
                    stateData.setValue(LoadState.ERROR);
                    currentPage--;
                } else if (response.body().getSize() == 0 || currentPage > response.body().getPages() ){
                    stateData.setValue(LoadState.EMPTY);
                    currentPage--;
                } else {
                    stateData.setValue(LoadState.SUCCESS);
                    List<Course> temp = (courseListData.getValue() != null) ? courseListData.getValue() : new ArrayList<>();
                    temp.addAll(response.body().getList());
                    courseListData.setValue(temp);
                }
            }

            @Override
            public void onFailure(Call<PageInfo<Course>> call, Throwable t) {
                LogUtil.e(courseCall, "loadCourseListPageInfo failure: "+ t.getMessage() + "  caused by: "+t.getCause());
                stateData.setValue(LoadState.ERROR);
                currentPage--;
            }
        });
    }


    public void loadStudyCourseListData(String uid){
        if(uid == null) return;
        LogUtil.i(this, "Start to load user's paid course list.");
        Call<List<Course>> courseCall =  RetrofitClient.getCourseDao().getCourseByUid(uid);
        // 异步调用
        courseCall.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.body() == null)
                    ToastUtil.showShortToast("Backend abnormal data ...");
                studyCourseListData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                LogUtil.e(courseCall, "getCourseByUid failure: "+ t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("Network Error，retry it！");
            }
        });
    }




}
