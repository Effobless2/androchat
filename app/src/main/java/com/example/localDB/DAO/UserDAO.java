package com.example.localDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM contacts")
    LiveData<List<User>> getAllContacts();

    @Query("DELETE FROM contacts WHERE documentId = :documentId")
    void remove(String documentId);
}
