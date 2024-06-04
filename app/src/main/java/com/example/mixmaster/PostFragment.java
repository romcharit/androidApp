package com.example.mixmaster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PostFragment extends Fragment {

    TextView userNameTv;
    String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // gets the userName by Argument from PostListFragment Navigation
        userName = PostFragmentArgs.fromBundle(getArguments()).getTitleUserName();

        TextView userNameTv = view.findViewById(R.id.post_user_name);
        if (userName != null){
            userNameTv.setText(userName);
        }

        return view;
    }

    public void setUserName(String userName){
        this.userName = userName;
        if (userName != null){
            userNameTv.setText(userName);
        }
    }
}