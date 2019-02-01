package com.example.dell.myapplication.model;

public class CompanyInfo {
    private String companyName;
    private String tel;
    private String email;

    public CompanyInfo (String companyName, String tel, String email){
        this.companyName = companyName;
        this.tel = tel;
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
