package com.example.model;

import java.util.Date;

public class Conversation {
    public static String COLLECTION_DATABASE_NAME = "conversations";
    public static String NAME_DATABASE_FIELD = "name";
    public static String LAST_MESSAGE_DATE_DATABASE_FIELD = "last_message_date";
    public static String ID_DATABASE_FIELD = "id";

    private String id;
    private String name;
    private Date last_message_date;

    public Conversation() {
    }

    public Conversation(String id, String name, Date last_message_date) {
        this.id = id;
        this.name = name;
        this.last_message_date = last_message_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLast_message_date() {
        return last_message_date;
    }

    public void setLast_message_date(Date last_message_date) {
        this.last_message_date = last_message_date;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", last_message_date=" + last_message_date +
                '}';
    }

}
