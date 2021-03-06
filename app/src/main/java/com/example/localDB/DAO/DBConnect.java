package com.example.localDB.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.localDB.DAO.UserDAO.UserDataAccessDAO;
import com.example.localDB.DAO.UserDAO.UserDataUpdatesDAO;
import com.example.localDB.DAO.conversationDAO.ConversationDataAccessDAO;
import com.example.localDB.DAO.conversationDAO.ConversationDataUpdatesDAO;
import com.example.localDB.DAO.messagesDAO.MessageDataAccessDAO;
import com.example.localDB.DAO.messagesDAO.MessageDataUpdatesDAO;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.RelContacts;
import com.example.model.RelUserConv;
import com.example.model.User;

@Database(entities = {User.class, Conversation.class, Message.class, RelContacts.class, RelUserConv.class}, version = 3)
public abstract class DBConnect extends RoomDatabase {
    private static DBConnect INSTANCE;

    public synchronized static DBConnect getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, DBConnect.class, "my_database").build();
        }
        return INSTANCE;
    }

    public abstract UserDataAccessDAO userDataAccessDAO();
    public abstract UserDataUpdatesDAO userDataUpdatesDAO();

    public abstract ConversationDataAccessDAO conversationDataAccessDAO();
    public abstract ConversationDataUpdatesDAO conversationDataUpdatesDAO();

    public abstract MessageDataUpdatesDAO messageDataUpdatesDAO();
    public abstract MessageDataAccessDAO messageDataAccessDAO();
}