package com.example.mixmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mixmaster.databinding.FragmentCreatePostBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;
import com.example.mixmaster.model.User;

import java.util.Objects;
import java.util.UUID;


public class CreatePostFragment extends Fragment {
    FragmentCreatePostBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    Boolean isCocktailImageSelected = false;
    UserViewModel userViewModel;
    User user;
    String cocktailCategory;
    String cocktailName;
    String cocktailDescription;
    String cocktailRecipe;

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

        // onAttach
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User newUser) {
                user = newUser;
            }
        });

        // Category Spinner
        Spinner cocktailCategorySpinner = binding.categorySpinnerCreatePost;
        cocktailCategorySpinner.setAdapter(SpinnerAdapter.setCocktailCategoriesSpinner(getContext()));
        cocktailCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cocktailCategory = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.confirmBtnCreatePost.setOnClickListener(view1->
        {
            cocktailName = binding.cocktailNameCreatePost.getText().toString();
            cocktailDescription = binding.cocktailDescriptionCreatePost.getText().toString();
            cocktailRecipe = binding.cocktailRecipeCreatePost.getText().toString();

            // Cocktail Image
            if (validateInput()) {
                binding.cocktailImgCreatePost.setDrawingCacheEnabled(true);
                binding.cocktailImgCreatePost.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.cocktailImgCreatePost.getDrawable()).getBitmap();
                String id = UUID.randomUUID().toString();
                Post post = new Post(id, user.userName, cocktailName, cocktailDescription, cocktailRecipe, user.avatar, "", " ");

                Model.getInstance().uploadImage(id, bitmap, url -> {
                    if (url != null) {
                        post.setCocktailUrl(url);
                    }
                    Model.getInstance().addPost(post, (unused) -> {
                        // go back to PostListFragment... onResume will update the data
                        Navigation.findNavController(view1).popBackStack(R.id.postListFragment, false);
                    });
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    public boolean validateInput() {
        if (!isCocktailImageSelected) {
            makeAToast("Please choose a picture for your drink");
            return false;
        }
        else if (Objects.equals(cocktailName, "")) {
            makeAToast("Please write a Name for your drink");
            return false;
        }
        else if (Objects.equals(cocktailDescription, "")) {
            makeAToast("Please write a description for your drink");
            return false;
        }
        else if (Objects.equals(cocktailRecipe, "")) {
            makeAToast("Please write a recipe for your drink");
            return false;
        }
        else if (Objects.equals(cocktailCategory, "Category")) {
            makeAToast("Please choose drink category");
            return false;
        }
        return true;
    }

    public void makeAToast(String text) {
        new AlertDialog.Builder(getContext())
                .setTitle("Invalid Input")
                .setMessage(text)
                .setPositiveButton("Ok", (dialog,which)->{
                })
                .create().show();
    }
}