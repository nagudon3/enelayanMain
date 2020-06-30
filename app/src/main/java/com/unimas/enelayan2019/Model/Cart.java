package com.unimas.enelayan2019.Model;

public class Cart {
    private String uid;
    private String cartId;
    private String productId;
    private String productName;
    private String productImage;
    private String amountOrdered;
    private String price;
    private Boolean isCod;
    private Boolean isPickup;
    private String sellerId;

    public Cart() {
    }

    public Cart(String uid, String cartId, String productId, String productName, String productImage, String amountOrdered, String price, Boolean isCod, Boolean isPickup, String sellerId) {
        this.uid = uid;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.amountOrdered = amountOrdered;
        this.price = price;
        this.isCod = isCod;
        this.isPickup = isPickup;
        this.sellerId = sellerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getAmountOrdered() {
        return amountOrdered;
    }

    public void setAmountOrdered(String amountOrdered) {
        this.amountOrdered = amountOrdered;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
