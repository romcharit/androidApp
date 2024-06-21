package com.example.mixmaster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostListFragmentViewModel extends ViewModel {
    private LiveData<List<Post>> data = Model.getInstance().getAllPosts();

    public LiveData<List<Post>> getData() { return data; }


}
