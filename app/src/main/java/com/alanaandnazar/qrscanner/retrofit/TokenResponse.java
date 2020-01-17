package com.alanaandnazar.qrscanner.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("fio")
    @Expose
    private String fio;

    @SerializedName("about")
    @Expose
    private String about;

    @SerializedName("mess")
    @Expose
    private String mess;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "status='" + status + '\'' +
                ", fio='" + fio + '\'' +
                ", about='" + about + '\'' +
                ", mess='" + mess + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
