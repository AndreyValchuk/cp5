package com.example.finance.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class TabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    protected ViewBinding binding;

    public TabFragment(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        setArguments(bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}