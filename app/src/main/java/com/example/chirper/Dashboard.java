package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chirper.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity
{

    FirebaseAuth mFirebaseAuth;
    private ActivityDashboardBinding mActivityDashboardBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(mActivityDashboardBinding.getRoot());

        //Lets try to commit this change


    }
}