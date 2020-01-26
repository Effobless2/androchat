package com.example.localDB.DAO.messagesDAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.model.Message;

@Dao
public interface MessageDataUpdatesDAO {

    @Insert
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);
}
