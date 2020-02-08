package com.example.localDB.DAO.conversationDAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.model.Conversation;

import java.util.List;

@Dao
public interface ConversationDataAccessDAO {

    @Query("SELECT * FROM conversations WHERE id = :id")
    LiveData<List<Conversation>> getConversationById(String id);

    @Query("SELECT * FROM conversations ORDER BY last_message_date DESC")
    LiveData<List<Conversation>> getAll();
}
