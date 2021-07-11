package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = "SignInActivity";
    ActivitySignInBinding mActivitySignInBinding;
    FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    boolean verified = false;
    ProgressDialog mProgressDialog, mGoogleProgressDialog;
    private GoogleSignInClient mGoogleSignInClient;

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

        mGoogleProgressDialog = new ProgressDialog(SignInActivity.this);
        mGoogleProgressDialog.setTitle("Google Sign In");
        mGoogleProgressDialog.setMessage("Signing in with your Google Account");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mGoogleSignInClient.revokeAccess();

        mActivitySignInBinding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mActivitySignInBinding.loginemail.getText().toString().isEmpty() ||
                        mActivitySignInBinding.loginpassword.getText().toString().isEmpty() ) {

                    if (mActivitySignInBinding.loginemail.getText().toString().isEmpty()) {

                        mActivitySignInBinding.loginemail.setError("This field cannot be blank");

                    }
                    if (mActivitySignInBinding.loginpassword.getText().toString().isEmpty()) {

                        mActivitySignInBinding.loginpassword.setError("This field cannot be blank");

                    }

                } else {

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
                                if (mFirebaseUser.isEmailVerified()) {

                                    verified = true;
                                    String id = mFirebaseUser.getUid();
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

                                    Toast.makeText(SignInActivity.this,"Password reset email sent to " + resetadd ,Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }
        });

        mActivitySignInBinding.googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SignIn();

            }
        });


    }

    int RC_SIGN_IN;
    private void SignIn () {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }




    private void firebaseAuthWithGoogle(String idToken) {
        mGoogleProgressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser mUser = mFirebaseAuth.getCurrentUser();
                            mGoogleProgressDialog.dismiss();

                            Users user = new Users();
                            user.setEmail(mUser.getEmail());
                            user.setUsername(mUser.getDisplayName());
                            user.setProfile_picture(mUser.getPhotoUrl().toString());
                            user.setVerified_email(true);

                            mFirebaseDatabase.getReference().child("Users").child(mUser.getUid()).setValue(user);

                            Intent intent3 = new Intent(SignInActivity.this,Dashboard.class);
                            startActivity(intent3);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
}