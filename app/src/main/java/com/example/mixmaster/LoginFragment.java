package com.example.mixmaster;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mixmaster.databinding.FragmentLoginBinding;
import com.example.mixmaster.model.Model;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.loginUsername.getText().toString();
                String password = binding.loginPassword.getText().toString();
                Model.getInstance().logIn(username, password, (isSuccessful) -> {
                    if (isSuccessful) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Username or Password are Incorrect!"
                                ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}