package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class User {
    public static String COLLECTION_DATABASE_NAME = "users";
    public static String GOOGLE_ID_FIREBASE_FIELD = "googleId";
    public static String AVATAR_URL_FIREBASE_FIELD = "avatar";
    public static String USER_NAME_FIREBASE_FIELD = "userName";
    public static String USER_EMAIL_FIREBASE_FIELD = "email";


    @NonNull
    private String googleId;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String documentId;

    private String avatar;

    private String email;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "googleId='" + googleId + '\'' +
                ", idDocument='" + documentId + '\'' +
                '}';
    }
}
