package com.example.localDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.Conversation;

import java.util.List;

@Dao
public interface ConversationDAO {

    @Insert
    void insert(Conversation conversation);

    @Query("SELECT * FROM conversations WHERE id = :id")
    LiveData<List<Conversation>> getConversationById(String id);

    @Query("SELECT * FROM conversations")
    LiveData<List<Conversation>> getAllConversations();

    @Query("DELETE FROM conversations WHERE id = :id")
    void remove(String id);

    @Update
    void update(Conversation conversation);
}
