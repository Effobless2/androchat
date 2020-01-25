package com.example.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rel_contacts")
public class RelContacts {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String documentId;

    private String from;
    private String to;

    public RelContacts() {
    }

    public RelContacts(String documentId, String from, String to) {
        this.documentId = documentId;
        this.from = from;
        this.to = to;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
