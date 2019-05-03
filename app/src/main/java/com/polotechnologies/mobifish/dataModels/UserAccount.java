package com.polotechnologies.mobifish.dataModels;

public class UserAccount {

    public String userID;
    public String userCategory;
    public String userIdNumber;

    public UserAccount() {
    }

    public UserAccount(String userID, String userCategory, String userIdNumber) {
        this.userID = userID;
        this.userCategory = userCategory;
        this.userIdNumber = userIdNumber;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public String getUserIdNumber() {
        return userIdNumber;
    }
}
