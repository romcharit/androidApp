package com.example.mixmaster.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    public String id = "";
    public String userName = "";
    public String cocktailName = "";
    public String cocktailDescription = "";
    public String cocktailRecipe= "";
    public String avatarUrl = "";
    public String cocktailUrl = "";
    public String likeUrl = "";

    public Post(){}

    public Post(String id, String userName, String cocktailName, String cocktailDescription,
                String cocktailRecipe, String avatarUrl, String cocktailUrl, String likeUrl)
    {
        this.id = id;
        this.userName = userName;
        this.cocktailName = cocktailName;
        this.cocktailDescription = cocktailDescription;
        this.cocktailRecipe = cocktailRecipe;
        this.avatarUrl = avatarUrl;
        this.cocktailUrl = cocktailUrl;
        this.likeUrl = likeUrl;
    }

    public static Post fromJson(Map<String,Object> json){
        String id = (String)json.get("id");
        String username = (String)json.get("username");
        String cocktailName = (String)json.get("cocktailName");
        String description = (String)json.get("description");
        String recipe = (String)json.get(("recipe"));
        String avatar = (String)json.get(("avatar"));
        String cocktailImg = (String)json.get(("cocktailImg"));
        String like = (String)json.get(("like"));
        Post post = new Post(id,username,cocktailName,description,recipe,avatar,cocktailImg,like);
        return post;
    }

    public Map<String,Object> toJson()
    {
        Map<String, Object> json = new HashMap<>();
        json.put("id", getId());
        json.put("username", getUserName());
        json.put("cocktailName", getCocktailName());
        json.put("description", getCocktailDescription());
        json.put("recipe", getCocktailRecipe());
        json.put("avatar", getAvatarUrl());
        json.put("cocktailImg", getCocktailUrl());
        json.put("like", getLikeUrl());
        return json;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public String getCocktailRecipe() {
        return cocktailRecipe;
    }

    public String getCocktailDescription() {
        return cocktailDescription;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
    public String getCocktailUrl() {
        return cocktailUrl;
    }
    public String getLikeUrl() {
        return likeUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCocktailUrl(String cocktailUrl) {
        this.cocktailUrl = cocktailUrl;
    }

    public void setLikeUrl(String likeUrl) {
        this.likeUrl = likeUrl;
    }

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
