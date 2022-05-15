package com.example.finance.ui;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private final View view;

    public ViewHolder(View view) {
        super(view);
        this.view = view;
    }

    public View getView() {
        return view;
    }
}