package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = "SignInActivity";
    ActivitySignInBinding mActivitySignInBinding;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    boolean verified = false;
    ProgressDialog mProgressDialog;
    boolean flag = false;

    @Override
    public void onStart() {

        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null && mFirebaseUser.isEmailVerified()) {

            Intent intent_first = new Intent(SignInActivity.this, Dashboard.class);
            startActivity(intent_first);
            finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());

        getSupportActionBar().hide();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");

        mProgressDialog = new ProgressDialog(SignInActivity.this);
        mProgressDialog.setTitle("Logging in");
        mProgressDialog.setMessage("Logging into your account");

        mActivitySignInBinding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.show();
                mFirebaseAuth.signInWithEmailAndPassword(mActivitySignInBinding.loginemail.getText().toString(),
                            mActivitySignInBinding.loginpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                            mProgressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                if(mFirebaseUser.isEmailVerified()) {

                                    verified = true;
                                    String id = mFirebaseUser.getUid();
                                    if(flag) {
                                        mFirebaseDatabase.getReference().child("Users").child(id).child("password").setValue(mActivitySignInBinding.loginpassword.getText().toString());
                                    }
                                    mFirebaseDatabase.getReference().child("Users").child(id).child("verified_email").setValue(verified);
                                    Intent intent = new Intent(SignInActivity.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    mActivitySignInBinding.loginemail.setError("Email not verified");

                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




            }
        });

        mActivitySignInBinding.signupgotobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent2);
                finish();

            }
        });

        mActivitySignInBinding.forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resetadd = mActivitySignInBinding.loginemail.getText().toString();

                mFirebaseAuth.sendPasswordResetEmail(resetadd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    flag = true;
                                    Toast.makeText(SignInActivity.this,"Password reset email sent to " + resetadd ,Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }
        });

    }
}