package com.example.localDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.RelContacts;

import java.util.List;

@Dao
public interface RelContactsDAO {
    @Insert
    void insert(RelContacts relContacts);

    @Query("SELECT * FROM rel_contacts")
    LiveData<List<RelContacts>> getAllRelContacts();

    @Query("SELECT * FROM rel_contacts WHERE `from` = :userId")
    LiveData<List<RelContacts>> getRelForUserId(String userId);

    @Update
    void update(RelContacts relContacts);

    @Delete
    void delete(RelContacts relContacts);
}
