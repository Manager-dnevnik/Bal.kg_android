package com.alanaandnazar.qrscanner.model;

public class Subject {
    int id;
    String name_subject;
    String time_start;
    String name;

    String mark;
    String comm;
    String date;
    String homework;

    public void setId(int id) {
        this.id = id;
    }

    public void setName_subject(String name_subject) {
        this.name_subject = name_subject;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public int getId() {
        return id;
    }

    public String getName_subject() {
        return name_subject;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getHomework() {
        return homework;
    }
}
