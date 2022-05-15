package com.example.finance.ui;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.db.Category;
import com.example.finance.db.CategoryType;
import com.example.finance.db.Operation;
import com.example.finance.R;
import com.example.finance.viewmodel.OperationViewModel;

import java.sql.Date;
import java.time.LocalDate;

public class OperationsActivity extends AppCompatActivity {

    private OperationViewModel operationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);

        Category category = (Category) getIntent().getSerializableExtra("category");
        OperationsAdapter operationsAdapter = new OperationsAdapter();

        operationViewModel = new ViewModelProvider(this).get(OperationViewModel.class);
        operationViewModel.setCategoryIdFilter(category.getId());
        operationViewModel.getOperationsForCategory().observe(this, operationsAdapter::addOperations);

        ((TextView)findViewById(R.id.category_name)).setText(category.getName());

        ((RecyclerView)findViewById(R.id.operations)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView)findViewById(R.id.operations)).setAdapter(operationsAdapter);

        findViewById(R.id.add_operation_button).setOnClickListener(view -> {
            EditText operationEditText = new EditText(this);
            operationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.add_operation_dialog_title_text)
                    .setMessage(R.string.add_operation_dialog_text)
                    .setView(operationEditText)
                    .setNegativeButton(R.string.add_operation_dialog_cancel_button_text, null)
                    .setPositiveButton(R.string.add_operation_dialog_add_button_text, (dialogInterface, i) -> {
                        int monetaryAmount = Integer.parseInt((operationEditText.getText().toString()));
                        monetaryAmount = category.getType() == CategoryType.CASH_OUTFLOW ? -monetaryAmount : monetaryAmount;
                        operationViewModel.insertOperation(new Operation(category.getId(), monetaryAmount, Date.valueOf(LocalDate.now().toString())));
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            operationEditText.addTextChangedListener(new OperationsDialogTextWatcher(dialog.getButton(AlertDialog.BUTTON_POSITIVE)));
        });
    }
}
