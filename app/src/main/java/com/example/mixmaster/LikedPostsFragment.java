package com.example.mixmaster;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mixmaster.databinding.FragmentLikedPostsBinding;
import com.example.mixmaster.model.Model;

public class LikedPostsFragment extends Fragment {

    FragmentLikedPostsBinding binding;
    LikedPostsViewModel viewModel;
    UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLikedPostsBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(LikedPostsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userViewModel.getUser().observe(getViewLifecycleOwner(),(user)->{
            PostRecyclerAdapter adapter = new PostRecyclerAdapter(user, getLayoutInflater(), viewModel.getLikedPosts(), getContext());
            binding.recyclerView.setAdapter(adapter);

            Model.getInstance().getLikedPosts(user.likedPosts,(posts)->{
                viewModel.setLikedPosts(posts);
                adapter.setData(posts);
            });
        });

        return view;
    }

}