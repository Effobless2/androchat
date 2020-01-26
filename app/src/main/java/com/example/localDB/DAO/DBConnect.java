package com.example.localDB.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.RelContacts;
import com.example.model.User;

@Database(entities = {User.class, Conversation.class, Message.class, RelContacts.class}, version = 3)
public abstract class DBConnect extends RoomDatabase {
    private static DBConnect INSTANCE;

    public synchronized static DBConnect getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, DBConnect.class, "my_database").build();
        }
        return INSTANCE;
    }

    public abstract UserDAO userDAO();

    public abstract ConversationDAO conversationDAO();

    public abstract MessageDAO messageDAO();

    public abstract RelContactsDAO relContactsDAO();
}