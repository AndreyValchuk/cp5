package com.example.finance.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finance.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager()));
        binding.tabs.setupWithViewPager(binding.viewPager);
    }
}