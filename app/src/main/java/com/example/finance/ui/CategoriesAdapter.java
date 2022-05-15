package com.example.finance.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.db.Category;
import com.example.finance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Category> operationCategories = new ArrayList<>();

    public List<Category> getOperationCategories() {
        return operationCategories;
    }

    public void addCategories(List<Category> categoriesToAdd) {
        List<Category> newCategories = categoriesToAdd.stream().filter(categoryToAdd -> operationCategories.stream()
                .noneMatch(category -> categoryToAdd.getId() == category.getId())).collect(Collectors.toList());
        operationCategories.addAll(newCategories);
        notifyItemRangeChanged(operationCategories.size() - newCategories.size(), newCategories.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ((TextView)viewHolder.getView().findViewById(R.id.category_name)).setText(operationCategories.get(position).getName());
        viewHolder.getView().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), OperationsActivity.class);
            intent.putExtra("category", operationCategories.get(position));
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return operationCategories.size();
    }
}

