package com.example.finance.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {

    private final CategoryDao categoryDao;

    public CategoryRepository(Application application) {
        categoryDao = OperationsDatabase.getDatabase(application).categoryDao();
    }

    public LiveData<List<Category>> getAllCategoriesByType(CategoryType type) {
        return categoryDao.getAllCategoriesByType(type);
    }

    public void insert(Category category) {
        OperationsDatabase.databaseWriteExecutor.execute(() -> category.setId(categoryDao.insert(category)));
    }
}
