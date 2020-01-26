package com.example.localDB.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firelib.DAL.RelContactsDAL;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.RelContactsDAO;
import com.example.model.RelContacts;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class ContactsRepository implements EventListener<QuerySnapshot> {
    private RelContactsDAO contactsDAO;
    private Query query;
    private ListenerRegistration registration;

    public ContactsRepository(Context context) {
        contactsDAO = DBConnect.getInstance(context).relContactsDAO();
        this.query = RelContactsDAL.getAllContacts();
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    private void insert(RelContacts relContact) {
        new InsertAsyncTask(contactsDAO).execute(relContact);
    }

    private void remove(RelContacts relContact) {
        new RemoveAsyncTask(contactsDAO).execute(relContact);
    }

    private void update(RelContacts relContacts) {

    }

    public LiveData<List<RelContacts>> getAllRelContacts(){
        return contactsDAO.getAllRelContacts();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e != null){
            return; //TODO : Handle Exception
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            RelContacts relContact = documentChange.getDocument().toObject(RelContacts.class);
            relContact.setId(documentChange.getDocument().getId());
            switch (documentChange.getType()){
                case ADDED:
                    insert(relContact);
                    break;
                case REMOVED:
                    remove(relContact);
                    break;
                case MODIFIED:
                    update(relContact);
                    break;
            }
        }
    }

    public static class InsertAsyncTask extends AsyncTask<RelContacts, Void, Void> {
        private RelContactsDAO relContactsDAO;

        public InsertAsyncTask(RelContactsDAO relContactsDAO) {
            this.relContactsDAO = relContactsDAO;
        }

        @Override
        protected Void doInBackground(RelContacts... relContacts) {
            for (RelContacts relContact : relContacts) {
                try {
                    relContactsDAO.insert(relContact);
                } catch (Exception e){

                }
            }
            return null;
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<RelContacts, Void, Void>{
        private RelContactsDAO relContactsDAO;

        public RemoveAsyncTask(RelContactsDAO relContactsDAO) {
            this.relContactsDAO = relContactsDAO;
        }

        @Override
        protected Void doInBackground(RelContacts... relContacts) {
            for (RelContacts relContact : relContacts) {
                relContactsDAO.delete(relContact);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<RelContacts, Void, Void>{
        private RelContactsDAO relContactsDAO;

        public UpdateAsyncTask(RelContactsDAO relContactsDAO) {
            this.relContactsDAO = relContactsDAO;
        }

        @Override
        protected Void doInBackground(RelContacts... relContacts) {
            for (RelContacts relContact : relContacts) {
                relContactsDAO.update(relContact);
            }
            return null;
        }
    }
}
