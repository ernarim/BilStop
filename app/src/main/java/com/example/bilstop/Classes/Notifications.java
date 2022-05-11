package com.example.bilstop.Classes;

import java.io.Serializable;

public class Notifications implements Serializable {
    private String senderUserId, targetUid, currentState, name, profilePicture ;
    private Ride ride;
    private String notificationId;

    public Notifications(){

    }

    public Notifications(String senderUserId, String targetUid, String currentState, String name, String profilePicture)
    {
        this.senderUserId = senderUserId;
        this.targetUid = targetUid;
        this.currentState = currentState;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public Notifications(String senderUserId, String targetUid, String currentState, String name, String profilePicture, Ride ride)
    {
        this.senderUserId = senderUserId;
        this.targetUid = targetUid;
        this.currentState = currentState;
        this.name = name;
        this.profilePicture = profilePicture;
        this.ride = ride;
    }


    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCurrentState() { return this.currentState; }

    public void setCurrentState(String currentState) {this.currentState = currentState;}

    public String getSenderUserId() {
        return this.senderUserId;
    }

    public void setSenderUserId(String senderUserId) { this.senderUserId = senderUserId; }

    public String getTargetUid() {
        return this.targetUid;
    }

    public void setTargetUid(String targetUid) { this.targetUid = targetUid; }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "senderUserId='" + senderUserId + '\'' +
                ", targetUid='" + targetUid + '\'' +
                ", currentState='" + currentState + '\'' +
                ", name='" + name + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", ride=" + ride +
                ", notificationId='" + notificationId + '\'' +
                '}';
    }
}
