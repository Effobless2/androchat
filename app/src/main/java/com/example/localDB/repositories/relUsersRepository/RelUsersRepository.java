package com.example.localDB.repositories.relUsersRepository;

import android.content.Context;

import com.example.firelib.DAL.RelContactsDAL;
import com.example.firelib.DAL.UserDAL;
import com.example.localDB.repositories.userRepositories.UserDataListenerRepository;
import com.example.model.RelContacts;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class RelUsersRepository implements EventListener<QuerySnapshot> {
    private Context context;
    private Query query;
    private ListenerRegistration registration;

    public RelUsersRepository(Context context, Query query) {
        this.context = context;
        this.query = query;
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
            return; //TODO : Handle Exception
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            RelContacts relContact = documentChange.getDocument().toObject(RelContacts.class);
            relContact.setId(documentChange.getDocument().getId());
            new UserDataListenerRepository(context, UserDAL.getUserByGoogleId(relContact.getTo()));
        }
    }
}
