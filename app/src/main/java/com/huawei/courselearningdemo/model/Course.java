package com.huawei.courselearningdemo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 实现序列化接口，使得Course类的实例对象可以在Activity之间传递
 */
public class Course implements Serializable {
    private Integer id;
    private String name;
    private String type;
    private String intro;
    private String picture;
    private String provider;
    private Integer cost;
    private boolean bought;
    private boolean starred;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", intro='" + intro + '\'' +
                ", picture='" + picture + '\'' +
                ", provider='" + provider + '\'' +
                ", cost=" + cost +
                ", bought=" + bought +
                ", starred=" + starred +
                ", createTime=" + createTime +
                '}';
    }
}
