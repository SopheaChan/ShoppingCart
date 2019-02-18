package com.example.dell.myapplication.model;

public class ProductData {
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String productQuantity;
    private String productDescription;

    public ProductData(String productImage, String productTitle, String productPrice, String productQuantity, String productDescription) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productDescription = productDescription;
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
}
