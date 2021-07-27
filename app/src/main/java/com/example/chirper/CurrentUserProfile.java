package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivityCurrentUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CurrentUserProfile extends AppCompatActivity {

    private ActivityCurrentUserProfileBinding mActivityCurrentUserProfileBinding;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityCurrentUserProfileBinding = ActivityCurrentUserProfileBinding.inflate(getLayoutInflater());
        setContentView(mActivityCurrentUserProfileBinding.getRoot());
        getSupportActionBar().setTitle("Profile");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUsersref = mFirebaseDatabase.getReference().child("Users");

        mUsersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(mFirebaseAuth.getCurrentUser()!= null) {
                    Users user = snapshot.child(mFirebaseAuth.getCurrentUser().getUid()).getValue(Users.class);
                    if (!user.getProfile_picture().equals("default")) {
                        Picasso.get().load(user.getProfile_picture()).into(mActivityCurrentUserProfileBinding.circleImageView);
                    }
                    if (user.getUsername() != null) {
                        mActivityCurrentUserProfileBinding.displaycusername.setText(user.getUsername());
                    }
                    mActivityCurrentUserProfileBinding.displaycuseremail.setText(user.getEmail());
                    mActivityCurrentUserProfileBinding.displaycusereaddress.setText(user.getAddress());
                    mActivityCurrentUserProfileBinding.displaycuserBio.setText(user.getBio());
                    mActivityCurrentUserProfileBinding.displaycuserphone.setText(user.getPhoneNo());
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mActivityCurrentUserProfileBinding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editintent = new Intent(CurrentUserProfile.this, UserProfileActivity.class);
                startActivity(editintent);

            }
        });


    }

}