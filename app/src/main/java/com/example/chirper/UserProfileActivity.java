package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.chirper.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends AppCompatActivity {


    private ActivityUserProfileBinding mActivityUserProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(mActivityUserProfileBinding.getRoot());


    }
}