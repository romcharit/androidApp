package com.example.mixmaster.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from Post")
    LiveData<List<Post>> getAll();

    @Query("select * from Post where userName = :username")
    LiveData<List<Post>> getUserPosts(String username);

    @Query("select * from Post where userName = :postUserName")
    Post getPostByUserName(String postUserName);

    @Query("select * from Post where id=:id")
    LiveData<Post> getPostById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Delete
    void delete(Post post);

}
