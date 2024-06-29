package com.example.mixmaster.model;



import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Model {
    private static final Model instance = new Model();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public static Model getInstance() {
        return instance;
    }
    private Model(){}

    public interface Listener<T>{
        void onComplete(T data);
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }

    // observable
    final public MutableLiveData<LoadingState> EventPostsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    private LiveData<List<Post>> postList;
    public LiveData<List<Post>> getAllPosts(){
        if (postList == null) {
            postList = localDb.postDao().getAll();
            refreshAllPosts();
        }
        return postList;
    }

    LiveData<Post> post;
    public LiveData<Post> getPostById(String id) {
        post = localDb.postDao().getPostById(id);
        return post;
    }

    public void refreshAllPosts(){
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Post.getPostLocalLastUpdate();
        // get all updated record from firebase since local last update
        firebaseModel.getAllPostsSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", "firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Post post : list) {
                    // insert new records into ROOM
                    localDb.postDao().insert(post);
                    if (time < post.getLastUpdated()){
                        time = post.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Post.setPostLocalLastUpdate(time);
                EventPostsListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void addPost(Post post, Listener<Void> listener){
        firebaseModel.addPost(post,(Void)->{
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }

    public void signUp(User newUser, String password, Listener<Void> listener) {
        //listener.onComplete(null);
        firebaseModel.signUp(newUser,password,listener);
        refreshAllUsers();
    }

    public void logIn(String username, String password, Listener<Boolean> listener) {
        firebaseModel.logIn(username,password,listener);
    }


    LiveData<User> otherUser;
    public LiveData<User> getOtherUser(String username) {
        otherUser=localDb.userDao().getUserByUsername(username);
        return otherUser;
    }

    LiveData<List<Post>> userPostsList;
    public LiveData<List<Post>> getUserPosts(String username){
        userPostsList = localDb.postDao().getUserPosts(username);
        return userPostsList;
    }

    private LiveData<User> user;
    public String username;
    public LiveData<User> getLoggedUser(){
        String username = firebaseModel.getLoggedUserUsername();
        if (username!=null){
            this.username = username;
        }
        if (user==null){
            user=localDb.userDao().getUserByUsername(this.username);
            refreshAllUsers();
        }
        return user;
    }

    public void refreshAllUsers(){
        Long localLastUpdated = User.getUserLocalLastUpdate();
        firebaseModel.getAllUsersSince(localLastUpdated,(users)->{
            executor.execute(()->{
                Long time = localLastUpdated;
                for (User user : users) {
                    localDb.userDao().insert(user);
                    if (user.lastUpdated > time) {
                        time=user.lastUpdated;
                    }
                }
                User.setUserLocalLastUpdate(time);
            });
        });
    }

    public boolean isLoggedIn(){
        return firebaseModel.isLoggedIn();
    }

    public void logOut() {
        firebaseModel.logOut();
        user=null;
    }

    public List<String> getCocktailCategories() {
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Category");
        categoryList.add("Alcohol Free");
        categoryList.add("Classic");
        categoryList.add("Tropical Drinks");
        categoryList.add("Mocktails");
        categoryList.add("Punches");
        categoryList.add("Garnishes");
        return categoryList;
    }

}
