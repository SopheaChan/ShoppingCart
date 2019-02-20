package com.example.dell.myapplication.model;

public class ProductData {
    private String salerName;
    private String salerTel;
    private String salerEmail;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String productQuantity;
    private String productDescription;
    private String productID;
    private String userID;

    public ProductData(){}

    public ProductData(String productImage, String productTitle, String productPrice, String productQuantity,
                       String productDescription, String userID, String productID, String salerName,
                       String salerTel, String salerEmail) {
        this.salerName = salerName;
        this.salerTel = salerTel;
        this.salerEmail = salerEmail;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productDescription = productDescription;
        this.userID = userID;
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSalerName() {
        return salerName;
    }

    public String getSalerTel() {
        return salerTel;
    }

    public String getSalerEmail() {
        return salerEmail;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public void setSalerTel(String salerTel) {
        this.salerTel = salerTel;
    }

    public void setSalerEmail(String salerEmail) {
        this.salerEmail = salerEmail;
    }
}
