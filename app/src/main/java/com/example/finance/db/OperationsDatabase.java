package com.example.finance.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.finance.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Operation.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class OperationsDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract OperationDao operationDao();

    private static volatile OperationsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static OperationsDatabase getDatabase(final Context context) {
        RoomDatabase.Callback dbCallBack = new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                for (String category : context.getResources().getStringArray(R.array.default_inflow_operations)) {
                    databaseWriteExecutor.execute(() -> INSTANCE.categoryDao().insert(new Category(category, CategoryType.CASH_INFLOW)));
                }

                for (String category : context.getResources().getStringArray(R.array.default_outflow_operations)) {
                    databaseWriteExecutor.execute(() -> INSTANCE.categoryDao().insert(new Category(category, CategoryType.CASH_OUTFLOW)));
                }
            }
        };

        if (INSTANCE == null) {
            synchronized (OperationsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), OperationsDatabase.class, "operations_database")
                            .addCallback(dbCallBack)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
