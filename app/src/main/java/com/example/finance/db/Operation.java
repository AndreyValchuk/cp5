package com.example.finance.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(foreignKeys = {@ForeignKey(entity = Category.class, parentColumns = {"id"}, childColumns = {"categoryId"})},
        indices = {@Index(value = {"categoryId"})})
public class Operation {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private final long categoryId;
    private final int monetaryAmount;
    private final Date operationDate;

    public Operation(long categoryId, int monetaryAmount, Date operationDate) {
        this.categoryId = categoryId;
        this.monetaryAmount = monetaryAmount;
        this.operationDate = operationDate;
    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public int getMonetaryAmount() {
        return monetaryAmount;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setId(long id) {
        this.id = id;
    }
}
