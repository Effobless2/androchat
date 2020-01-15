package com.example.firelib.DAL;

import com.example.model.User;
import com.example.model.UserRegistration;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class UserDAL {
    public static Query getAllUser(){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .orderBy(User.PSEUDO_DATABASE_FIELD);
    }

    public static Query getUserByLogin(String login){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.LOGIN_DATABASE_FIELD, login)
                //.orderBy(User.LOGIN_DATABASE_FIELD)
                ;
    }

    public static Query getUserByPseudo(String pseudo){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.PSEUDO_DATABASE_FIELD, pseudo)
                //.orderBy(User.PSEUDO_DATABASE_FIELD)
                ;
    }

    public static DocumentReference getUserById(String id){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .document(id);

    }

    public static Query connection(String login, String password){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.LOGIN_DATABASE_FIELD, login)
                .whereEqualTo(User.PASSWORD_DATABASE_FIELD, password);
    }

    public static Task<DocumentReference> register(UserRegistration newUser){
        return DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .add(newUser);
    }


}
