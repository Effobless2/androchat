package com.example.localDB.repositories.messageRepositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.firelib.DAL.MessageDAL;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.messagesDAO.MessageDataUpdatesDAO;
import com.example.model.Message;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MessageDataListenerRepository implements EventListener<QuerySnapshot> {
    private MessageDataUpdatesDAO messageDataUpdatesDAO;
    private Query query;
    private ListenerRegistration registration;

    public MessageDataListenerRepository(Context context, Query query){
        messageDataUpdatesDAO = DBConnect.getInstance(context).messageDataUpdatesDAO();
        this.query = query;
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    private void update(Message message) {
        new UpdateAsyncTask(messageDataUpdatesDAO).execute(message);
    }

    private void remove(Message message) {
        new RemoveAsyncTask(messageDataUpdatesDAO).execute(message);
    }

    private void insert(Message message) {
        new InsertAsyncTask(messageDataUpdatesDAO).execute(message);
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
                    Log.i("SERVICE_LOG", "message added" + message.getContent());
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
        private MessageDataUpdatesDAO messageDataUpdatesDAO;

        public InsertAsyncTask(MessageDataUpdatesDAO messageDataUpdatesDAO) {
            this.messageDataUpdatesDAO = messageDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            for (Message message : messages) {
                try {
                    messageDataUpdatesDAO.insert(message);
                } catch (Exception e){

                }
            }
            return null;
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<Message, Void, Void>{
        private MessageDataUpdatesDAO messageDataUpdatesDAO;

        public RemoveAsyncTask(MessageDataUpdatesDAO messageDataUpdatesDAO) {
            this.messageDataUpdatesDAO = messageDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            for (Message message : messages) {
                messageDataUpdatesDAO.delete(message);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Message, Void, Void>{
        private MessageDataUpdatesDAO messageDataUpdatesDAO;

        public UpdateAsyncTask(MessageDataUpdatesDAO messageDataUpdatesDAO) {
            this.messageDataUpdatesDAO = messageDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            for (Message message : messages) {
                messageDataUpdatesDAO.update(message);
            }
            return null;
        }
    }
}
