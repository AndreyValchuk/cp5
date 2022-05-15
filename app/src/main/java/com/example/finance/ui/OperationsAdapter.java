package com.example.finance.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.db.Operation;
import com.example.finance.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class OperationsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Operation> operations = new ArrayList<>();

    public void addOperations(List<Operation> operationsToAdd) {
        List<Operation> newOperations = operationsToAdd.stream().filter(operationToAdd -> operations.stream()
                .noneMatch(operation -> operationToAdd.getId() == operation.getId())).collect(Collectors.toList());
        operations.addAll(newOperations);
        notifyItemRangeChanged(operations.size() - newOperations.size(), operations.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.operations_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ((TextView)viewHolder.getView().findViewById(R.id.monetary_amount)).setText(String.valueOf(operations.get(position).getMonetaryAmount()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        ((TextView)viewHolder.getView().findViewById(R.id.operation_date)).setText(formatter.format(operations.get(position).getOperationDate()));
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }
}

