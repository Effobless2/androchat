package com.example.firelib;

import com.example.model.User;

public interface IUserConnectionListener {
    void connectionStarted();
    void connectionSucceeded(User documentReference);
    void connectionFailed();
}
