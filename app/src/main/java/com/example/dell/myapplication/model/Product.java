package com.example.dell.myapplication.model;

public class Product {
    private int proImage;
    private String proName;
    private String proPrice;
    private int proQuantity;

    public Product(int image, String proName, String proPrice, int proQuantity) {
        this.proImage = image;
        this.proName = proName;
        this.proPrice = proPrice;
        this.proQuantity = proQuantity;
    }

    public int getProImage() {
        return proImage;
    }

    public String getProName() {
        return proName;
    }

    public String getProPrice() {
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

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }
}
