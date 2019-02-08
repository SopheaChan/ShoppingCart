package com.example.dell.myapplication.model;

import android.net.Uri;

public class UserInfo {
    private Uri profileUrl;
    private String uName;
    private String uGender;
    private String uTel;
    private String uEmail;
    private String uOther;

    public UserInfo(Uri profileUrl, String uName, String uGender, String uTel, String uEmail, String uOther) {
        this.profileUrl = profileUrl;
        this.uName = uName;
        this.uGender = uGender;
        this.uTel = uTel;
        this.uEmail = uEmail;
        this.uOther = uOther;
    }

    public void setProfileUrl(Uri profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public void setuOther(String uOther) {
        this.uOther = uOther;
    }

    public Uri getProfileUrl() {
        return profileUrl;
    }

    public String getuName() {
        return uName;
    }

    public String getuGender() {
        return uGender;
    }

    public String getuTel() {
        return uTel;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuOther() {
        return uOther;
    }
}
