package com.huawei.courselearningdemo.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

import com.huawei.courselearningdemo.R;
import com.huawei.courselearningdemo.model.User;
import com.huawei.courselearningdemo.ui.fragment.AccountFragment;
import com.huawei.courselearningdemo.ui.fragment.BaseFragment;
import com.huawei.courselearningdemo.ui.fragment.HomeFragment;
import com.huawei.courselearningdemo.ui.fragment.StudyFragment;
import com.huawei.courselearningdemo.utils.Constant;
import com.huawei.courselearningdemo.utils.LogUtil;
import com.huawei.courselearningdemo.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity {
    // 导航栏
    private BottomNavigationView mainNavigationView;
    // 上一次显示的fragment
    private BaseFragment lastOneFragment = null;
    // fragment管理器
    private FragmentManager fragmentManager = null;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private StudyFragment studyFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initViewModel();
        initObserver();
    }

    private void initView() {
        mainNavigationView = this.findViewById(R.id.main_navigation_bar);
        homeFragment = new HomeFragment();
        studyFragment = new StudyFragment();
        accountFragment = new AccountFragment();
        fragmentManager = getSupportFragmentManager();

        switchFragment(homeFragment);
        mainNavigationView.setSelectedItemId(R.id.home);
    }

    private void initListener(){
        mainNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        switchFragment(homeFragment);
                        break;
                    case R.id.study:
                        switchFragment(studyFragment);
                        break;
                    case R.id.account:
                        switchFragment(accountFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void initViewModel() {
        sharedViewModel = new ViewModelProvider(MainActivity.this).get(SharedViewModel.class);
    }

    private void initObserver() {
        sharedViewModel.getUid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String uid) {
                if(uid != null) {
                    sharedViewModel.userLoginToServer();
                    getToken();
                }
            }
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        // 如果上一个fragment跟当前要切换的fragment是同一个，那么不需要切换
        if(lastOneFragment == targetFragment) {
            return;
        }
        // 使用add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        if(!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        lastOneFragment = targetFragment;
        //fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }

    // 处理用户的登录请求
    public void signIn(){
        // 获取用于登录的AccountAuthManager
        AccountAuthService mAuthManager = sharedViewModel.getAccountAuthServiceForSignIn();
        // 调用getSignInIntent方法并拉起帐号登录授权页面
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(this, "onActivityResult  resultCode: "+ requestCode);
        if (requestCode == Constant.REQUEST_CODE_SIGN_IN) {
            // 华为账号授权登录结果处理，从AuthAccount中获取Authorization Code
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                //登录成功，获取用户的帐号信息和 ID Token
                AuthAccount authAccount = authAccountTask.getResult();
                String idToken = authAccount.getIdToken();
                User user = new User();
                user.setUid(authAccount.getUnionId());
                user.setUname(authAccount.getDisplayName());
                user.setPicture(authAccount.getAvatarUriString());
                //需要对IDToken有效性进行验证
                sharedViewModel.verifyIdToken(user,idToken);
                LogUtil.i(authAccountTask, "Sign in successfully, server AuthCode: " + authAccount.getAuthorizationCode());
            } else {
                //登录失败
                LogUtil.e(authAccountTask, "Sign in failed, status code: " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        } else if(requestCode == Constant.REQUEST_CODE_BUY){
            // 若检查到为华为应用内支付的请求代码，则转交给accountFragment处理
            accountFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    LogUtil.i("MainActivity", "get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        LogUtil.i("getToken ", token);
                        sharedViewModel.sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e("MainActivity", "get token failed, " + e);
                }
            }
        }.start();
    }


}