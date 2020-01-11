package com.example.firelib.contracts;

public interface IUserRegistrationListener {
    void verificationProcess();
    void registrationProcess();
    void registrationSucceed(String id);
    void loginAlreadyTaken(String login);
    void pseudoAlreadyTaken(String pseudo);
}
