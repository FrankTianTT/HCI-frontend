package com.huawei.courselearningdemo.utils;

import android.widget.Toast;

import com.huawei.courselearningdemo.CourseLearningDemoApplication;

public class ToastUtil {
    private static Toast toast;
    public static void showShortToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(CourseLearningDemoApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showLongToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(CourseLearningDemoApplication.getContext(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
