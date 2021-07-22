package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.ScriptGroup;

import com.example.chirper.databinding.ActivityProfileBinding;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {


    private ActivityProfileBinding mActivityProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(mActivityProfileBinding.getRoot());

        String getusernamefromintent = getIntent().getStringExtra("Username");
        String getuserIDfromintent = getIntent().getStringExtra("UserID");
        String getuserpicfromintent = getIntent().getStringExtra("Userpic");

        if(getusernamefromintent!=null)
            mActivityProfileBinding.displayusername.setText(getusernamefromintent);
        if(getuserpicfromintent!= null)
            Picasso.get().load(getuserpicfromintent).into(mActivityProfileBinding.circleImageView);


        getSupportActionBar().hide();

    }
}