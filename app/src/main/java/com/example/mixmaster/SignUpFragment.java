package com.example.mixmaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mixmaster.databinding.FragmentCreatePostBinding;
import com.example.mixmaster.databinding.FragmentSignUpBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.User;

import java.util.ArrayList;
import java.util.UUID;


public class SignUpFragment extends Fragment {

    FragmentSignUpBinding binding;
    ActivityResultLauncher<String> galleryLauncher;
    ActivityResultLauncher<Void> cameraLauncher;
    Boolean isAvatarSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o != null){
                    binding.signupAvatar.setImageURI(o);
                    isAvatarSelected = true;
                }
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o != null) {
                    binding.signupAvatar.setImageBitmap(o);
                    isAvatarSelected = true;
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        /* SUBMIT BTN */
        binding.signupSubmitBtn.setOnClickListener(view1->{
            String userName = binding.signupUsername.getText().toString();
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String country = binding.signupCountry.getText().toString();

            if (isAvatarSelected) {
                binding.signupAvatar.setDrawingCacheEnabled(true);
                binding.signupAvatar.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.signupAvatar.getDrawable()).getBitmap();
                String id = UUID.randomUUID().toString();

                Model.getInstance().uploadImage(id, bitmap, avatarUrl -> {

                    User newUser = new User(userName, avatarUrl, email, country, new ArrayList<>());
                    Model.getInstance().signUp(newUser, password, (unused) -> {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    });
                });
            }
        });

        binding.signupEditAvatar.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        binding.signupCameraBtn.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        return view;
    }

}