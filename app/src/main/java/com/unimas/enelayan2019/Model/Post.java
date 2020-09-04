package com.unimas.enelayan2019.Model;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String details;
    private String image;
    private String userId;
    private String userName;
    private String userPics;
    private Object timeStamp;

    public Post() {
    }

    public Post(String details, String image, String userId, String userName, String userPics) {
        this.details = details;
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.userPics = userPics;
        this.timeStamp = ServerValue.TIMESTAMP;

    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPics() {
        return userPics;
    }

    public void setUserPics(String userPics) {
        this.userPics = userPics;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
