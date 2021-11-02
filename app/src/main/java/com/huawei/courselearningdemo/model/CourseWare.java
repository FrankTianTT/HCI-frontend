package com.huawei.courselearningdemo.model;

import java.util.Date;

public class CourseWare {
    private Integer id;

    private Integer courseId;

    private Integer number;

    private String title;

    private String fileName;

    private String fileType;

    private String fileSize;

    private Boolean freeFlag;

    private Boolean downloadFlag;

    private Boolean availableFlag;

    private Date uploadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getFreeFlag() {
        return freeFlag;
    }

    public void setFreeFlag(Boolean freeFlag) {
        this.freeFlag = freeFlag;
    }

    public Boolean getDownloadFlag() {
        return downloadFlag;
    }

    public void setDownloadFlag(Boolean downloadFlag) {
        this.downloadFlag = downloadFlag;
    }

    public Boolean getAvailableFlag() {
        return availableFlag;
    }

    public void setAvailableFlag(Boolean availableFlag) {
        this.availableFlag = availableFlag;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
