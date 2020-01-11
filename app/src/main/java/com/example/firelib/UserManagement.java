package com.example.firelib;

import com.example.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.List;

public class UserManagement {

    public static Task<DocumentReference> Register(User user){
        return DbConnect.getDatabase().collection("users").add(user);
    }

    public static Query getAll(){
        return DbConnect.getDatabase().collection("users").orderBy("name");
    }
}
