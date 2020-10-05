package com.alanaandnazar.qrscanner.model;

import java.util.List;

public class Schedule {

    private String day_name;
    private List<Subject> list_subjects;

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    public void setList_subjects(List<Subject> list_subjects) {
        this.list_subjects = list_subjects;
    }

    public String getDay_name() {
        return day_name;
    }

    public List<Subject> getList_subjects() {
        return list_subjects;
    }
}
