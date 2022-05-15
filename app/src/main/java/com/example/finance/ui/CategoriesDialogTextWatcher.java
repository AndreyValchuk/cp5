package com.example.finance.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import java.util.List;

public class CategoriesDialogTextWatcher implements TextWatcher {

    private final List<String> categories;
    private final Button addButton;

    public CategoriesDialogTextWatcher(List<String> categories, Button addButton) {
        this.categories = categories;
        this.addButton = addButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().isEmpty() && !categories.contains(editable.toString())) {
            addButton.setEnabled(true);

            return;
        }

        addButton.setEnabled(false);
    }
}
