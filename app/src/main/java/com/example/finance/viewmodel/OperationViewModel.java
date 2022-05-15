package com.example.finance.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.finance.db.Category;
import com.example.finance.db.CategoryRepository;
import com.example.finance.db.CategoryType;
import com.example.finance.db.Operation;
import com.example.finance.db.OperationRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class OperationViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;
    private final LiveData<Integer> balance;
    private final LiveData<List<Category>> cashInflowCategories;
    private final LiveData<List<Category>> cashOutflowCategories;
    private final LiveData<List<Operation>> operationsForCategory;
    private final LiveData<List<Operation>> operationsFromStartOfYear;
    private final MutableLiveData<Long> categoryIdFilter = new MutableLiveData<>();

    public OperationViewModel(Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        operationRepository = new OperationRepository(application);

        balance = operationRepository.getBalance();
        cashInflowCategories = categoryRepository.getAllCategoriesByType(CategoryType.CASH_INFLOW);
        cashOutflowCategories = categoryRepository.getAllCategoriesByType(CategoryType.CASH_OUTFLOW);
        operationsForCategory = Transformations.switchMap(categoryIdFilter, operationRepository::getOperationsForCategory);
        operationsFromStartOfYear = operationRepository.getOperationsFromDate(Date.valueOf(LocalDate.now().withDayOfYear(1).toString()));
    }

    public LiveData<Integer> getBalance() {
        return balance;
    }

    public LiveData<List<Category>> getCashInflowCategories() {
        return cashInflowCategories;
    }

    public LiveData<List<Category>> getCashOutflowCategories() {
        return cashOutflowCategories;
    }

    public LiveData<List<Operation>> getOperationsForCategory() {
        return operationsForCategory;
    }

    public void insertCategory(Category category) {
        categoryRepository.insert(category);
    }

    public void insertOperation(Operation operation) {
        operationRepository.insert(operation);
    }

    public void setCategoryIdFilter(long categoryId) {
        categoryIdFilter.setValue(categoryId);
    }

    public LiveData<List<Operation>> getOperationsFromStartOfYear() {
        return operationsFromStartOfYear;
    }
}
