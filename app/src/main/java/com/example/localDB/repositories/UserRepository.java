package com.example.localDB.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.UserDAO;
import com.example.model.User;

import java.util.List;

public class UserRepository {
    private UserDAO userDAO;

    public UserRepository(Context context) {
        DBConnect database = DBConnect.getInstance(context);
        userDAO = database.userDAO();
    }

    public LiveData<List<User>> getAllContacts() {
        return userDAO.getAllContacts();
    }

    public void insert(User user) {
        new InsertAsyncTask(userDAO).execute(user);
    }

    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        public InsertAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            for (User user : users) {
                userDAO.insert(user);
            }
            return null;
        }
    }
}
