package com.huawei.courselearningdemo.repository;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.CommentDao;
import com.huawei.courselearningdemo.model.Course;
import com.huawei.courselearningdemo.model.Comment;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.utils.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private CommentDao commentDao = RetrofitClient.getCommentDao();
    private final MutableLiveData<List<Comment>> commentData = new MutableLiveData<>();

    private static CommentRepository uniqueInstance = null;

    public static CommentRepository getCommentWareRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new CommentRepository();
        }
        return uniqueInstance;
    }

    private CommentRepository(){

    }
    public MutableLiveData<List<Comment>> getCommentLiveData(){
        return commentData;
    }

    public void loadCommentData(Course course){
        String uid = null;
        User user = UserLocalRepository.getUser();
        if(user!=null && user.getUid()!=null)
            uid = user.getUid();
        if(course==null || course.getId()==null)
            return;
        Call<List<Comment>> commentCall = commentDao.getComment(course.getId(), uid);
        commentCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.body() == null)
                    ToastUtil.showShortToast("后端数据异常...");
                else
                    commentData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                LogUtil.e(commentCall, "loadCommentWareData failure: "+t.getMessage() + "  caused by: "+t.getCause());
                ToastUtil.showShortToast("网络错误，请稍后重试！");
            }
        });
    }
}
