package com.huawei.courselearningdemo.repository;

import androidx.lifecycle.MutableLiveData;

import com.huawei.courselearningdemo.model.ResultVO;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.network.RetrofitClient;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<String> uid = new MutableLiveData<>();

    private static UserRepository uniqueInstance = null;

    public static UserRepository getCourseRepository(){
        if (uniqueInstance == null)
        {
            uniqueInstance = new UserRepository();
        }
        return uniqueInstance;
    }

    private UserRepository(){

    }
    public MutableLiveData<String>  getUidLiveData(){
        return uid;
    }
    public MutableLiveData<User>  getUserDataLiveData(){
        return userData;
    }


    public void updateUserData(User user){
//        LogUtil.e(this, "updateUserData  user: "+user);
        if(user==null || user.getUid()==null){
            uid.setValue(null);
            UserLocalRepository.clearUser();
        }else{
            userData.setValue(user);
            UserLocalRepository.saveUser(user);
            if(uid.getValue() == null || !uid.getValue().equals(user.getUid()))
                uid.setValue(user.getUid());
        }
    }


    public void userLoginToServer() {
        Call<User> userCall = RetrofitClient.userDao.login(userData.getValue());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
//                LogUtil.i("userLoginToServer", "get response: "+response.body());
                if(response.body() == null || response.body().getUid() == null)
                    LogUtil.e("userLoginToServer", "response Null Error!");
                else
                    updateUserData(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                LogUtil.e("userLoginToServer", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
            }
        });
    }

    // 从应用服务器重新拉取用户账号信息
    public void reloadUserDataFromServer() {
        Call<User> userCall = RetrofitClient.userDao.getUser(uid.getValue());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                LogUtil.i("reloadUserDataFromServer", "get response");
                if(response.body() == null || response.body().getUid() == null)
                    LogUtil.e("reloadUserDataFromServer", "response Null Error!");
                else
                    updateUserData(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                LogUtil.e("reloadUserDataFromServer", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());
            }
        });
    }

    public void verifyIdToken(User user, String idToken){
        //需要对IDToken有效性进行验证
        Call<ResultVO<String>> verifyCall = RetrofitClient.getUserDao().verifyIdToken(idToken);
        verifyCall.enqueue(new Callback<ResultVO<String>>() {
            @Override
            public void onResponse(Call<ResultVO<String>> call, Response<ResultVO<String>> response) {
                if(response.body().getCode().equals(Constant.REQUEST_SUCCESS))
                    updateUserData(user);
                else
                    LogUtil.e("Verify ID Token failed ",response.body().getMsg());
            }

            @Override
            public void onFailure(Call<ResultVO<String>> call, Throwable t) {
                LogUtil.e("Verify ID Token ", "Call failure: "+ t.getMessage() + "  caused by: "+t.getCause());;
            }
        });
    }

    public void sendRegTokenToServer(String token){
        Call<ResultVO<String>> refreshUserTokenCall = RetrofitClient.getUserDao().refreshUserToken(uid.getValue(), token);
        refreshUserTokenCall.enqueue(new Callback<ResultVO<String>>() {
            @Override
            public void onResponse(Call call, Response response) {
                LogUtil.i("refreshUserToken", "success");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                LogUtil.i("refreshUserToken", "fail");
            }
        });
    }

}
