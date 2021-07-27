package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chirper.R;
import com.example.chirper.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mActivitySettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mActivitySettingsBinding.getRoot());
        setSupportActionBar(mActivitySettingsBinding.toolbar2);

        mActivitySettingsBinding.backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        mActivitySettingsBinding.option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent prointent = new Intent(SettingsActivity.this, CurrentUserProfile.class);
                startActivity(prointent);

            }
        });

        mActivitySettingsBinding.option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent helpintent = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(helpintent);

            }
        });

        mActivitySettingsBinding.option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent appintent = new Intent(SettingsActivity.this, AboutAppActivity.class);
                startActivity(appintent);

            }
        });

        mActivitySettingsBinding.option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contactintent = new Intent(SettingsActivity.this, ContactActivity.class);
                startActivity(contactintent);

            }
        });

    }
}