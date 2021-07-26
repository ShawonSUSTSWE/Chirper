package com.example.chirper;

import android.os.Bundle;

import com.example.chirper.Adapters.MessageAdapter;
import com.example.chirper.Models.MessageModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chirper.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private ActivityChatBinding mActivityChatBinding;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReferenceUsers, mSender, mRoot, mMessageRef;
    FirebaseAuth mFirebaseAuth;
    String senderId, receiverid, receivername, receiverimage;
    private final List<MessageModel> mMessageModelList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;


    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseReferenceUsers.child("lastseen").onDisconnect().setValue(ServerValue.TIMESTAMP);
        mDatabaseReferenceUsers.child("online_status").onDisconnect().setValue(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(mActivityChatBinding.getRoot());
        setSupportActionBar(mActivityChatBinding.toolbar2);

        //Attribution: By Smashicons
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mRoot = mFirebaseDatabase.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceUsers = mFirebaseDatabase.getReference().child("Users").child(mFirebaseAuth.getCurrentUser().getUid());

        senderId = mFirebaseAuth.getCurrentUser().getUid();
        receiverid = getIntent().getStringExtra("UserID");
        receivername = getIntent().getStringExtra("Username");
        receiverimage = getIntent().getStringExtra("Userpic");
        mSender = mFirebaseDatabase.getReference().child("Users").child(receiverid);



        mActivityChatBinding.usernameinchat.setText(receivername);
        mActivityChatBinding.usernameinchat.setSelected(true);
        Picasso.get().load(receiverimage).placeholder(R.drawable.user_2).into(mActivityChatBinding.profileUserPicture);

        mMessageAdapter = new MessageAdapter(mMessageModelList, getApplicationContext());
        mLinearLayoutManager = new LinearLayoutManager(this);
        mActivityChatBinding.chatRecyclerview.setHasFixedSize(true);
        mActivityChatBinding.chatRecyclerview.setLayoutManager(mLinearLayoutManager);
        mActivityChatBinding.chatRecyclerview.setAdapter(mMessageAdapter);

        loadMessage();


        mSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.hasChild("lastseen")) {

                    if(snapshot.child("lastseen").getValue().equals("true")) {
                        Log.d("SHAWONDebug", snapshot.child("lastseen").getValue().toString());
                        mActivityChatBinding.onlineTimestamp.setText("Online");
                    } else {
                        Log.d("SHAWONDebug", snapshot.child("lastseen").getValue().toString());
                        GetTimeAgo timeAgo = new GetTimeAgo();
                        long lastTime = Long.parseLong(snapshot.child("lastseen").getValue().toString());
                        mActivityChatBinding.onlineTimestamp.setText(timeAgo.getTimeAgo(lastTime, getApplicationContext()));
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mRoot.child("Chat").child(senderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(!snapshot.hasChild(receiverid)) {
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + senderId + "/" + receiverid, chatAddMap);
                    chatUserMap.put("Chat/" + receiverid + "/" + senderId, chatAddMap);

                    mRoot.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {

                            if (error != null) {
                                Log.d("Debugging Database", error.getMessage().toString());
                            }

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mActivityChatBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        mActivityChatBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });


    }

    private void loadMessage() {

        mRoot.child("Messages").child(senderId).child(receiverid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                MessageModel model = snapshot.getValue(MessageModel.class);
                mMessageModelList.add(model);
                mMessageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void sendMessage() {

        String message = mActivityChatBinding.sendmessage.getText().toString();

        if(!TextUtils.isEmpty(message)) {

            String senderref = "Messages/"+ senderId + "/" + receiverid;
            String receiverref = "Messages/"+ receiverid + "/" + senderId;

            DatabaseReference mMessageRef = mRoot.child("Messages").child(senderId).child(receiverid).push();
            String push_id = mMessageRef.getKey();

            Map messageMap = new HashMap();
            messageMap.put( "message", message );
            messageMap.put( "seen", false );
            messageMap.put( "type", "text" );
            messageMap.put( "time", ServerValue.TIMESTAMP );
            messageMap.put( "from", senderId );

            Map messageUserMap = new HashMap();
            messageUserMap.put(senderref+"/"+ push_id, messageMap);
            messageUserMap.put(receiverref+"/"+push_id, messageMap);

            mActivityChatBinding.sendmessage.setHint("");
            mActivityChatBinding.sendmessage.setText("");

            mRoot.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {

                    if (error != null) {
                        Log.d("Debugging Database", error.getMessage().toString());
                    }
                }

            });
        }

    }
}