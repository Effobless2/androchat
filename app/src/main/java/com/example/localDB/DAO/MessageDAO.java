package com.example.localDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.Message;

import java.util.List;

@Dao
public interface MessageDAO {

    @Insert
    void insert(Message message);

    @Query("SELECT * FROM messages")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM messages WHERE id_conv = :id_conv ORDER BY date ASC")
    LiveData<List<Message>> getMessageByConvId(String id_conv);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);
}
