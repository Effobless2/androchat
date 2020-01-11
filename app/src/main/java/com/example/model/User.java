package com.example.model;

public class User {
    public static final String PASSWORD_DATABASE_FIELD = "password";
    public static String COLLECTION_DATABASE_NAME = "users";

    public static String LOGIN_DATABASE_FIELD = "login";
    public static String PSEUDO_DATABASE_FIELD = "pseudo";
    public static String FIRSTNAME_DATABASE_FIELD = "firstname";
    public static String LASTNAME_DATABASE_FIELD = "lastname";

    private String login;

    private String firstname;
    private String lastname;
    private String pseudo;


    public User() {
    }

    public User(String login, String firstname, String lastname, String pseudo) {
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pseudo = pseudo;
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
}
