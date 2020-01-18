package com.example.localDB.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class DBConnect extends RoomDatabase {
    private static DBConnect INSTANCE;

    public synchronized static DBConnect getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, DBConnect.class, "my_database").build();
        }
        return INSTANCE;
    }

    public abstract UserDAO userDAO();
}