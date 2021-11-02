package com.huawei.courselearningdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

import com.huawei.courselearningdemo.CourseLearningDemoApplication;
import com.huawei.courselearningdemo.repository.UserLocalRepository;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.repository.UserRepository;
import com.huawei.courselearningdemo.utils.LogUtil;

/**
 * SharedViewModel 和MainActivity生命周期绑定，可存储如用户信息等多个View共享的信息。
 */
public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> uid;
    private MutableLiveData<User> userData;
    private UserRepository userRepository = UserRepository.getCourseRepository();
    private AccountAuthService mAuthManager;

    public SharedViewModel(){

    }
    public LiveData<String> getUid(){
        if(uid == null){
            uid = userRepository.getUidLiveData();
            userData = userRepository.getUserDataLiveData();
            // 打开APP时，默认使用silentSignIn尝试登录
            silentSignIn();
        }
        return uid;
    }

    public LiveData<User> getUser(){
        return userData;
    }

    public void updateUserData(User user){
        userRepository.updateUserData(user);
    }
    public void userLoginToServer() {
        userRepository.userLoginToServer();
    }

    public void reloadUserDataFromServer() {
        userRepository.reloadUserDataFromServer();
    }
    public void verifyIdToken(User user, String idToken){
        userRepository.verifyIdToken(user,idToken);
    }
    public void sendRegTokenToServer(String token){
        userRepository.sendRegTokenToServer(token);
    }
    // 静默登录
    private void silentSignIn() {
        AccountAuthParams mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams();
        mAuthManager = AccountAuthManager.getService(CourseLearningDemoApplication.getContext(), mAuthParam);
        Task<AuthAccount> task = mAuthManager.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                LogUtil.i(task, "Silent sign in success");
                User user = new User();
                user.setUid(authAccount.getUnionId());
                user.setUname(authAccount.getDisplayName());
                user.setPicture(authAccount.getAvatarUriString());
                updateUserData(user);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 如果静默登录失败
                if (e instanceof ApiException) {
                    UserLocalRepository.clearUser();
                    ApiException apiException = (ApiException) e;
                    LogUtil.w(task, "Silent sign in error, status code: " + apiException.getStatusCode());
                    LogUtil.w(task, "Silent sign in error, " + apiException.getMessage());
                }
            }
        });
    }

    /**
     * 使用华为账号登录前，需要调用此方法获取AccountAuthService实例
     */
    public AccountAuthService getAccountAuthServiceForSignIn() {
        // 调用AccountAuthParamsHelper.setIdToken方法请求授权。
        AccountAuthParams mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        // 调用AccountAuthManager.getService方法初始化AccountAuthService对象。
        mAuthManager = AccountAuthManager.getService(CourseLearningDemoApplication.getContext(), mAuthParam);
        return mAuthManager;
    }

    /**
     * 用户登出
     * 在一个成功的登出操作之前，我们将所有登录操作所使用的AccountAuthService实例 都同步到mAuthManager
     */
    public void signOut() {
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateUserData(new User());
                LogUtil.i(signOutTask, "Sign out success.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                LogUtil.d(signOutTask, "Sign out fail!");
            }
        });
    }

    /**
     * 取消华为账号授权
     */
    public void cancelAuthorization(){
        mAuthManager.cancelAuthorization().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                updateUserData(new User());
                if (task.isSuccessful()) {
                    //取消授权成功后的处理
                    LogUtil.i("cancelAuthorization", "success");
                } else {
                    //异常处理
                    Exception e = task.getException();
                    if (e instanceof ApiException)
                        LogUtil.e("cancelAuthorization", "fail status code:"+ ((ApiException) e).getStatusCode());
                    else
                        LogUtil.e("consumeOwnedPurchase",  "checkOwedPurchases fail failure: "+e.getMessage() + "  caused by: "+e.getCause());
                }
            }
        });
    }


}
