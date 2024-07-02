package com.example.mixmaster;

import androidx.lifecycle.ViewModel;

import com.example.mixmaster.model.Post;

import java.util.List;

public class SeachViewModel extends ViewModel {
    private List<Post> data;
    public List<Post> getData() {
        return data;
    }
    void setData(List<Post> data){this.data=data;}
}
