package com.unimas.enelayan2019.Model;

public class Product {
    private String productId;
    private String sellerId;
    private String productCategory;
    private String productName;
    private String productPricePerKg;
    private int amountAvailable;
    private String paymentMethod;
    private String productImage;

    public Product() {
    }

    public Product(String productId, String sellerId, String productCategory,
                   String productName, String productPricePerKg, int amountAvailable, String paymentMethod, String productImage) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productPricePerKg = productPricePerKg;
        this.amountAvailable = amountAvailable;
        this.paymentMethod = paymentMethod;
        this.productImage = productImage;
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

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
