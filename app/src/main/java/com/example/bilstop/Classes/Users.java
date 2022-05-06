package com.example.bilstop.Classes;

import java.util.List;

public class Users {
    private String about, email, name, profilePicture, surname, id;

    public Users(){

    }

    public Users(String about, String email, String id, String name, String profilePicture, String surname) {
        this.about = about;
        this.email = email;
        this.name = name;
        this.profilePicture = profilePicture;
        this.surname = surname;
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
