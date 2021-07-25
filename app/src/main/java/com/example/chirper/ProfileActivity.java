package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;

import com.example.chirper.Fragments.ChatsFragment;
import com.example.chirper.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {


    private ActivityProfileBinding mActivityProfileBinding;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReferenceFriendreq, mDatabaseReferenceFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(mActivityProfileBinding.getRoot());
        getSupportActionBar().hide();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceFriendreq = mFirebaseDatabase.getReference("Friend_req");
        mDatabaseReferenceFriend = mFirebaseDatabase.getReference("Friends");

        String getusernamefromintent = getIntent().getStringExtra("Username");
        String getuserIDfromintent = getIntent().getStringExtra("UserID");
        String getuserpicfromintent = getIntent().getStringExtra("Userpic");

        if(getusernamefromintent!=null)
            mActivityProfileBinding.displayusername.setText(getusernamefromintent);
        if(getuserpicfromintent!= null)
            Picasso.get().load(getuserpicfromintent).into(mActivityProfileBinding.circleImageView);

        mDatabaseReferenceFriendreq.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                /*
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    if(dataSnapshot.getKey().equals(getuserIDfromintent) && dataSnapshot.child("Status").getValue().toString().equals("sent")) {

                        mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.cancelrequest.setVisibility(View.VISIBLE);

                    } else if (dataSnapshot.getKey().equals(getuserIDfromintent) && dataSnapshot.child("Status").getValue().toString().equals("received")) {

                        mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.cancelrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.acceptfriendrequest.setVisibility(View.VISIBLE);
                        mActivityProfileBinding.declinerequest.setVisibility(View.VISIBLE);

                    }

                }
                 */

                if (snapshot.hasChild(getuserIDfromintent)) {

                    if(snapshot.child(getuserIDfromintent).child("Status").getValue().toString().equals("sent")) {

                        mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.cancelrequest.setVisibility(View.VISIBLE);

                    } else if ( snapshot.child(getuserIDfromintent).child("Status").getValue().toString().equals("received")) {

                        mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.cancelrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.acceptfriendrequest.setVisibility(View.VISIBLE);
                        mActivityProfileBinding.declinerequest.setVisibility(View.VISIBLE);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mDatabaseReferenceFriend.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(getuserIDfromintent)) {

                        mActivityProfileBinding.acceptfriendrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.declinerequest.setVisibility(View.GONE);
                        mActivityProfileBinding.cancelrequest.setVisibility(View.GONE);
                        mActivityProfileBinding.chatbutton.setVisibility(View.VISIBLE);
                        mActivityProfileBinding.unfrienduser.setVisibility(View.VISIBLE);
                        mActivityProfileBinding.displayuserfriendtimeperiod.setText("Friends since " + snapshot.child(getuserIDfromintent).child("Date").getValue().toString());
                        mActivityProfileBinding.displayuserfriendtimeperiod.setVisibility(View.VISIBLE);

                    }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        mActivityProfileBinding.sendfriendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityProfileBinding.sendfriendrequest.setVisibility(View.GONE);
                mActivityProfileBinding.cancelrequest.setVisibility(View.VISIBLE);
                mFirebaseDatabase.getReference().child("Friend_req").child(mFirebaseAuth.getCurrentUser().getUid()).child(getuserIDfromintent).child("Status").setValue("sent");
                mFirebaseDatabase.getReference().child("Friend_req").child(getuserIDfromintent).child(mFirebaseAuth.getCurrentUser().getUid()).child("Status").setValue("received");

            }
        });

        mActivityProfileBinding.cancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityProfileBinding.cancelrequest.setVisibility(View.GONE);
                mActivityProfileBinding.sendfriendrequest.setVisibility(View.VISIBLE);
                mFirebaseDatabase.getReference().child("Friend_req").child(mFirebaseAuth.getCurrentUser().getUid()).child(getuserIDfromintent).child("Status").removeValue();
                mFirebaseDatabase.getReference().child("Friend_req").child(getuserIDfromintent).child(mFirebaseAuth.getCurrentUser().getUid()).child("Status").removeValue();

            }
        });

        mActivityProfileBinding.acceptfriendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String currentDate = DateFormat.getDateInstance().format(new Date());
                mActivityProfileBinding.acceptfriendrequest.setVisibility(View.GONE);
                mActivityProfileBinding.declinerequest.setVisibility(View.GONE);
                mActivityProfileBinding.chatbutton.setVisibility(View.VISIBLE);
                mActivityProfileBinding.unfrienduser.setVisibility(View.VISIBLE);
                mActivityProfileBinding.displayuserfriendtimeperiod.setText("Friends since "+ currentDate);
                mActivityProfileBinding.displayuserfriendtimeperiod.setVisibility(View.VISIBLE);
                mFirebaseDatabase.getReference().child("Friends").child(mFirebaseAuth.getCurrentUser().getUid()).child(getuserIDfromintent).child("Date").setValue(currentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        mFirebaseDatabase.getReference().child("Friends").child(getuserIDfromintent).child(mFirebaseAuth.getCurrentUser().getUid()).child("Date").setValue(currentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                mFirebaseDatabase.getReference().child("Friend_req").child(mFirebaseAuth.getCurrentUser().getUid()).child(getuserIDfromintent).removeValue();
                                mFirebaseDatabase.getReference().child("Friend_req").child(getuserIDfromintent).child(mFirebaseAuth.getCurrentUser().getUid()).removeValue();

                            }
                        });
                    }
                });

            }
        });

        mActivityProfileBinding.chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatintent = new Intent(ProfileActivity.this, ChatActivity.class);
                chatintent.putExtra("UserID", getuserIDfromintent);
                chatintent.putExtra("Username", getusernamefromintent);
                chatintent.putExtra("Userpic",getuserpicfromintent);
                startActivity(chatintent);

            }
        });

        mActivityProfileBinding.unfrienduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityProfileBinding.chatbutton.setVisibility(View.GONE);
                mActivityProfileBinding.unfrienduser.setVisibility(View.GONE);
                mActivityProfileBinding.displayuserfriendtimeperiod.setText("Friends");
                mActivityProfileBinding.displayuserfriendtimeperiod.setVisibility(View.GONE);
                mActivityProfileBinding.sendfriendrequest.setVisibility(View.VISIBLE);
                mFirebaseDatabase.getReference().child("Friends").child(mFirebaseAuth.getCurrentUser().getUid()).child(getuserIDfromintent).removeValue();
                mFirebaseDatabase.getReference().child("Friends").child(getuserIDfromintent).child(mFirebaseAuth.getCurrentUser().getUid()).removeValue();

            }
        });

        mActivityProfileBinding.declinerequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityProfileBinding.declinerequest.setVisibility(View.GONE);
                mActivityProfileBinding.acceptfriendrequest.setVisibility(View.GONE);
                mActivityProfileBinding.sendfriendrequest.setVisibility(View.VISIBLE);
                mFirebaseDatabase.getReference().child("Friend_req").child(mFirebaseAuth.getCurrentUser().getUid()).child(getuserIDfromintent).removeValue();
                mFirebaseDatabase.getReference().child("Friend_req").child(getuserIDfromintent).child(mFirebaseAuth.getCurrentUser().getUid()).removeValue();

            }
        });

    }
}