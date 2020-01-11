package com.example.firelib;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.firelib.contracts.IUserConnectionListener;
import com.example.firelib.contracts.IUserRegistrationListener;
import com.example.firelib.contracts.IUserResearcherListener;
import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    private static final String LOG_CATEGORY = "UserManagement";

    public static void register(final IUserRegistrationListener context, final User user){
        context.verificationProcess();
        verifyLogin(user.getLogin())
        .continueWith(new Continuation<Boolean, Object>() {
            @Override
            public Object then(@NonNull Task<Boolean> task) throws Exception {
                boolean result = task.getResult();
                if(!result)
                    context.loginAlreadyTaken(user.getLogin());
                else{
                    verifyPseudo(user.getPseudo())
                            .continueWith(new Continuation<Boolean, Object>() {
                                @Override
                                public Object then(@NonNull Task<Boolean> task) throws Exception {
                                    boolean result = task.getResult();
                                    if(!result)
                                        context.pseudoAlreadyTaken(user.getPseudo());
                                    else{
                                        context.registrationProcess();
                                        register(user)
                                                .continueWith(new Continuation<DocumentReference, DocumentReference>() {
                                                    @Override
                                                    public DocumentReference then(@NonNull Task<DocumentReference> task) throws Exception {
                                                        context.registrationSucceed(task.getResult().getId());
                                                        return null;
                                                    }
                                                });
                                    }
                                    return null;
                                }
                            });
                }
                return null;
            }
        });

    }

    private static Task<Boolean> verifyLogin(String login){
        return DbConnect.getDatabase().collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.LOGIN_DATABASE_FIELD, login).get()
                .continueWith(new Continuation<QuerySnapshot, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        QuerySnapshot snapshots = task.getResult();
                        if(snapshots.getDocuments().size() != 0){
                            return false;
                        }
                        return true;
                    }
                });
    }

    private static Task<Boolean> verifyPseudo(String pseudo){
        return DbConnect.getDatabase().collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.PSEUDO_DATABASE_FIELD, pseudo).get()
                .continueWith(new Continuation<QuerySnapshot, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        QuerySnapshot snapshots = task.getResult();
                        if(snapshots.size() != 0){
                            return false;
                        }
                        return true;
                    }
                });
    }

    private static Task<DocumentReference> register(User user){
        return DbConnect.getDatabase().collection(User.COLLECTION_DATABASE_NAME).add(user);
    }

    public static void connection(final IUserConnectionListener context, String login, String password){
        context.connectionStarted();
        DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.LOGIN_DATABASE_FIELD, login)
                .whereEqualTo(User.PASSWORD_DATABASE_FIELD, password).get()
                .continueWith(new Continuation<QuerySnapshot, Object>() {
                    @Override
                    public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        QuerySnapshot snapshots = task.getResult();
                        List<DocumentSnapshot> list = snapshots.getDocuments();
                        if(list.size() == 0)
                            context.connectionFailed();
                        else{
                            User result = list.get(0).toObject(User.class);
                            context.connectionSucceeded(result);
                        }
                        return null;
                    }
                });
    }

    public static void getUsersByPseudo(final IUserResearcherListener context, String pseudo){
        context.searchInProgress();
        DbConnect.getDatabase()
                .collection(User.COLLECTION_DATABASE_NAME)
                .whereEqualTo(User.PSEUDO_DATABASE_FIELD, pseudo).get()
                .continueWith(new Continuation<QuerySnapshot, Object>() {
                    @Override
                    public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        Log.i("caca", "passé");
                        try{
                            QuerySnapshot snapshot = task.getResult();
                            List<DocumentSnapshot> list = snapshot.getDocuments();
                            List<User> result = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : list) {

                                result.add(documentSnapshot.toObject(User.class));
                            }
                            context.searchFinished(result);
                        } catch(Exception e){
                            Log.i(LOG_CATEGORY, e.getMessage());
                        }
                        return null;
                    }
                });
    }
}
