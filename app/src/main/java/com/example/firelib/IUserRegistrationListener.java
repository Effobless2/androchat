package com.example.firelib;

import com.google.firebase.firestore.DocumentReference;

public interface IUserRegistrationListener {
    void processStarted();
    void processFinished(DocumentReference documentReference);
}
