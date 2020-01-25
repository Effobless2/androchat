package com.example.localDB.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firelib.DAL.MessageDAL;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.MessageDAO;
import com.example.model.Message;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class MessageRepository implements EventListener<QuerySnapshot> {
    private MessageDAO messageDAO;
    private Query query;
    private ListenerRegistration registration;

    public MessageRepository(Context context){
        messageDAO = DBConnect.getInstance(context).messageDAO();
        this.query = MessageDAL.getAllMessage();
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    public LiveData<List<Message>> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    private void update(Message message) {
        new UpdateAsyncTask(messageDAO).execute(message);
    }

    private void remove(Message message) {
        new RemoveAsyncTask(messageDAO).execute(message);
    }

    private void insert(Message message) {
        new InsertAsyncTask(messageDAO).execute(message);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e != null){
            return;
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            Message message = documentChange.getDocument().toObject(Message.class);
            message.setId(documentChange.getDocument().getId());
            switch (documentChange.getType()){
                case ADDED:
                    insert(message);
                    break;
                case REMOVED:
                    remove(message);
                    break;
                case MODIFIED:
                    update(message);
                    break;
            }
        }
    }

    public static class InsertAsyncTask extends AsyncTask<Message, Void, Void>{
        private MessageDAO messageDAO;

        public InsertAsyncTask(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            for (Message message : messages) {
                try {
                    messageDAO.insert(message);
                } catch (Exception e){

                }
            }
            return null;
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<Message, Void, Void>{
        private MessageDAO messageDAO;

        public RemoveAsyncTask(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            for (Message message : messages) {
                messageDAO.delete(message);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Message, Void, Void>{
        private MessageDAO messageDAO;

        public UpdateAsyncTask(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            for (Message message : messages) {
                messageDAO.update(message);
            }
            return null;
        }
    }
}
