package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chirper.databinding.ActivityAboutAppBinding;

public class AboutAppActivity extends AppCompatActivity {

    private ActivityAboutAppBinding mActivityAboutAppBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAboutAppBinding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(mActivityAboutAppBinding.getRoot());
        getSupportActionBar().setTitle("About Chirper");
    }
}