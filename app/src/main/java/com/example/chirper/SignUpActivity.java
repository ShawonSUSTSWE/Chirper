package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

import java.util.EnumMap;

import static android.os.SystemClock.sleep;


public class SignUpActivity extends AppCompatActivity {


    ActivitySignUpBinding mActivitySignUpBinding;
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    ProgressDialog progressDialog;
    FirebaseUser mFirebaseUser;
    boolean possible = false;
    final int time = 600;
    ProgressDialog progressDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Attribution should be given to freepik from flaticon.com
        super.onCreate(savedInstanceState);


        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");


        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Your account is being created");

        progressDialog2 = new ProgressDialog(SignUpActivity.this);
        progressDialog2.setTitle("Verification Email");
        progressDialog2.setMessage("Sending verification email");


        mActivitySignUpBinding.signup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(isEmailValid(mActivitySignUpBinding.email.getText().toString()) &&
                        !mActivitySignUpBinding.username.getText().toString().isEmpty() &&
                        mActivitySignUpBinding.password.getText().toString().length() >= 8 ) {

                    createAccount();

                }
                else {

                    if (!isEmailValid(mActivitySignUpBinding.email.getText().toString())) {

                        mActivitySignUpBinding.email.setError("Enter valid email!");

                    }
                    if (mActivitySignUpBinding.username.getText().toString().isEmpty()) {

                        mActivitySignUpBinding.username.setError("Enter a username!");

                    }
                    if ( mActivitySignUpBinding.password.getText().toString().length() < 8 ) {

                        mActivitySignUpBinding.password.setError("Password must be 8 characters or longer!");

                    }
                }

            }
        });

        mActivitySignUpBinding.signinswitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent2);
                finish();

            }
        });

        mActivitySignUpBinding.signupphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent phone_intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(phone_intent);
                finish();

            }
        });

    }
    public void createAccount() {

        progressDialog.show();
        auth.createUserWithEmailAndPassword
                (mActivitySignUpBinding.email.getText().toString(), mActivitySignUpBinding.password.getText().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            progressDialog2.show();
                            mFirebaseUser = auth.getCurrentUser();

                            mFirebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog2.dismiss();
                                    Toast.makeText(SignUpActivity.this,"Must verify before continuing",Toast.LENGTH_SHORT).show();
                                    String id = mFirebaseUser.getUid();
                                    Users user = new Users(mActivitySignUpBinding.username.getText().toString(),
                                            mActivitySignUpBinding.email.getText().toString());
                                    mDatabase.getReference().child("Users").child(id).setValue(user);
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });




                        } else {

                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }


                });

    }
    boolean isEmailValid ( String email ) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
