package com.example.mixmaster;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.mixmaster.databinding.FragmentCreatePostBinding;
import com.example.mixmaster.databinding.FragmentEditPostBinding;
import com.example.mixmaster.model.Model;
import com.squareup.picasso.Picasso;


public class EditPostFragment extends Fragment {

    FragmentEditPostBinding binding;
    ImageViewModel viewModel;
    boolean isImageSelected;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    String cocktailCategory;
    String postId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview()
                ,new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o != null) {
                    viewModel.setBitmap(o);
                    binding.cocktailImgEditPost.setImageBitmap(viewModel.getBitmap());
                    isImageSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent()
                ,new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o != null) {
                    viewModel.setUrl(o);
                    binding.cocktailImgEditPost.setImageURI(viewModel.getUrl());
                    isImageSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditPostBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        viewModel=new ViewModelProvider(this).get(ImageViewModel.class);
        if (viewModel.getBitmap()!=null)
            binding.cocktailImgEditPost.setImageBitmap(viewModel.getBitmap());
        if (viewModel.getUrl()!=null)
            binding.cocktailImgEditPost.setImageURI(viewModel.getUrl());

        Spinner cocktailCategorySpinner = binding.categorySpinnerEditPost;
        cocktailCategorySpinner.setAdapter(SpinnerAdapter.setCocktailCategoriesSpinner(getContext()));
        cocktailCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cocktailCategory = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.cameraBtnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLauncher.launch(null);
            }
        });

        binding.editImgBtnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        Model.getInstance().getPostById(postId).observe(getViewLifecycleOwner(),(post)-> {

            Picasso.get().load(Uri.parse(post.cocktailUrl)).into(binding.cocktailImgEditPost);
            binding.cocktailNameEditPost.setText(post.cocktailName);
            binding.cocktailDescriptionEditPost.setText(post.cocktailDescription);
            binding.cocktailRecipeEditPost.setText(post.cocktailRecipe);

            binding.confirmBtnEditPost.setOnClickListener(view1->{

                post.cocktailName = binding.cocktailNameEditPost.getText().toString();
                post.cocktailDescription = binding.cocktailDescriptionEditPost.getText().toString();
                post.cocktailRecipe = binding.cocktailRecipeEditPost.getText().toString();
                post.category = cocktailCategory;

                if (isImageSelected)
                {
                    binding.cocktailImgEditPost.setDrawingCacheEnabled(true);
                    binding.cocktailImgEditPost.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.cocktailImgEditPost.getDrawable()).getBitmap();
                    Model.getInstance().uploadImage(postId, bitmap, (url) -> {
                        post.setCocktailUrl(url);
                        Model.getInstance().updatePost(post, (unused) -> {
                            Navigation.findNavController(view1).popBackStack(R.id.postFragment, false);
                        });
                    });
                }

                else 
                {
                    Model.getInstance().updatePost(post, (unused) -> {
                        Navigation.findNavController(view1).popBackStack(R.id.postFragment, false);
                    });
                }

            });
        });

        return view;
    }
}