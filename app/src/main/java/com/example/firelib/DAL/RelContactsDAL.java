package com.example.firelib.DAL;

import com.example.model.RelContacts;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class RelContactsDAL {

    public static Query getAllContactsOfCurrentGoogleUser(String googleId){
        return DbConnect.getDatabase()
                .collection(RelContacts.COLLECTION_DATABASE_NAME)
                .whereEqualTo(RelContacts.FROM_DATABASE_NAME, googleId);
    }

    public static Task<DocumentReference> createRelation(RelContacts relContacts){
        return DbConnect.getDatabase()
                .collection(RelContacts.COLLECTION_DATABASE_NAME)
                .add(relContacts);
    }
}
