package com.huawei.courselearningdemo.model;

import java.util.Date;
import java.util.List;

public class ExaminationWare{
    private int id;
    private String title;
    private List<Question> list;
    private int score;
    private int courseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer id) {
        this.courseId = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getList() {return list;}

    public void setList(List<Question> list) {this.list = list;}

}
