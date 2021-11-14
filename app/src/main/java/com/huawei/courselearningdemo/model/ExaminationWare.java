package com.huawei.courselearningdemo.model;

import java.util.Date;
import java.util.List;

public class ExaminationWare{
    private int id;
    private String content;
    private String title;
    private List<Question> list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
