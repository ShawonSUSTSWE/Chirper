package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chirper.databinding.ActivityPhoneSignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneSignIn extends AppCompatActivity {

    private ActivityPhoneSignInBinding mActivityPhoneSignInBinding;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityPhoneSignInBinding = ActivityPhoneSignInBinding.inflate(getLayoutInflater());
        setContentView(mActivityPhoneSignInBinding.getRoot());

        getSupportActionBar().hide();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");

        mProgressDialog = new ProgressDialog(PhoneSignIn.this);
        mProgressDialog.setTitle("Logging in with Phone");
        mProgressDialog.setMessage("Logging into your account");

        mActivityPhoneSignInBinding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( mActivityPhoneSignInBinding.loginphone.getText().toString().isEmpty() ||
                        mActivityPhoneSignInBinding.loginpassword.getText().toString().isEmpty() ) {

                    if(mActivityPhoneSignInBinding.loginphone.getText().toString().isEmpty()) {

                        mActivityPhoneSignInBinding.loginphone.setError("This field can not be blank");

                    }
                    if(mActivityPhoneSignInBinding.loginpassword.getText().toString().isEmpty()) {

                        mActivityPhoneSignInBinding.loginpassword.setError("This field can not be blank");

                    }

                } else {




                }


            }
        });

        mActivityPhoneSignInBinding.emailsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailintent = new Intent(PhoneSignIn.this, SignInActivity.class);
                startActivity(emailintent);
                finish();

            }
        });



    }
}