package com.example.mixmaster;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.mixmaster.databinding.FragmentSearchBinding;
import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.Post;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    SeachViewModel searchViewModel;
    RecyclerView recyclerView;
    UserViewModel userViewModel;
    PostRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this).get(SeachViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Clear focus to prevent the SearchView from being focused initially
        binding.searchViewSearchFrag.clearFocus();

        // Set up RecyclerView adapter
        adapter = new PostRecyclerAdapter(userViewModel.getUser().getValue(), getLayoutInflater(),
                searchViewModel.getData(), getContext());
        recyclerView.setAdapter(adapter);

        // Set listener for SearchView text changes
        binding.searchViewSearchFrag.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission (optional)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Update RecyclerView data based on the search query
                Model.getInstance().getPostsByDrinkName(newText, (data) -> {

                    adapter.setOnImageClickListener(pos -> {
                        Post post = data.get(pos);
                        SearchFragmentDirections.ActionSearchFragmentToPostFragment action = SearchFragmentDirections.actionSearchFragmentToPostFragment(post.id);
                        Navigation.findNavController(view).navigate(action);

                    });

                    searchViewModel.setData(data);
                    adapter.setData(data);
                });
                return true; // Return true to indicate that the query has been handled
            }
        });

        // Set listener for Button click
        binding.searchBtnSearchFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drinkName = binding.searchViewSearchFrag.getQuery().toString();
                Model.getInstance().getPostsByDrinkName(drinkName, (data) -> {
                    searchViewModel.setData(data);
                    adapter.setData(data);
                });
                hideKeyboard();
            }
        });

        return view;
    }

    private void hideKeyboard() {
        // Check if no view has focus
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

