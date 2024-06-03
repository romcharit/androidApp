package com.example.mixmaster.model;



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
    Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    public static Model getInstance() {
        return instance;
    }
    private Model(){
    }

    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }
    public void getAllPosts(GetAllPostsListener callback ){
        executor.execute(()->{
            List<Post> data = localDb.postDao().getAll();
            mainHandler.post(()->{
                callback.onComplete(data);
            });
        });
    }

    public interface AddPostListener{
        void onComplete();
    }
    public void addPost(Post post, AddPostListener listener){
        executor.execute(()->{
            localDb.postDao().insertAll(post);
            mainHandler.post(()->{
               listener.onComplete();
            });
        });
    }


}
