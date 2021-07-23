package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;

import com.example.chirper.databinding.ActivityProfileBinding;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {


    private ActivityProfileBinding mActivityProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(mActivityProfileBinding.getRoot());
        getSupportActionBar().hide();

        String getusernamefromintent = getIntent().getStringExtra("Username");
        String getuserIDfromintent = getIntent().getStringExtra("UserID");
        String getuserpicfromintent = getIntent().getStringExtra("Userpic");

        if(getusernamefromintent!=null)
            mActivityProfileBinding.displayusername.setText(getusernamefromintent);
        if(getuserpicfromintent!= null)
            Picasso.get().load(getuserpicfromintent).into(mActivityProfileBinding.circleImageView);

        mActivityProfileBinding.sendfriendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                mActivityProfileBinding.cancelrequest.setVisibility(View.VISIBLE);

            }
        });

        mActivityProfileBinding.cancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityProfileBinding.cancelrequest.setVisibility(View.GONE);
                mActivityProfileBinding.sendfriendrequest.setVisibility(View.VISIBLE);

            }
        });

    }
}