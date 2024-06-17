package com.example.mixmaster.model;



import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

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
    private Model(){
    }

    public interface Listener<T>{
        void onComplete(T data);
    }
    public void getAllPosts(Listener<List<Post>> callback){
        firebaseModel.getAllPosts(callback);
//        executor.execute(()->{
//            List<Post> data = localDb.postDao().getAll();
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->{
//                callback.onComplete(data);
//            });
//        });
    }

    public void addPost(Post post, Listener<Void> listener){
        firebaseModel.addPost(post,listener);
//        executor.execute(()->{
//            localDb.postDao().insert(post);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainHandler.post(()->{
//               listener.onComplete();
//            });
//        });
    }

    public void uploadImage (String name, Bitmap bitmap, Listener<String> listener)
    {
        firebaseModel.uploadImage(name,bitmap,listener);
    }



}
