package com.unimas.enelayan2019.Model;

public class Seller {
    private String sellerId;
    private String sellerName;
    private String sellerImage;
    private String sellingArea;
    private String sellingReason;
    private String sellingFishSource;
    private String phoneNumber;
    private String address;
    private Boolean approvalStatus;

    public Seller() {
    }

    public Seller(String sellerId, String sellerName, String sellerImage, String sellingArea, String sellingReason, String sellingFishSource, String phoneNumber, String address, Boolean approvalStatus) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerImage = sellerImage;
        this.sellingArea = sellingArea;
        this.sellingReason = sellingReason;
        this.sellingFishSource = sellingFishSource;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.approvalStatus = approvalStatus;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getSellingArea() {
        return sellingArea;
    }

    public void setSellingArea(String sellingArea) {
        this.sellingArea = sellingArea;
    }

    public String getSellingReason() {
        return sellingReason;
    }

    public void setSellingReason(String sellingReason) {
        this.sellingReason = sellingReason;
    }

    public String getSellingFishSource() {
        return sellingFishSource;
    }

    public void setSellingFishSource(String sellingFishSource) {
        this.sellingFishSource = sellingFishSource;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}

