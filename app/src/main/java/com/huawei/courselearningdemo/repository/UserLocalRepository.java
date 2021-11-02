package com.huawei.courselearningdemo.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.huawei.courselearningdemo.CourseLearningDemoApplication;
import com.huawei.courselearningdemo.model.User;

/**
 * 使用SharedPreferences 实现本地数据库
 */
public class UserLocalRepository {

    private static final SharedPreferences sharedPreferences = CourseLearningDemoApplication.getContext().getSharedPreferences("course_learning", Context.MODE_PRIVATE);

    public static void saveUser(User user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", new Gson().toJson(user));
        editor.apply();
    }

    public static void clearUser(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user");
        editor.apply();
    }

    public static User getUser(){
        String userJson = sharedPreferences.getString("user", "");
        return new Gson().fromJson(userJson, User.class);
    }

    public static boolean isUserSaved(){
        return sharedPreferences.contains("user");
    }
}
