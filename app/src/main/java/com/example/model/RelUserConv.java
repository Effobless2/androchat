package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rel_user_conv")
public class RelUserConv {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String documentId;

    private String convId;
    private String userId;

    public RelUserConv() {
    }

    public RelUserConv(String documentId, String convId, String userId) {
        this.documentId = documentId;
        this.convId = convId;
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
