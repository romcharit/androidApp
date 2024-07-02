package com.example.mixmaster;

import androidx.lifecycle.ViewModel;

import com.example.mixmaster.model.Post;

import java.util.List;

public class LikedPostsViewModel extends ViewModel {
    private List<Post> likedPosts;
    public List<Post> getLikedPosts(){
        return likedPosts;
    }
    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }
}