package com.example.churchbooking.models;

//userID1{
//        userName = "James"
//        churchMembershipId = "churchId"
//        profilePic = ""
//        userID = "userId1"
//
//        }

public class User {
    String userName;
    String churchMembershipId;
    String profilePic;
    String userId;

    public User() {
    }

    public User(String userName, String churchMembershipId, String profilePic,String userId) {
        this.userName = userName;
        this.churchMembershipId = churchMembershipId;
        this.profilePic = profilePic;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChurchMembershipId() {
        return churchMembershipId;
    }

    public void setChurchMembershipId(String churchMembershipId) {
        this.churchMembershipId = churchMembershipId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", churchMembershipId='" + churchMembershipId + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", userID='" + userId + '\'' +
                '}';
    }
}
