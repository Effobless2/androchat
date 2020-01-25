package com.example.localDB.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firelib.DAL.ConversationDAL;
import com.example.localDB.DAO.ConversationDAO;
import com.example.localDB.DAO.DBConnect;
import com.example.model.Conversation;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class ConversationRepository implements EventListener<QuerySnapshot> {
    private ConversationDAO conversationDAO;
    private Query query;
    private ListenerRegistration registration;

    public ConversationRepository(Context context) {
        this.conversationDAO = DBConnect.getInstance(context).conversationDAO();
        this.query = ConversationDAL.getAllConversation();
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e != null){
            return;
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            Conversation cur = documentChange.getDocument().toObject(Conversation.class);
            cur.setId(documentChange.getDocument().getId());
            switch (documentChange.getType()){
                case ADDED:
                    this.insert(cur);
                    break;
                case REMOVED:
                    this.remove(cur);
                    break;
                case MODIFIED:
                    this.update(cur);
            }
        }
    }

    private void update(Conversation conversation) {
        new UpdateAsyncTask(conversationDAO).execute(conversation);
    }

    private void remove(Conversation conversation) {
        new RemoveAsyncTask(conversationDAO).execute(conversation);
    }

    private void insert(Conversation conversation ) {
        new InsertAsyncTask(conversationDAO).execute(conversation);
    }

    public LiveData<List<Conversation>> getAllConversations(){
        return conversationDAO.getAllConversations();
    }

    public static class InsertAsyncTask extends AsyncTask<Conversation, Void, Void>{
        private ConversationDAO conversationDAO;

        public InsertAsyncTask(ConversationDAO conversationDAO) {
            this.conversationDAO = conversationDAO;
        }

        @Override
        protected Void doInBackground(Conversation... conversations) {
            for (Conversation conversation : conversations) {
                conversationDAO.insert(conversation);
            }
            return null;
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<Conversation, Void, Void>{
        private ConversationDAO conversationDAO;

        public RemoveAsyncTask(ConversationDAO conversationDAO) {
            this.conversationDAO = conversationDAO;
        }

        @Override
        protected Void doInBackground(Conversation... conversations) {
            for (Conversation conversation : conversations) {
                conversationDAO.remove(conversation.getId());
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Conversation, Void, Void>{
        private ConversationDAO conversationDAO;

        public UpdateAsyncTask(ConversationDAO conversationDAO) {
            this.conversationDAO = conversationDAO;
        }

        @Override
        protected Void doInBackground(Conversation... conversations) {
            for (Conversation conversation : conversations) {
                conversationDAO.update(conversation);
            }
            return null;
        }
    }
}
