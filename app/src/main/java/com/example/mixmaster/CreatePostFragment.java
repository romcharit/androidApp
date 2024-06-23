package com.example.mixmaster;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.example.mixmaster.databinding.FragmentCreatePostBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;

import java.util.UUID;


public class CreatePostFragment extends Fragment {
    FragmentCreatePostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    Boolean isCocktailImageSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.cocktailImgCreatePost.setImageBitmap(result);
                    isCocktailImageSelected = true;
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatePostBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        TextView titleTv = view.findViewById(R.id.title_create_post);
        ImageView cocktailImg = view.findViewById(R.id.cocktail_img_create_post);
        ImageButton editImgBtn = view.findViewById(R.id.edit_img_btn_create_post);
        ImageButton cameraBtn = view.findViewById(R.id.camera_btn_create_post);
        EditText cocktailNameEt = view.findViewById(R.id.cocktail_name_create_post);
        EditText cocktailDescriptionEt = view.findViewById(R.id.cocktail_description_create_post);
        EditText cocktailRecipeEt = view.findViewById(R.id.cocktail_recipe_create_post);
        Button confirmBtn = view.findViewById(R.id.confirm_btn_create_post);

        binding.confirmBtnCreatePost.setOnClickListener(view1->{
            // Image cocktailImg = editImgBtn.url ...
            String cocktailName = cocktailNameEt.getText().toString();
            String cocktailDescription = cocktailDescriptionEt.getText().toString();
            String cocktailRecipe = cocktailRecipeEt.getText().toString();
            String id = UUID.randomUUID().toString();
            Post post = new Post(id," ", cocktailName,cocktailDescription,cocktailRecipe, " ", " ", " ");

            // cocktailImg
            if (isCocktailImageSelected)
            {
                binding.cocktailImgCreatePost.setDrawingCacheEnabled(true);
                binding.cocktailImgCreatePost.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.cocktailImgCreatePost.getDrawable()).getBitmap();
                Model.getInstance().uploadImage(id,bitmap, url->{
                    if (url != null) {
                        post.setCocktailUrl(url);
                    }
                    Model.getInstance().addPost(post, (unused) -> {
                        // go back to PostListFragment... onResume will update the data
                        Navigation.findNavController(view1).popBackStack();
                    });
                });
            }else {
                Model.getInstance().addPost(post, (unused) -> {
                    // go back to PostListFragment... onResume will update the data
                    Navigation.findNavController(view1).popBackStack();
                });
            }
        });

        binding.cameraBtnCreatePost.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.editImgBtnCreatePost.setOnClickListener(view1->{

        });

        return view;
    }
}