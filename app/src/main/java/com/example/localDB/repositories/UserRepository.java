package com.example.localDB.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firelib.DAL.UserDAL;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.UserDAO;
import com.example.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class UserRepository implements EventListener<QuerySnapshot> {
    private UserDAO userDAO;
    private Query query;
    private ListenerRegistration registration;

    public UserRepository(Context context) {
        DBConnect database = DBConnect.getInstance(context);
        userDAO = database.userDAO();
        this.query = UserDAL.getAllUser();
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    public void stopQuery(){
        if(registration != null){
            registration.remove();
            registration = null;
        }
    }
    public LiveData<List<User>> getAllContacts() {
        return userDAO.getAllContacts();
    }

    public void insert(User user) {
        new InsertAsyncTask(userDAO).execute(user);
    }

    public void remove(String userId){ new  RemoveAsyncTask(userDAO).execute(userId); }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                        @Nullable FirebaseFirestoreException e) {
        if(e != null){
            return; //TODO : Handle Exception
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            switch (documentChange.getType()){
                case ADDED:
                    User u = new User();
                    u.setGoogleId(documentChange.getDocument().toObject(User.class).getGoogleId());
                    u.setDocumentId(documentChange.getDocument().getId());
                    this.insert(u);
                    break;
                case REMOVED:
                    remove(documentChange.getDocument().getId().toString());
                    break;
            }
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<String, Void, Void>{
        private UserDAO userDAO;

        public RemoveAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(String... userIds) {
            for (String userId : userIds) {
                userDAO.remove(userId);
            }
            return null;
        }
    }

    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        public InsertAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            for (User user : users) {
                try{
                    userDAO.insert(user);
                } catch (Exception e){

                }
            }
            return null;
        }
    }
}
