package com.huawei.courselearningdemo.utils;

import android.content.Context;

import com.huawei.courselearningdemo.ui.fragment.QuestionFragment;

public class ScreenUtils {

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     * @param context
     */
    public static int getScreenWidth(QuestionFragment context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

}