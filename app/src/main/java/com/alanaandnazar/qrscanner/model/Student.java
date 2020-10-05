package com.alanaandnazar.qrscanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {

    int id;
    String first_name;
    String last_name;
    String second_name;

    String parent_1;
    String parent_2;
    String phone;
    String status;
    @SerializedName("class")
    @Expose
    private String grade;
    String school;
    String img;
    String fio;
    int move_status;
    String move_about;
    int mark_position = 0;
    String mark;

    public void setMark_position(int mark_position) {
        this.mark_position = mark_position;
    }

    public int getMark_position() {
        return mark_position;
    }

    public void setMove_about(String move_about) {
        this.move_about = move_about;
    }

    public String getMove_about() {
        return move_about;
    }

    public void setMove_status(int move_status) {
        this.move_status = move_status;
    }

    public int getMove_status() {
        return move_status;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getFio() {
        return fio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getParent_1() {
        return parent_1;
    }

    public void setParent_1(String parent_1) {
        this.parent_1 = parent_1;
    }

    public String getParent_2() {
        return parent_2;
    }

    public void setParent_2(String parent_2) {
        this.parent_2 = parent_2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }
}
