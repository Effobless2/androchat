package com.example.firelib.DAL;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

class DbConnect {
    private static FirebaseFirestore database;

    public static FirebaseFirestore getDatabase(){
        if(database == null) {
            database = FirebaseFirestore.getInstance();
          /*  FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            database.setFirestoreSettings(settings);*/
        }
        return database;
    }
}
