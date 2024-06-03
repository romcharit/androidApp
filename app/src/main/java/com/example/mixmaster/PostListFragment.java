package com.example.mixmaster;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mixmaster.databinding.FragmentPostListBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;

import java.util.LinkedList;
import java.util.List;


public class PostListFragment extends Fragment {

    List<Post> data = new LinkedList<>();
    PostRecyclerAdapter adapter;
    FragmentPostListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPostListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Model.getInstance().getAllPosts((postList)->{
            data = postList;
            adapter.setData(data);
        });
        RecyclerView list = binding.recyclerList;
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostRecyclerAdapter(getLayoutInflater(),data);
        list.setAdapter(adapter);

        return view;
    }

}