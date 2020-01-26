package com.example.localDB.DAO.UserDAO;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.model.Conversation;
import com.example.model.User;

@Dao
public interface UserDataUpdatesDAO {

    @Insert
    void insert(User user);

    @Delete
    void remove(User user);

    @Update
    void update(Conversation conversation);
}
