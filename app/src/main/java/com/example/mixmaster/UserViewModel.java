package com.example.mixmaster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mixmaster.model.Model;
import com.example.mixmaster.model.User;

public class UserViewModel extends ViewModel {
    private LiveData<User> user;
    public LiveData<User> getUser() {
            this.user = Model.getInstance().getLoggedUser();
        return user;
    }
}
