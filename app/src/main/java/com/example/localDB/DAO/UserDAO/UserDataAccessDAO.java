package com.example.localDB.DAO.UserDAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.Conversation;
import com.example.model.User;

import java.util.List;

@Dao
public interface UserDataAccessDAO {

    @Query("SELECT * FROM contacts")
    LiveData<List<User>> getAll();
}
