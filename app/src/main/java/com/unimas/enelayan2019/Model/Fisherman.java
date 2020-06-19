package com.unimas.enelayan2019.Model;

public class Fisherman {
    private String fishermanID;
    private String fishermanName;
    private String fishermanLicenseNumber;
    private String fishingArea;
    private String phoneNumber;
    private String address;
    private String years;
    private Boolean approvalStatus;

    public Fisherman() {
    }

    public Fisherman(String fishermanID, String fishermanName, String fishermanLicenseNumber, String fishingArea, String phoneNumber, String address, String years, Boolean approvalStatus) {
        this.fishermanID = fishermanID;
        this.fishermanName = fishermanName;
        this.fishermanLicenseNumber = fishermanLicenseNumber;
        this.fishingArea = fishingArea;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.years = years;
        this.approvalStatus = approvalStatus;
    }

    public String getFishermanID() {
        return fishermanID;
    }

    public void setFishermanID(String fishermanID) {
        this.fishermanID = fishermanID;
    }

    public String getFishermanName() {
        return fishermanName;
    }

    public void setFishermanName(String fishermanName) {
        this.fishermanName = fishermanName;
    }

    public String getFishermanLicenseNumber() {
        return fishermanLicenseNumber;
    }

    public void setFishermanLicenseNumber(String fishermanLicenseNumber) {
        this.fishermanLicenseNumber = fishermanLicenseNumber;
    }

    public String getFishingArea() {
        return fishingArea;
    }

    public void setFishingArea(String fishingArea) {
        this.fishingArea = fishingArea;
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

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}


