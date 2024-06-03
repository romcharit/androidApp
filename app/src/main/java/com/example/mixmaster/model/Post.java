package com.example.mixmaster.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    @PrimaryKey
    public String cocktailName = "";
    public String cocktailDescription = "";
    public String cocktailRecipe= "";


//    public String userName = "";
//    public String cocktailName = "";
//    public String avatarUrl = "";
//    public String cocktailUrl = "";
//    public String likeUrl = "";

    public Post(){}

    public Post(String cocktailName, String cocktailDescription, String cocktailRecipe){
        this.cocktailName = cocktailName;
        this.cocktailDescription = cocktailDescription;
        this.cocktailRecipe = cocktailRecipe;
    }

//    public Post(String userName, String cocktailName, String avatarUrl, String cocktailUrl, String likeUrl){
//        this.userName = userName;
//        this.cocktailName = cocktailName;
//        this.avatarUrl = avatarUrl;
//        this.cocktailUrl = cocktailUrl;
//        this.likeUrl = likeUrl;
//    }

//    public String getUserName() {
//        return userName;
//    }
//
    public String getCocktailName() {
        return cocktailName;
    }
    public String getCocktailRecipe() {
        return cocktailRecipe;
    }
    public String getCocktailDescription() {
        return cocktailDescription;
    }
//
//    public String getAvatarUrl() {
//        return avatarUrl;
//    }
//
//    public String getCocktailUrl() {
//        return cocktailUrl;
//    }
//
//    public String getLikeUrl() {
//        return likeUrl;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//
//    public void setAvatarUrl(String avatarUrl) {
//        this.avatarUrl = avatarUrl;
//    }
//
//    public void setCocktailUrl(String cocktailUrl) {
//        this.cocktailUrl = cocktailUrl;
//    }
//
//    public void setLikeUrl(String likeUrl) {
//        this.likeUrl = likeUrl;
//    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public void setCocktailDescription(String cocktailDescription) {
        this.cocktailDescription = cocktailDescription;
    }

    public void setCocktailRecipe(String cocktailRecipe) {
        this.cocktailRecipe = cocktailRecipe;
    }
}
