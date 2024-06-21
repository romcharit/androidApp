package com.example.mixmaster.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String userName;
    public String avatar;
    public String email;
    public String country;

    public User(){}

    public User(String userName, String avatar, String email, String country){
        this.userName = userName;
        this.avatar = avatar;
        this.email = email;
        this.country = country;
    }

    static final String USER_NAME = "username";
    static final String AVATAR = "avatar";
    static final String EMAIL = "email";
    static final String COUNTRY = "country";

    public static User fromJson(Map<String,Object> json){
        String username = (String)json.get(USER_NAME);
        String avatar = (String)json.get(AVATAR);
        String email = (String)json.get(EMAIL);
        String country = (String)json.get(COUNTRY);
        User user = new User(username, avatar, email, country);
        return user;
    }

    public static Map<String,Object> toJson(User user) {
        Map<String, Object> json = new HashMap<>();
        json.put(USER_NAME, user.getUserName());
        json.put(AVATAR, user.getAvatar());
        json.put(EMAIL, user.getEmail());
        json.put(COUNTRY, user.getCountry());
        return json;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
