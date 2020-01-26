package com.example.localDB.repositories.conversationRepositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.conversationDAO.ConversationDataAccessDAO;
import com.example.model.Conversation;

import java.util.List;

public class ConversationDataAccessRepository {
    private Context context;
    private ConversationDataAccessDAO dao;

    public ConversationDataAccessRepository(Context context) {
        this.context = context;
        dao = DBConnect.getInstance(context).conversationDataAccessDAO();
    }



    public LiveData<List<Conversation>> getAll(){
        return dao.getAll();
    }
}
