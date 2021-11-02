package com.huawei.courselearningdemo.repository;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.dao.CourseOrderDao;
import com.huawei.courselearningdemo.model.CourseOrder;
import com.huawei.courselearningdemo.model.ResultVO;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseOrderRepository {
    private final MutableLiveData<Boolean> courseOrderFlag = new MutableLiveData<>();
    private CourseOrderDao courseOrderDao=RetrofitClient.getCourseOrderDao();

    private static CourseOrderRepository uniqueInstance = null;

    public static CourseOrderRepository getCourseRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new CourseOrderRepository();
        }
        return uniqueInstance;
    }

    private CourseOrderRepository(){

    }

    public MutableLiveData<Boolean> getCourseOrderFlagLiveData(){
        return courseOrderFlag;
    }

    public void createCourseOrder(CourseOrder order)
    {

        // 调用后端生成订单的接口
        Call<ResultVO<CourseOrder>> courseOrderCall = courseOrderDao.createCourseOrder(order);
        courseOrderCall.enqueue(new Callback<ResultVO<CourseOrder>>() {
            @Override
            public void onResponse(Call<ResultVO<CourseOrder>> call, Response<ResultVO<CourseOrder>> response) {
                if(response.body() == null || response.body().getCode() == null)
                    LogUtil.e("createCourseOrder", "response Null Error!");
                else if(response.body().getCode().equals(Constant.REQUEST_SUCCESS)){
                    courseOrderFlag.setValue(true);
                    sendCourseOrderSuccessNotify(order.getUserId(),order.getCourseId());
                }else {
                    courseOrderFlag.setValue(false);
                    LogUtil.e("createCourseOrder", response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResultVO<CourseOrder>> call, Throwable t) {
                LogUtil.e("createCourseOrder Fail", t.getMessage());
            }
        });

    }

    private void sendCourseOrderSuccessNotify(String uid,Integer courseId){
        System.out.println("sendCourseOrderSuccessNotify uid = "+uid);
        Call<ResultVO<String>> sendCourseOrderSuccessNotify = RetrofitClient.getNotifyDao().sendCourseOrderSuccessNotify(uid, courseId);
        sendCourseOrderSuccessNotify.enqueue(new Callback<ResultVO<String>>() {
            @Override
            public void onResponse(Call<ResultVO<String>> call, Response<ResultVO<String>> response) {
                if(response.body() == null || response.body().getCode() == null)
                    LogUtil.e("sendCourseOrderSuccessNotify", "response Null Error!");
                else if(response.body().getCode().equals(Constant.REQUEST_SUCCESS))
                    LogUtil.i("sendCourseOrderSuccessNotify", "success");
                else
                    LogUtil.i("sendCourseOrderSuccessNotify", "fail");
            }

            @Override
            public void onFailure(Call<ResultVO<String>> call, Throwable t) {
                LogUtil.i("sendCourseOrderSuccessNotify", "Call fail"+ t.getMessage() + "  caused by: "+t.getCause());
            }
        });
    }

}
