package com.huawei.courselearningdemo.utils;

public class StringUtil {
    public static boolean hasText(String str){
        return str != null && str.length() > 0 && str.trim().length() > 0;
    }
}
