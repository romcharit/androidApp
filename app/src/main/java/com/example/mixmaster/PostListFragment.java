package com.example.mixmaster;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mixmaster.databinding.FragmentPostListBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;

import java.util.LinkedList;
import java.util.List;


public class PostListFragment extends Fragment {

    List<Post> data = new LinkedList<>();
    PostRecyclerAdapter adapter;
    FragmentPostListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView list = binding.recyclerList;
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        PostRecyclerAdapter adapter = new PostRecyclerAdapter(getLayoutInflater(),data);
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new PostRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Post post = data.get(pos);
                PostListFragmentDirections.ActionPostListFragmentToPostFragment action = PostListFragmentDirections.actionPostListFragmentToPostFragment(post.userName);
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//       reloadData();
    }

    void reloadData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllPosts((postList)->{
            data = postList;
            adapter.setData(data);
            binding.progressBar.setVisibility(View.GONE);
        });
    }
}