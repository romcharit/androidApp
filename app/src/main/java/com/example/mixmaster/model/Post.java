package com.example.mixmaster.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mixmaster.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    public String id = "";
    public String userName = "";
    public String avatarUrl = "";
    public String cocktailName = "";
    public String cocktailDescription = "";
    public String cocktailRecipe= "";
    public String cocktailUrl = "";
    public String likeUrl = "";
    public String category = "";
    public Long lastUpdated;

    public Post(){}

    public Post(String id, String userName, String category, String cocktailName, String cocktailDescription,
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
        this.category = category;
    }

    static final String ID = "id";
    static final String USER_NAME = "username";
    static final String COCKTAIL_NAME = "cocktailName";
    static final String DESCRIPTION = "description";
    static final String RECIPE = "recipe";
    static final String AVATAR = "avatar";
    static final String COCKTAIL_IMAGE = "cocktailImg";
    static final String LIKE = "like";
    static final String CATEGORY = "category";
    static final String COLLECTION = "posts";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "posts_local_last_updated";


    public static Post fromJson(Map<String,Object> json)
    {
        String id = (String)json.get(ID);
        String username = (String)json.get(USER_NAME);
        String cocktailName = (String)json.get(COCKTAIL_NAME);
        String description = (String)json.get(DESCRIPTION);
        String recipe = (String)json.get((RECIPE));
        String avatar = (String)json.get((AVATAR));
        String cocktailImg = (String)json.get((COCKTAIL_IMAGE));
        String like = (String)json.get((LIKE));
        String category = (String)json.get((CATEGORY));

        Post post = new Post(id, username, category, cocktailName, description, recipe, avatar, cocktailImg, like);

       try{
           Timestamp time = (Timestamp) json.get(LAST_UPDATED);
           post.setLastUpdated(time.getSeconds());
       }catch (Exception e) {}

       return post;
    }

    public static Long getPostLocalLastUpdate() {
        SharedPreferences sharedPef = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPef.getLong(LOCAL_LAST_UPDATED,0);
    }

    public static void setPostLocalLastUpdate(Long time) {
        MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
        .edit().putLong(LOCAL_LAST_UPDATED, time).commit();
    }

    public Map<String,Object> toJson()
    {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(USER_NAME, getUserName());
        json.put(COCKTAIL_NAME, getCocktailName());
        json.put(DESCRIPTION, getCocktailDescription());
        json.put(RECIPE, getCocktailRecipe());
        json.put(AVATAR, getAvatarUrl());
        json.put(COCKTAIL_IMAGE, getCocktailUrl());
        json.put(LIKE, getLikeUrl());
        json.put(CATEGORY,getCategory());
        // time update
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
