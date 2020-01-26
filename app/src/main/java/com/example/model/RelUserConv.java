package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rel_user_conv")
public class RelUserConv {

    public static final String COLLECTION_DATABASE_NAME = "rel_users_conv";

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    private String id_conv;
    private String id_users;

    public RelUserConv() {
    }

    @Ignore
    public RelUserConv(String id, String id_conv, String id_users) {
        this.id = id;
        this.id_conv = id_conv;
        this.id_users = id_users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_conv() {
        return id_conv;
    }

    public void setId_conv(String id_conv) {
        this.id_conv = id_conv;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    @Override
    public String toString() {
        return "RelUserConv{" +
                "id='" + id + '\'' +
                ", id_conv='" + id_conv + '\'' +
                ", id_users='" + id_users + '\'' +
                '}';
    }
}
