package com.example.localDB.DAO.conversationDAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.Conversation;

@Dao
public interface ConversationDataUpdatesDAO {

    @Insert
    void insert(Conversation conversation);

    @Delete
    void remove(Conversation conversation);

    @Update
    void update(Conversation conversation);

    @Query("DELETE FROM conversations")
    void removeAll();
}
