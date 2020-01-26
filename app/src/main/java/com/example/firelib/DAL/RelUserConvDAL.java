package com.example.firelib.DAL;

import com.example.model.RelUserConv;
import com.google.firebase.firestore.Query;

public class RelUserConvDAL {
    public static Query getAllRelUserConv(){
        return DbConnect.getDatabase()
                .collection(RelUserConv.COLLECTION_DATABASE_NAME);
    }

    public static Query getAllRelationForUserByGoogleId(String googleId){
        return DbConnect.getDatabase()
                .collection(RelUserConv.COLLECTION_DATABASE_NAME)
                .whereEqualTo("id_users", googleId);
    }
}
