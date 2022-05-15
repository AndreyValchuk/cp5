package com.example.finance.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.sql.Date;
import java.util.List;

public class OperationRepository {

    private final OperationDao operationDao;

    public OperationRepository(Application application) {
        operationDao = OperationsDatabase.getDatabase(application).operationDao();
    }

    public LiveData<Integer> getBalance() {
        return operationDao.getBalance();
    }

    public LiveData<List<Operation>> getOperationsForCategory(long categoryId) {
        return operationDao.getOperationsForCategory(categoryId);
    }

    public LiveData<List<Operation>> getOperationsFromDate(Date date) {
        return operationDao.getOperationsFromDate(date);
    }

    public void insert(Operation operation) {
        OperationsDatabase.databaseWriteExecutor.execute(() -> operation.setId(operationDao.insert(operation)));
    }
}
