package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.chirper.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {

    private ActivityHelpBinding mActivityHelpBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityHelpBinding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(mActivityHelpBinding.getRoot());

    }
}