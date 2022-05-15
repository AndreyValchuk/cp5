package com.example.finance.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category WHERE type = :type")
    LiveData<List<Category>> getAllCategoriesByType(CategoryType type);

    @Insert
    long insert(Category category);
}
