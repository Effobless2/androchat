package com.example.firelib.contracts;

import com.example.model.User;

public interface IUserConnectionListener {
    void connectionStarted();
    void connectionSucceeded(User user);
    void connectionFailed();
}
