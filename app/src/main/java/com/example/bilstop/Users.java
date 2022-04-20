package com.example.bilstop;

public class Users {

    private String user_name;
    private String user_surname;
    private String user_email;
    private String user_password;

    private String user_about;
//    private Friends[] user_friends;
//    private Cars[] user_cars;

    public Users(){

    }
    public Users(String user_name, String user_surname, String user_email, String user_password){
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.user_email = user_email;
        this.user_password  = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_about() {
        return user_about;
    }

    public void setUser_about(String user_about) {
        this.user_about = user_about;
    }

//    public Friends[] getUser_friends() {
//        return user_friends;
//    }
//
//    public void setUser_friends(Friends[] user_friends) {
//        this.user_friends = user_friends;
//    }
//
//    public Cars[] getUser_cars() {
//        return user_cars;
//    }
//
//    public void setUser_cars(Cars[] user_cars) {
//        this.user_cars = user_cars;
//    }

}
