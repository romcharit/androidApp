package com.example.mixmaster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mixmaster.databinding.FragmentPostBinding;
import com.example.mixmaster.model.Model;
import com.squareup.picasso.Picasso;


public class PostFragment extends Fragment {

    String postId;
    FragmentPostBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        // gets the post id by Argument from PostListFragment Navigation
        postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();

        Model.getInstance().getPostById(postId).observe(getViewLifecycleOwner(), (post)->{
            binding.postUsername.setText(post.userName);
            binding.postCocktailName.setText(post.cocktailName);
            Picasso.get().load(post.cocktailUrl).into(binding.postCocktailImg);
            binding.postDescription.setText(post.cocktailDescription);
            binding.postRecipe.setText(post.cocktailRecipe);
        });

        return view;
    }

}