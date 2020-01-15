package com.example.firelib.Exceptions;

public enum RegistrationErrors{
    LOGIN_NOT_IN_CORRECT_FORMAT, //Login didn't respects Format rules
    PSEUDO_NOT_IN_CORRECT_FORMAT, //Pseudo didn't respects Format rules
    LOGIN_ALREADY_USED, //Login is Already used
    PSEUDO_ALREADY_USED, //Pseudo is Already used
    REQUEST_CACHED, //Verifications passed but internet is disconnected and registration will be realized when reconnected
    REQUEST_NOT_CACHED, //Login and Pseudo format verification passed but internet disconnected before Unicity verifications
    SYSTEM_ERROR //Unknown error
}
