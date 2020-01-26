package com.example.localDB.repositories.messageRepositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.messagesDAO.MessageDataAccessDAO;
import com.example.model.Message;

import java.util.List;

public class MessageDataAccessRepository {
    private Context context;
    private MessageDataAccessDAO dao;

    public MessageDataAccessRepository(Context context) {
        this.context = context;
        dao = DBConnect.getInstance(context).messageDataAccessDAO();
    }

    public LiveData<List<Message>> getAll(){
        return dao.getAll();
    }

    public LiveData<List<Message>> getMessagesByConversation(String id_conv){
        return dao.getMessageByConvId(id_conv);
    }
}
