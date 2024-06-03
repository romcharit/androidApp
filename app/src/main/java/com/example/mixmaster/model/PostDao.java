package com.example.mixmaster.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post")
    List<Post> getAll();

    @Query("select * from Post where userName = :postUserName")
    Post getPostByUserName(String postUserName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... post);

    @Delete
    void delete(Post post);

}
