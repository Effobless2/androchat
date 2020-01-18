package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class User {
    public static final String PASSWORD_DATABASE_FIELD = "password";
    public static String COLLECTION_DATABASE_NAME = "users";

    public static String LOGIN_DATABASE_FIELD = "login";
    public static String PSEUDO_DATABASE_FIELD = "pseudo";
    public static String FIRSTNAME_DATABASE_FIELD = "firstname";
    public static String LASTNAME_DATABASE_FIELD = "lastname";


    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    @NonNull
    private String login;

    private String firstname;
    private String lastname;

    @NonNull
    private String pseudo;


    @Ignore
    public User() {
    }

    public User(String login, String firstname, String lastname, String pseudo) {
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pseudo = pseudo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return "User{" +
                "pseudo='" + pseudo + '\'' +
                '}';
    }
}
