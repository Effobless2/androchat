package com.example.firelib;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

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
}
