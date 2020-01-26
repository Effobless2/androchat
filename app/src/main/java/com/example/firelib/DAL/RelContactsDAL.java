package com.example.firelib.DAL;

import com.example.model.RelContacts;
import com.google.firebase.firestore.Query;

public class RelContactsDAL {

    public static Query getAllContactsOfCurrentGoogleUser(String googleId){
        return DbConnect.getDatabase()
                .collection(RelContacts.COLLECTION_DATABASE_NAME)
                .whereEqualTo("from", googleId);
    }
}
