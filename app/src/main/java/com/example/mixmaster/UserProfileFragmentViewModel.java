package com.example.mixmaster;

import androidx.lifecycle.ViewModel;

import com.example.mixmaster.model.Post;

import java.util.List;

public class UserProfileFragmentViewModel extends ViewModel {
    private List<Post> data;
    public List<Post> getData() {
        return data;
    }
    public void setData(List<Post> data) {
        this.data = data;
    }
}
