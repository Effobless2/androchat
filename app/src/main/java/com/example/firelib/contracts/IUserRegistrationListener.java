package com.example.firelib.contracts;

import com.google.firebase.firestore.DocumentReference;

public interface IUserRegistrationListener {
    void processStarted();
    void processFinished(String id);
}
