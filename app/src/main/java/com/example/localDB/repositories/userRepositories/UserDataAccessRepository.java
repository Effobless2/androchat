package com.example.localDB.repositories.userRepositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.UserDAO.UserDataAccessDAO;
import com.example.model.User;

import java.util.List;

public class UserDataAccessRepository {
    private Context context;
    private UserDataAccessDAO dao;

    public UserDataAccessRepository(Context context) {
        this.context = context;
        this.dao = DBConnect.getInstance(context).userDataAccessDAO();
    }

    public LiveData<List<User>> getAll(){
        return dao.getAll();
    }
}
