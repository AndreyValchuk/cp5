package com.example.finance.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.db.Category;
import com.example.finance.db.CategoryType;
import com.example.finance.R;
import com.example.finance.databinding.FragmentCategoriesBinding;
import com.example.finance.viewmodel.OperationViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriesFragment extends TabFragment {

    private OperationViewModel operationViewModel;

    public CategoriesFragment(int index) {
        super(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        CategoriesAdapter inflowCategoriesAdapter = new CategoriesAdapter();
        CategoriesAdapter outflowCategoriesAdapter = new CategoriesAdapter();

        operationViewModel = new ViewModelProvider(this).get(OperationViewModel.class);
        operationViewModel.getBalance().observe(getViewLifecycleOwner(), balance
                -> ((TextView)binding.getRoot().findViewById(R.id.balance)).setText(String.valueOf(balance == null ? 0 : balance)));
        operationViewModel.getCashInflowCategories().observe(getViewLifecycleOwner(), inflowCategoriesAdapter::addCategories);
        operationViewModel.getCashOutflowCategories().observe(getViewLifecycleOwner(), outflowCategoriesAdapter::addCategories);

        initializeCategoriesRecyclerView(binding.getRoot().findViewById(R.id.cash_inflow_categories), inflowCategoriesAdapter);
        initializeCategoriesRecyclerView(binding.getRoot().findViewById(R.id.cash_outflow_categories), outflowCategoriesAdapter);

        binding.getRoot().findViewById(R.id.add_inflow_category_button)
                .setOnClickListener(view -> addCategory(inflowCategoriesAdapter, CategoryType.CASH_INFLOW));
        binding.getRoot().findViewById(R.id.add_outflow_category_button)
                .setOnClickListener(view -> addCategory(outflowCategoriesAdapter, CategoryType.CASH_OUTFLOW));

        return binding.getRoot();
    }

    private void initializeCategoriesRecyclerView(RecyclerView recyclerView, CategoriesAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void addCategory(CategoriesAdapter categoriesAdapter, CategoryType categoryType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        EditText categoryEditText = new EditText(requireContext());
        builder.setTitle(R.string.add_category_dialog_title_text)
                .setMessage(R.string.add_category_dialog_text)
                .setView(categoryEditText)
                .setNegativeButton(R.string.add_category_dialog_cancel_button_text, null)
                .setPositiveButton(R.string.add_category_dialog_add_button_text, (dialogInterface, i)
                        -> operationViewModel.insertCategory(new Category(categoryEditText.getText().toString(), categoryType)));

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        List<String> categoriesStr = categoriesAdapter.getOperationCategories().stream().map(Category::getName).collect(Collectors.toList());
        categoryEditText.addTextChangedListener(new CategoriesDialogTextWatcher(categoriesStr, dialog.getButton(AlertDialog.BUTTON_POSITIVE)));
    }
}