package com.example.firelib;

import com.google.firebase.firestore.FirebaseFirestore;

public class DbConnect {
    private static FirebaseFirestore database;

    public static FirebaseFirestore getDatabase(){
        if(database == null)
            database = FirebaseFirestore.getInstance();
        return database;
    }
}
