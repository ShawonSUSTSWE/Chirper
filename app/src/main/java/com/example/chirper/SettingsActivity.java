package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chirper.R;
import com.example.chirper.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mActivitySettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mActivitySettingsBinding.getRoot());
    }
}