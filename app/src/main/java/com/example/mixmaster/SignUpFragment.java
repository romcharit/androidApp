package com.example.mixmaster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ImageView avatarImg = view.findViewById(R.id.signup_avatar);
        ImageButton editAvatarImg = view.findViewById(R.id.signup_edit_avatar);
        EditText userNameEt = view.findViewById(R.id.signup_username);
        EditText emailEt = view.findViewById(R.id.signup_email);
        EditText passwordEt = view.findViewById(R.id.signup_password);
        EditText countryEt = view.findViewById(R.id.signup_country);
        Button submitBtn = view.findViewById(R.id.signup_submit_btn);

        submitBtn.setOnClickListener(view1->{
            // Image avatar = avatarImg.url ...
            // Password = ?
            String name = userNameEt.getText().toString();
            String email = emailEt.getText().toString();
            String country = countryEt.getText().toString();
            // ...

        });

        return view;
    }

}