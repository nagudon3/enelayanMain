package com.unimas.enelayan2019.Model;

public class Purchase {
    private String purchaseId;
    private String productId;
    private String sellerId;
    private String customerId;
    private String amountPurchased;
    private String purchasePrice;
    private String productName;
    private String productImage;
    private String customerPhone;
    private String customerAddress;
    private String customerName;
    private String paymentMethod;

    public Purchase() {
    }

    public Purchase(String purchaseId, String productId, String sellerId, String customerId,
                    String amountPurchased, String purchasePrice, String productName, String productImage,
                    String customerPhone, String customerAddress, String customerName, String paymentMethod) {
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.customerId = customerId;
        this.amountPurchased = amountPurchased;
        this.purchasePrice = purchasePrice;
        this.productName = productName;
        this.productImage = productImage;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAmountPurchased() {
        return amountPurchased;
    }

    public void setAmountPurchased(String amountPurchased) {
        this.amountPurchased = amountPurchased;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
