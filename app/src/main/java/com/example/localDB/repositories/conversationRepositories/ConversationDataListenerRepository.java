package com.example.localDB.repositories.conversationRepositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firelib.DAL.ConversationDAL;
import com.example.localDB.DAO.conversationDAO.ConversationDataUpdatesDAO;
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

public class ConversationDataListenerRepository implements EventListener<QuerySnapshot> {
    private ConversationDataUpdatesDAO conversationDataUpdatesDAO;
    private Query query;
    private ListenerRegistration registration;

    public ConversationDataListenerRepository(Context context) {
        this.conversationDataUpdatesDAO = DBConnect.getInstance(context).conversationDataUpdatesDAO();
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
        new UpdateAsyncTask(conversationDataUpdatesDAO).execute(conversation);
    }

    private void remove(Conversation conversation) {
        new RemoveAsyncTask(conversationDataUpdatesDAO).execute(conversation);
    }

    private void insert(Conversation conversation ) {
        new InsertAsyncTask(conversationDataUpdatesDAO).execute(conversation);
    }

    public static class InsertAsyncTask extends AsyncTask<Conversation, Void, Void>{
        private ConversationDataUpdatesDAO conversationDataUpdatesDAO;

        public InsertAsyncTask(ConversationDataUpdatesDAO conversationDataUpdatesDAO) {
            this.conversationDataUpdatesDAO = conversationDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(Conversation... conversations) {
            for (Conversation conversation : conversations) {
                try {
                    conversationDataUpdatesDAO.insert(conversation);
                } catch (Exception e){

                }
            }
            return null;
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<Conversation, Void, Void>{
        private ConversationDataUpdatesDAO conversationDataUpdatesDAO;

        public RemoveAsyncTask(ConversationDataUpdatesDAO conversationDataUpdatesDAO) {
            this.conversationDataUpdatesDAO = conversationDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(Conversation... conversations) {
            for (Conversation conversation : conversations) {
                conversationDataUpdatesDAO.remove(conversation);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<Conversation, Void, Void>{
        private ConversationDataUpdatesDAO conversationDataUpdatesDAO;

        public UpdateAsyncTask(ConversationDataUpdatesDAO conversationDataUpdatesDAO) {
            this.conversationDataUpdatesDAO = conversationDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(Conversation... conversations) {
            for (Conversation conversation : conversations) {
                conversationDataUpdatesDAO.update(conversation);
            }
            return null;
        }
    }
}
