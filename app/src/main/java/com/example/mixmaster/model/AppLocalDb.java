package com.example.mixmaster.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mixmaster.MyApplication;


@Database(entities = {Post.class,User.class}, version = 130)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract UserDao userDao();
}
public class AppLocalDb{
    static public AppLocalDbRepository getAppDb() {
       return Room.databaseBuilder(MyApplication.getMyContext(),
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
    }
    private AppLocalDb(){}
}
