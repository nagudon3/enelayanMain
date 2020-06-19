package com.unimas.enelayan2019.Model;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String comment, userId, userName, userImage;
    private Object timeStamp;

    public Comment() {
    }

    public Comment(String comment, String userId, String userName, String userImage) {
        this.comment = comment;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
