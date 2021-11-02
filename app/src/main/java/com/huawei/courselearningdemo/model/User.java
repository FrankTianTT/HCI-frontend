package com.huawei.courselearningdemo.model;

public class User {
    private String uid;

    private String uname;

    private String picture;

    private Integer balance;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String toString(){
        return "uid: " + uid + "\r\n"
                + "uname: " + uname + "\r\n"
                + "picture: " + picture + "\r\n"
                + "balance: " + balance + "\r\n";
    }
}
