package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivityPhoneSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

public class PhoneSignIn extends AppCompatActivity {

    private ActivityPhoneSignInBinding mActivityPhoneSignInBinding;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private ProgressDialog mProgressDialog;
    private CountryCodePicker ccp;

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

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mActivityPhoneSignInBinding.mobilenumber);

        mActivityPhoneSignInBinding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mActivityPhoneSignInBinding.mobilenumber.getText().toString().isEmpty()) {

                    mActivityPhoneSignInBinding.mobilenumber.setError("Must Enter a number");

                } else {

                    Intent intent=new Intent(PhoneSignIn.this, Manageotp.class);
                    intent.putExtra("mobile", mActivityPhoneSignInBinding.ccp.getFullNumberWithPlus().replace(" ",""));
                    startActivity(intent);
                    finish();

                }


            }
        });

        mActivityPhoneSignInBinding.emailsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailintent = new Intent(PhoneSignIn.this, SignInActivity.class);
                emailintent.putExtra("Previous intent","Phone sign in");
                startActivity(emailintent);
                finish();

            }
        });



    }


}
