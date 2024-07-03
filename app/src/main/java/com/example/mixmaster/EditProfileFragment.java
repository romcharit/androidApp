package com.example.mixmaster;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.example.mixmaster.databinding.FragmentEditPostBinding;
import com.example.mixmaster.databinding.FragmentEditProfileBinding;
import com.example.mixmaster.databinding.FragmentSignUpBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.User;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class EditProfileFragment extends Fragment {

    FragmentEditProfileBinding binding;
    ActivityResultLauncher<String> galleryLauncher;
    ActivityResultLauncher<Void> cameraLauncher;
    UserViewModel userViewModel;
    Boolean isAvatarSelected = false;
    String country;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o != null) {
                    binding.editProfileAvatar.setImageURI(o);
                    isAvatarSelected = true;
                }
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o != null) {
                    binding.editProfileAvatar.setImageBitmap(o);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.editProfileGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });
        binding.editProfileCameraBtn.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        userViewModel.getUser().observe(getViewLifecycleOwner(),(user)-> {

            // set current user avatar
            if(!user.avatar.isEmpty())
                Picasso.get().load(user.avatar).into(binding.editProfileAvatar);
            else{
                binding.editProfileAvatar.setImageResource(R.drawable.profile_pic);
            }

            binding.editProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    user.country = parent.getItemAtPosition(pos).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // set current user Country
            SpinnerAdapter.setCountrySpinner(getContext(),adapter->binding.editProfileSpinner.setAdapter(adapter));


            // confirm btn
            binding.editProfileConfirmBtn.setOnClickListener(view1-> {


                if (!user.country.equals("Country")) {
                    if (isAvatarSelected) {
                        binding.editProfileAvatar.setDrawingCacheEnabled(true);
                        binding.editProfileAvatar.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) binding.editProfileAvatar.getDrawable()).getBitmap();
                        String id = UUID.randomUUID().toString();

                        Model.getInstance().uploadImage(id, bitmap, avatarUrl -> {
                            User newUser = new User(user.userName, avatarUrl, user.email, user.country, user.likedPosts);
                            Model.getInstance().updateUser(newUser, unused -> {
                                Navigation.findNavController(view1).popBackStack();
                            });
                        });
                    } else {
                        User newUser = new User(user.userName, user.avatar, user.email, user.country, user.likedPosts);
                        Model.getInstance().updateUser(newUser, unused -> {
                            Navigation.findNavController(view1).popBackStack();
                        });
                    }
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Invalid Input")
                            .setMessage("Please enter a valid Country")
                            .setPositiveButton("Ok", (dialog,which)->{
                            })
                            .create().show();
                }
            });
        });

        return view;
    }
}