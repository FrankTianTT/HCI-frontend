package com.huawei.courselearningdemo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * CourseLearningDemoApplication类 用于全局获取Context、读取配置的ApiKey
 */
public class CourseLearningDemoApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String appId;
    private static String apiKey;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public static String getAppId() {
        return appId;
    }

    public static String getApiKey() {
        return apiKey;
    }
}
