package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chirper.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding mActivityContactBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        mActivityContactBinding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(mActivityContactBinding.getRoot());



    }
}