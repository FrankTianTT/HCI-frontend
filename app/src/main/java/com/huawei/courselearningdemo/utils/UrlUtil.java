package com.huawei.courselearningdemo.utils;

public class UrlUtil {

    public static String getCourseImageUrl(String pictureName) {
        return Constant.HOST + pictureName;
    }

//    public static String getCoverPath(String pict_url) {
//        if(pict_url.startsWith("http") || pict_url.startsWith("https")) {
//            return pict_url;
//        } else {
//            return "https:" + pict_url;
//        }
//    }
}
