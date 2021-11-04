package com.huawei.courselearningdemo.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.CommentWareDao;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.CommentWare;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentWareRepository {
    private CommentWareDao commentWareDao = RetrofitClient.getCommentWareDao();
    private final MutableLiveData<List<CommentWare>> commentWareData = new MutableLiveData<>();

    private static CommentWareRepository uniqueInstance = null;

    public static CommentWareRepository getCommentWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new CommentWareRepository();
        }
        return uniqueInstance;
    }

    private CommentWareRepository(){

    }
    public MutableLiveData<List<CommentWare>> getCommentWareLiveData(){
        return commentWareData;
    }

    public void loadCommentWareData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<CommentWare>> commentWareCall = commentWareDao.getCommentWare(course.getId(), uid);
        commentWareCall.enqueue(new Callback<List<CommentWare>>() {
            @Override
            public void onResponse(Call<List<CommentWare>> call, Response<List<CommentWare>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    commentWareData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CommentWare>> call, Throwable t) {
                LogUtil.e(commentWareCall, "loadCommentWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }
}
