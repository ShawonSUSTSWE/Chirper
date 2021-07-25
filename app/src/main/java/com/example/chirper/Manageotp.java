package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivityManageotpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Manageotp extends AppCompatActivity {

    private ActivityManageotpBinding mActivityManageotpBinding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    String phonenumber, otpid, name, pass;
    private final String defaultBio = "Hey there! I am using Chirper";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityManageotpBinding = ActivityManageotpBinding.inflate(getLayoutInflater());
        setContentView(mActivityManageotpBinding.getRoot());
        getSupportActionBar().hide();

        mAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");

        //Getting number from signup
        phonenumber = getIntent().getStringExtra("mobile").toString();
        name = getIntent().getStringExtra("NAME");
        pass = getIntent().getStringExtra("PASS");

        initiateotp();

        mActivityManageotpBinding.veify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mActivityManageotpBinding.OTP.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_LONG).show();
                else if(mActivityManageotpBinding.OTP.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                else
                {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,mActivityManageotpBinding.OTP.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
    }

    private void initiateotp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            String id = mAuth.getCurrentUser().getUid();
                            Users user = new Users("default",name,"No email given",phonenumber,id,defaultBio,"No address given",true);
                            mFirebaseDatabase.getReference().child("Users").child(id).setValue(user);
                            mFirebaseDatabase.getReference().child("Users").child(id).child("phoneNo").setValue(phonenumber);
                            mFirebaseDatabase.getReference().child("Mobile Users").child(phonenumber).setValue(user);
                            mFirebaseDatabase.getReference().child("Mobile Users").child(phonenumber).child("phoneNo").setValue(phonenumber);
                            startActivity(new Intent(Manageotp.this,Dashboard.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),"Sign in Code Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}