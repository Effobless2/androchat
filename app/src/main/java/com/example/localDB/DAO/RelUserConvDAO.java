package com.example.localDB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.RelUserConv;

import java.util.List;

@Dao
public interface RelUserConvDAO {

    @Insert
    void insert(RelUserConv relUserConv);

    @Update
    void update(RelUserConv relUserConv);

    @Delete
    void delete(RelUserConv relUserConv);

    @Query("SELECT * FROM rel_user_conv")
    LiveData<List<RelUserConv>> getAll();
}
