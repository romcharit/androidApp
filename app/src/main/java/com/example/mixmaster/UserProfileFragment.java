package com.example.mixmaster;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mixmaster.databinding.FragmentUserProfileBinding;
import com.example.mixmaster.model.User;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;
    UserRecyclerAdapter adapter;
    UserProfileViewModel viewModel;
    User user;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        String userName = UserProfileFragmentArgs.fromBundle(getArguments()).getUsername();

//        Model.getInstance().getOtherUser(userName).observe(getViewLifecycleOwner(), otherUser-> {
//            if (otherUser != null) {
//                this.user = otherUser;
//                setProfile();
//            }
//        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
    }

    public void setProfile() {

        binding.userNameUserProfile.setText(user.userName);
        binding.emailUserProfile.setText(user.email);
        binding.countryUserProfile.setText(user.country);

        // Set user avatar
        if (Objects.equals(user.avatar, " ")) {
            binding.avatarUserProfile.setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.get().load(user.avatar).into(binding.avatarUserProfile);
        }

        viewModel.getData(user.userName).observe(getViewLifecycleOwner(), (posts) -> {
            adapter = new UserRecyclerAdapter(posts, getLayoutInflater());
            binding.recyclerView.setAdapter(adapter);

            // get user posts
//            adapter.SetItemClickListener(new UserRecyclerAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int pos) {
//                    UserProfileFragmentDirections.ActionUserProfileFragmentToPostFragment action = UserProfileFragmentDirections.actionUserProfileFragmentToPostFragment(posts.get(pos).id);
//                    Navigation.findNavController(view).navigate(action);
//                }
//            });
        });
    }

}