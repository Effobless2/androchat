package com.example.firelib.Exceptions;

import java.util.List;

public class RegistrationException extends Exception {

    List<RegistrationErrors> errors;

    public List<RegistrationErrors> getErrors() {
        return errors;
    }

    public RegistrationException(List<RegistrationErrors> errors){
        super();
        this.errors = errors;
    }
}
