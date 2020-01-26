package com.example.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rel_contacts")
public class RelContacts {
    public static final String COLLECTION_DATABASE_NAME = "rel_users";

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    private String from;
    private String to;

    public RelContacts() {
    }

    @Ignore
    public RelContacts(String id, String from, String to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "RelContacts{" +
                "documentId='" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
