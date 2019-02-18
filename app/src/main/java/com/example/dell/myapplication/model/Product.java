package com.example.dell.myapplication.model;

public class Product {
    private int proImage;
    private String proTitle;
    private double proPrice;
    private int proQuantity;
    private String productDescription;
    private CompanyInfo companyInfo;

    public Product(int image, String proTitle, double proPrice, int proQuantity, CompanyInfo companyInfo) {
        this.proImage = image;
        this.proTitle = proTitle;
        this.proPrice = proPrice;
        this.proQuantity = proQuantity;
        this.companyInfo = companyInfo;
    }

    public int getProImage() {
        return proImage;
    }

    public String getProTitle() {
        return proTitle;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public double getProPrice() {
        return proPrice;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProImageUrl(int proImageUrl) {
        this.proImage = proImageUrl;
    }

    public void setProTitle(String proTitle) {
        this.proTitle = proTitle;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProImage(int proImage) {
        this.proImage = proImage;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
