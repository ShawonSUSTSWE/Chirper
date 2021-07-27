package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chirper.Models.Users;
import com.example.chirper.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {


    private ActivityUserProfileBinding mActivityUserProfileBinding;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUser, mMobile;
    private final static int GALLERY_PICK_CODE = 3;
    private StorageReference imageMessageStorageRef;
    private String download_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(mActivityUserProfileBinding.getRoot());
        setSupportActionBar(mActivityUserProfileBinding.toolbar4);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUser = mFirebaseDatabase.getReference().child("Users");
        mMobile = mFirebaseDatabase.getReference().child("Mobile Users");
        imageMessageStorageRef = FirebaseStorage.getInstance().getReference().child("messages_image");

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(mFirebaseAuth.getCurrentUser()!=null) {
                    Users user = snapshot.child(mFirebaseAuth.getCurrentUser().getUid()).getValue(Users.class);
                    if (!user.getProfile_picture().equals("default")) {
                        Picasso.get().load(user.getProfile_picture()).into(mActivityUserProfileBinding.imageProfile);
                    }
                    if (user.getUsername() != null) {
                        mActivityUserProfileBinding.editFullName.setText(user.getUsername());
                    }
                    mActivityUserProfileBinding.editUserEmail.setText(user.getEmail());
                    mActivityUserProfileBinding.editUserAddress.setText(user.getAddress());
                    mActivityUserProfileBinding.editBio.setText(user.getBio());
                    mActivityUserProfileBinding.editUserPhoneNo.setText(user.getPhoneNo());
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mMobile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot2) {

                mUser.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                        if(snapshot2.hasChild(snapshot1.child("phoneNo").getValue().toString())) {

                            mActivityUserProfileBinding.editUserPhoneNo.setVisibility(View.GONE);

                        } else {
                            mActivityUserProfileBinding.editUserEmail.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mActivityUserProfileBinding.changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mupdate = mUser.child(mFirebaseAuth.getCurrentUser().getUid());

                if ( mActivityUserProfileBinding.editFullName.getText().toString() != null && !mActivityUserProfileBinding.editFullName.getText().toString().equals("") ) {
                    mupdate.child("username").setValue(mActivityUserProfileBinding.editFullName.getText().toString());
                }
                if ( mActivityUserProfileBinding.editBio.getText().toString() != null && !mActivityUserProfileBinding.editBio.getText().toString().equals("") ) {
                    mupdate.child("bio").setValue(mActivityUserProfileBinding.editBio.getText().toString());
                }
                if ( mActivityUserProfileBinding.editUserPhoneNo.getText().toString() != null && !mActivityUserProfileBinding.editUserPhoneNo.getText().toString().equals("") ) {
                    mupdate.child("phoneNo").setValue(mActivityUserProfileBinding.editUserPhoneNo.getText().toString());
                }
                if ( mActivityUserProfileBinding.editUserEmail.getText().toString() != null && !mActivityUserProfileBinding.editUserEmail.getText().toString().equals("") ) {
                    mupdate.child("email").setValue(mActivityUserProfileBinding.editUserEmail.getText().toString());
                }
                if ( mActivityUserProfileBinding.editUserAddress.getText().toString() != null && !mActivityUserProfileBinding.editUserAddress.getText().toString().equals("") ) {
                    mupdate.child("address").setValue(mActivityUserProfileBinding.editUserAddress.getText().toString());
                }
                finish();

            }
        });

        mActivityUserProfileBinding.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK_CODE);
            }
        });

        mActivityUserProfileBinding.backbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override // for gallery picking
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  For image sending
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();

            // image message sending size compressing will be placed below

            final StorageReference file_path = imageMessageStorageRef.child(mFirebaseAuth.getCurrentUser().getUid() + ".jpg");

            UploadTask uploadTask = file_path.putFile(imageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                    if (!task.isSuccessful()){
                        Toast.makeText(UserProfileActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    download_url = file_path.getDownloadUrl().toString();
                    return file_path.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        if (task.isSuccessful()){
                            download_url = task.getResult().toString();
                            Toast.makeText(UserProfileActivity.this, "From ChatActivity, link: " +download_url, Toast.LENGTH_SHORT).show();
                            mUser.child(mFirebaseAuth.getCurrentUser().getUid()).child("profile_picture").setValue(download_url);

                        } else{
                            Toast.makeText(UserProfileActivity.this, "Failed to send image. Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}