package com.example.finance.db;

import androidx.room.TypeConverter;

import java.sql.Date;

public class Converters {

    @TypeConverter
    public static Date getDateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long getTimestampFromDate(Date date) {
        return date == null ? null : date.getTime();
    }
}
