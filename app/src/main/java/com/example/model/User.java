package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class User {
    public static String COLLECTION_DATABASE_NAME = "users";
    public static String GOOGLE_ID_FIREBASE_FIELD = "googleId";


    @NonNull
    private String googleId;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String documentId;

    @Ignore
    public User() {
    }

    public User(String documentId, String googleId) {
        this.documentId = documentId;
        this.googleId = googleId;
    }

    @NonNull
    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(@NonNull String googleId) {
        this.googleId = googleId;
    }

    @NonNull
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(@NonNull String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "User{" +
                "googleId='" + googleId + '\'' +
                ", idDocument='" + documentId + '\'' +
                '}';
    }
}
