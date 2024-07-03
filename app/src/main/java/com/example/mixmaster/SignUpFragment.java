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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mixmaster.databinding.FragmentCreatePostBinding;
import com.example.mixmaster.databinding.FragmentSignUpBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    FragmentSignUpBinding binding;
    ActivityResultLauncher<String> galleryLauncher;
    ActivityResultLauncher<Void> cameraLauncher;
    Boolean isAvatarSelected = false;
    String username;
    String email;
    String password;
    String country;

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

        binding.signupCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                country = parent.getItemAtPosition(pos).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set Country spinner
        SpinnerAdapter.setCountrySpinner(getContext(),(adapter)->binding.signupCountrySpinner.setAdapter(adapter));

        /* SUBMIT BTN */
        binding.signupSubmitBtn.setOnClickListener(view1-> {

            username = binding.signupUsername.getText().toString();
            email = binding.signupEmail.getText().toString();
            password = binding.signupPassword.getText().toString();

            if (validateInput()) {
                Model.getInstance().isUsernameTaken(username, (usernameTaken)-> {
                    if (usernameTaken) {
                        makeAToast("Username is Already Taken");
                    }else {
                        Model.getInstance().isEmailTaken(email, (emailTaken)-> {
                            if (emailTaken) {
                                makeAToast("Email Is Already Taken");
                            } else {
                                if (isAvatarSelected) {
                                    binding.signupAvatar.setDrawingCacheEnabled(true);
                                    binding.signupAvatar.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) binding.signupAvatar.getDrawable()).getBitmap();
                                    String id = UUID.randomUUID().toString();

                                    Model.getInstance().uploadImage(id, bitmap, avatarUrl -> {

                                        User newUser = new User(username, avatarUrl, email, country, new ArrayList<>());
                                        Model.getInstance().signUp(newUser, password, (unused) -> {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        });
                                    });
                                }
                                else{
                                    User newUser = new User(username, "", email, country, new ArrayList<>());
                                    Model.getInstance().signUp(newUser, password, (unused) -> {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    });
                                }

                            }
                        });
                    }

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

    public void makeAToast(String text){
        new AlertDialog.Builder(getContext())
                .setTitle("Invalid Input")
                .setMessage(text)
                .setPositiveButton("Ok", (dialog,which)->{
                })
                .create().show();
    }

    public boolean validateInput()
    {

        if (Objects.equals(username, "")) {
            makeAToast("Please enter an username");
            return false;
        }
        else if (!isValidEmail(email)) {
            makeAToast("Please enter a valid email");
            return false;
        }
        else if (password.length()<6) {
            makeAToast("Password must contain at least 6 characters");
            return false;
        }
        else if (Objects.equals(country, "Country")) {
            makeAToast("Please choose a Country");
            return false;
        }

        return true;

    }

    public boolean isValidEmail(String email){
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}