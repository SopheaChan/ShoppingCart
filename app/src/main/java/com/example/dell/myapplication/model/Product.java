package com.example.dell.myapplication.model;

public class Product {
    private int proImage;
    private String proName;
    private double proPrice;
    private int proQuantity;
    private String productDescription;
    private CompanyInfo companyInfo;

    public Product(int image, String proName, double proPrice, int proQuantity, CompanyInfo companyInfo) {
        this.proImage = image;
        this.proName = proName;
        this.proPrice = proPrice;
        this.proQuantity = proQuantity;
        this.companyInfo = companyInfo;
    }

    public int getProImage() {
        return proImage;
    }

    public String getProName() {
        return proName;
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

    public void setProName(String proName) {
        this.proName = proName;
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
