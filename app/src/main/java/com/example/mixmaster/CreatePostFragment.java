package com.example.mixmaster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;

import java.util.UUID;


public class CreatePostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        TextView titleTv = view.findViewById(R.id.create_post_title);
        ImageView cocktailIv = view.findViewById(R.id.create_post_cocktail_img);
        ImageButton editImgBtn = view.findViewById(R.id.create_post_edit_img);
        EditText cocktailNameEt = view.findViewById(R.id.create_post_cocktail_name);
        EditText cocktailDescriptionEt = view.findViewById(R.id.create_post_cocktail_description);
        EditText cocktailRecipeEt = view.findViewById(R.id.create_post_cocktail_recipe);
        Button confirmBtn = view.findViewById(R.id.create_post_confirm_btn);

        confirmBtn.setOnClickListener(view1->{
            // Image cocktailImg = editImgBtn.url ...
            String cocktailName = cocktailNameEt.getText().toString();
            String cocktailDescription = cocktailDescriptionEt.getText().toString();
            String cocktailRecipe = cocktailRecipeEt.getText().toString();
            String id = UUID.randomUUID().toString();
            Post post = new Post(id," ", cocktailName,cocktailDescription,cocktailRecipe, " ", " ", " ");


            Model.getInstance().addPost(post,()->{
                // go back to PostListFragment... onResume will update the data
                Navigation.findNavController(view1).popBackStack();
            });
        });

        return view;
    }
}