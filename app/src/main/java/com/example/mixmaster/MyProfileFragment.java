package com.example.mixmaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mixmaster.databinding.FragmentMyProfileBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.User;
import com.squareup.picasso.Picasso;


public class MyProfileFragment extends Fragment {

    FragmentMyProfileBinding binding;
    UserRecyclerAdapter adapter;
    MyProfileViewModel myProfileViewModel;
    UserViewModel userViewModel;
    View view;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userViewModel.getUser().observe(getViewLifecycleOwner(),newUser->{
//            if (newUser != null) {
//                user = newUser;
//                setProfile();
//            }
            user = newUser;
            setProfile();
        });

        return view;
    }


        @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    public void setProfile(){

//        binding.userNameMyProfile.setText(user.userName);
//        binding.emailMyProfile.setText(user.email);
//        binding.countryMyProfile.setText(user.country);
//        binding.avatarMyProfile.setImageResource(R.drawable.profile_pic);

        // Edit Btn
//        binding.editButton.setOnClickListener(Navigation.createNavigateOnClickListener
//                (R.id.action_profileFragment_to_editProfileFragment));

        // LogOut Btn
        binding.logOutBtnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("yes", (dialog,which)->{
                            Model.getInstance().logOut();
                            Intent intent=new Intent(getActivity(),LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        }).setNegativeButton("No",(dialog,which)->{})
                        .create().show();
            }
        });


//        if (!user.avatar.equals(" ")) {
//            Picasso.get().load(user.avatar).into(binding.avatarMyProfile);
//        }else {
//            binding.avatarMyProfile.setImageResource(R.drawable.profile_pic);
//        }

    }

}