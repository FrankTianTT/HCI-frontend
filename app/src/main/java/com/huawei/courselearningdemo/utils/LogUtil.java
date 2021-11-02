package com.huawei.courselearningdemo.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtil {

    private static int level = 2;
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;

    public static void v(Object object, String message) {
        if(level <= VERBOSE) {
            Log.v(objTag(object), message);
        }
    }

    public static void d(Object object, String message) {
        if(level <= DEBUG) {
            Log.d(objTag(object), message);
        }
    }

    public static void i(Object object, String message) {
        if(level <= INFO) {
            Log.i(objTag(object), message);
        }
    }

    public static void w(Object object, String message) {
        if(level <= WARN) {
            Log.w(objTag(object), message);
        }
    }

    public static void e(Object object, String message) {
        if(level <= ERROR) {
            Log.e(objTag(object), message);
        }
    }

    public static String objTag(Object obj){
        return obj instanceof String ? obj.toString():obj.getClass().getSimpleName();
    }
}
