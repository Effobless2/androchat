package com.example.firelib;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserManagement {

    public static void register(final IUserRegistrationListener context, User user){
        context.processStarted();
        DbConnect.getDatabase().collection("users").add(user)
                .continueWith(new Continuation<DocumentReference, DocumentReference>() {
                    @Override
                    public DocumentReference then(@NonNull Task<DocumentReference> task) throws Exception {
                        context.processFinished(task.getResult());
                        return null;
                    }
                });
    }

    public static void connection(final IUserConnectionListener context, String login, String password){
        context.connectionStarted();
        DbConnect.getDatabase().collection("users").whereEqualTo("name", login).get()
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
}
