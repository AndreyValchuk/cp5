package com.example.finance.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

@Dao
public interface OperationDao {

    @Query("SELECT SUM(monetaryAmount) FROM operation")
    LiveData<Integer> getBalance();

    @Query("SELECT * FROM operation WHERE categoryId = :categoryId")
    LiveData<List<Operation>> getOperationsForCategory(long categoryId);

    @Query("SELECT * FROM operation WHERE operationDate >= :date")
    LiveData<List<Operation>> getOperationsFromDate(Date date);

    @Insert
    long insert(Operation operation);
}
