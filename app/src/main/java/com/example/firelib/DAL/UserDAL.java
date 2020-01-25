package com.example.firelib.DAL;

import com.example.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class UserDAL {
    public static Query getAllUser(){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME);
    }

    public static DocumentReference getUserById(String id){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .document(id);

    }

    public static Query connection(String googleId){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.GOOGLE_ID_FIREBASE_FIELD, googleId);
    }

    public static Task<DocumentReference> register(String googleId){
        User u = new User();
        u.setGoogleId(googleId);
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .add(u);
    }


}
