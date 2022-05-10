package com.example.bilstop.Classes;

import java.io.Serializable;

public class Notifications implements Serializable {
    private String senderUserId, targetUid, currentState, name, profilePicture ;

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

    public String getName() {
        return name;
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




}
