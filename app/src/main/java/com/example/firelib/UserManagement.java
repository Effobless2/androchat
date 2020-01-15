package com.example.firelib;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.example.model.UserRegistration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    private static final String LOG_CATEGORY = "UserManagement";

    public static Task<List<User>> getAllUser(){
        return UserDAL.getAllUser().get()
                .continueWith(new Continuation<QuerySnapshot, List<User>>() {
                    @Override
                    public List<User> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<User> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            result.add(documentSnapshot.toObject(User.class));
                        }
                        return result;
                    }
                });
    }

    public static Task<List<User>> getUserByLogin(String login){
        return UserDAL.getUserByLogin(login).get()
                .continueWith(new Continuation<QuerySnapshot, List<User>>() {
                    @Override
                    public List<User> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<User> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            result.add(documentSnapshot.toObject(User.class));
                        }
                        return result;
                    }
                });
    }

    public static Task<List<User>> getUserByPseudo(String pseudo){
        return UserDAL.getUserByPseudo(pseudo).get()
                .continueWith(new Continuation<QuerySnapshot, List<User>>() {
                    @Override
                    public List<User> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<User> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            result.add(documentSnapshot.toObject(User.class));
                        }
                        return result;
                    }
                });
    }

    public static Task<User> getUserById(String id){
        return UserDAL.getUserById(id).get()
                .continueWith(new Continuation<DocumentSnapshot, User>() {
                    @Override
                    public User then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        DocumentSnapshot snapshot = task.getResult();
                        return snapshot.toObject(User.class);
                    }
                });
    }

    public static Task<String> connection(String login, String password){
        return UserDAL.connection(login, password).get()
                .continueWith(new Continuation<QuerySnapshot, String>() {
                    @Override
                    public String then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        if (documentSnapshots.size() > 0){
                            return documentSnapshots.get(0).getId();
                        }
                        return null;
                    }
                });
    }

    public static Task<String> registration(UserRegistration newUser){
        RegistrationAsyncTask task = new RegistrationAsyncTask(newUser);
        task.execute();
        return task.getTask();
    }

    public static boolean loginOrPseudoValidation(String text){
        if(text.contains(" ")) return false;
        return true;
    }
}
