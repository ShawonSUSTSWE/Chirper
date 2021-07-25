package com.example.chirper;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.chirper.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {


    private ActivityChatBinding mActivityChatBinding;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(mActivityChatBinding.getRoot());

        //Attribution: By Smashicons
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseAuth = FirebaseAuth.getInstance();

        String senderId = mFirebaseAuth.getUid();
        String receiverid = getIntent().getStringExtra("UserID");
        String receivername = getIntent().getStringExtra("Username");
        String receiverimage = getIntent().getStringExtra("Userpic");

        mActivityChatBinding.usernameinchat.setText(receivername);
        mActivityChatBinding.usernameinchat.setSelected(true);
        Picasso.get().load(receiverimage).placeholder(R.drawable.user_2).into(mActivityChatBinding.profileUserPicture);

        mActivityChatBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }
}