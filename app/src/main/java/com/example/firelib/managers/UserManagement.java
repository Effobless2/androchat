package com.example.firelib.managers;

import androidx.annotation.NonNull;

import com.example.firelib.DAL.UserDAL;
import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
 * API of the library for Users Management
 * It should be the only class used by UIs
 * TODO: Foreach requests : Manage the way where the device is outline
 *  (already done for registration but could be better)
 * TODO: Take a look to ConnectivityManager
 *  (https://developer.android.com/training/basics/network-ops/managing#java)
 */
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

    public static Task<String> getDocumentRefenceByGoogleId(User user){
        ConnectionAsyncTask task = new ConnectionAsyncTask(user);
        task.execute();
        return task.getTask();
    }

}
