package com.example.localDB.DAO.messagesDAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.model.Message;

import java.util.List;

@Dao
public interface MessageDataAccessDAO {

    @Query("SELECT * FROM messages")
    LiveData<List<Message>> getAll();

    @Query("SELECT * FROM messages WHERE id_conv = :id_conv ORDER BY date ASC")
    LiveData<List<Message>> getMessageByConvId(String id_conv);

}
