package com.unimas.enelayan2019.Model;

public class PostViewer {
    private String userName;
    private String details;
    private String userPics;
    private String image;

    public PostViewer() {
    }

    public PostViewer(String userName, String details, String userPics, String image) {
        this.userName = userName;
        this.details = details;
        this.userPics = userPics;
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUserPics() {
        return userPics;
    }

    public void setUserPics(String userPics) {
        this.userPics = userPics;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
