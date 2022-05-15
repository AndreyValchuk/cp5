package com.example.finance.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

public class OperationsDialogTextWatcher implements TextWatcher {

    private final Button addButton;

    public OperationsDialogTextWatcher(Button addButton) {
        this.addButton = addButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().isEmpty()) {
            addButton.setEnabled(true);

            return;
        }

        addButton.setEnabled(false);
    }
}
