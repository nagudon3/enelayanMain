package com.unimas.enelayan2019.Model;

import android.widget.CheckBox;

public class Product {
    private String productId;
    private String sellerId;
    private String sellerImage;
    private String sellerName;
    private String sellerPhone;
    private String sellerAddress;
    private String productCategory;
    private String productName;
    private String productPricePerKg;
    private String amountAvailable;
    private Boolean isCod;
    private Boolean isPickup;
    private String productImage;
    private String sellingArea;

    public Product() {
    }

    public Product(String sellerId, String sellerImage, String sellerName, String sellerPhone, String sellerAddress, String productCategory,
                   String productName, String productPricePerKg, String amountAvailable, Boolean isCod, Boolean isPickup, String productImage, String sellingArea) {
        this.sellerId = sellerId;
        this.sellerImage = sellerImage;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sellerAddress = sellerAddress;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productPricePerKg = productPricePerKg;
        this.amountAvailable = amountAvailable;
        this.isCod = isCod;
        this.isPickup = isPickup;
        this.productImage = productImage;
        this.sellingArea = sellingArea;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPricePerKg() {
        return productPricePerKg;
    }

    public void setProductPricePerKg(String productPricePerKg) {
        this.productPricePerKg = productPricePerKg;
    }

    public String getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(String amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Boolean getCod() {
        return isCod;
    }

    public void setCod(Boolean cod) {
        isCod = cod;
    }

    public Boolean getPickup() {
        return isPickup;
    }

    public void setPickup(Boolean pickup) {
        isPickup = pickup;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSellingArea() {
        return sellingArea;
    }

    public void setSellingArea(String sellingArea) {
        this.sellingArea = sellingArea;
    }
}
