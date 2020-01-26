package com.example.localDB.repositories.userRepositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.UserDAO.UserDataUpdatesDAO;
import com.example.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class UserDataListenerRepository implements EventListener<QuerySnapshot> {
    private Context context;
    private UserDataUpdatesDAO userDataAccessDAO;
    private Query query;
    private ListenerRegistration registration;

    public UserDataListenerRepository(Context context, Query query) {
        this.context = context;
        DBConnect database = DBConnect.getInstance(context);
        userDataAccessDAO = database.userDataUpdatesDAO();
        this.query = query;
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

    public void insert(User user) {
        new InsertAsyncTask(userDataAccessDAO).execute(user);
    }

    public void remove(User user){ new  RemoveAsyncTask(userDataAccessDAO).execute(user); }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                        @Nullable FirebaseFirestoreException e) {
        if(e != null){
            return; //TODO : Handle Exception
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            User u = documentChange.getDocument().toObject(User.class);
            u.setDocumentId(documentChange.getDocument().getId());
            switch (documentChange.getType()){
                case ADDED:
                    this.insert(u);
                    break;
                case REMOVED:
                    remove(u);
                    break;
            }
        }
    }

    public static class RemoveAsyncTask extends AsyncTask<User, Void, Void>{
        private UserDataUpdatesDAO userDataUpdatesDAO;

        public RemoveAsyncTask(UserDataUpdatesDAO userDataUpdatesDAO) {
            this.userDataUpdatesDAO = userDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            for (User user : users) {
                userDataUpdatesDAO.remove(user);
            }
            return null;
        }
    }

    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDataUpdatesDAO userDataUpdatesDAO;

        public InsertAsyncTask(UserDataUpdatesDAO userDataUpdatesDAO) {
            this.userDataUpdatesDAO = userDataUpdatesDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            for (User user : users) {
                try{
                    userDataUpdatesDAO.insert(user);
                } catch (Exception e){

                }
            }
            return null;
        }
    }
}
