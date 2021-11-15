package com.huawei.courselearningdemo.model;

import java.util.List;

public class QuestionList {
    private List<Question> questions;
    private int len;
    public List<Question> getQuestions(){
        return questions;
    }
    public void setQuestions(List<Question> questions){
        this.questions = questions;
    }
    public int getLen(){
        return len;
    }
    public void setLen(int len){
        this.len = len;
    }
}
