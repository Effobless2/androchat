package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.model.converters.DateToLongConverter;

import java.util.Date;

@Entity(tableName = "messages")
@TypeConverters(DateToLongConverter.class)
public class Message {

    public static String COLLECTION_DATABASE_NAME = "messages";
    public static String ID_USER_DATABASE_FIELD = "id_user";
    public static String ID_CONVERSATION_DATABASE_FIELD = "id_conv";
    public static String DATE_DATABASE_FIELD = "date";
    public static String CONTENT_DATABASE_FIELD = "content";

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    private String id_conv;
    private String id_user;
    private Date date;
    private String content;

    public Message() {
    }

    public Message(String id_conv, String id_user, Date date, String content) {
        this.id_conv = id_conv;
        this.id_user = id_user;
        this.date = date;
        this.content = content;
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

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                "id_conv='" + id_conv + '\'' +
                ", id_user='" + id_user + '\'' +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }

}
