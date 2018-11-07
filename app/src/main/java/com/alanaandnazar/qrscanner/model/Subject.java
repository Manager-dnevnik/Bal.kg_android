package com.alanaandnazar.qrscanner.model;

public class Subject {
     int id;
     String name_subject;
     String time_start;

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
}
