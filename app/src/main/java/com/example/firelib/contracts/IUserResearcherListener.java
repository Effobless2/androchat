package com.example.firelib.contracts;

import com.example.model.User;

import java.util.List;

public interface IUserResearcherListener {
    void searchInProgress();
    void searchFinished(List<User> users);
}
