package com.example.chirper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.chirper.Adapters.MessageAdapter;
import com.example.chirper.Models.MessageModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private ActivityChatBinding mActivityChatBinding;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReferenceUsers, mSender, mRoot, mMessageRef;
    FirebaseAuth mFirebaseAuth;
    String senderId, receiverid, receivername, receiverimage, download_url;
    private final List<MessageModel> mMessageModelList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;


    private final static int GALLERY_PICK_CODE = 2;
    private StorageReference imageMessageStorageRef;


    //Commit check
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
        imageMessageStorageRef = FirebaseStorage.getInstance().getReference().child("messages_image");

        senderId = mFirebaseAuth.getCurrentUser().getUid();
        receiverid = getIntent().getStringExtra("UserID");
        receivername = getIntent().getStringExtra("Username");
        receiverimage = getIntent().getStringExtra("Userpic");
        mSender = mFirebaseDatabase.getReference().child("Users").child(receiverid);
        mFirebaseDatabase.getReference().keepSynced(true);



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
        mActivityChatBinding.audiocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChatActivity.this,"Will be added in future updates InshaAllah",Toast.LENGTH_SHORT).show();

            }
        });
        mActivityChatBinding.videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChatActivity.this,"Will be added in future updates InshaAllah",Toast.LENGTH_SHORT).show();

            }
        });
        mActivityChatBinding.moreoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ChatActivity.this,"Will be added in future updates InshaAllah",Toast.LENGTH_SHORT).show();

            }
        });
        mActivityChatBinding.sendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK_CODE);

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

            final String message_sender_reference = "Messages/" + senderId + "/" + receiverid;
            final String message_receiver_reference = "Messages/" + receiverid + "/" + senderId;

            DatabaseReference user_message_key = mRoot.child("Messages").child(senderId).child(receiverid).push();
            final String message_push_id = user_message_key.getKey();

            final StorageReference file_path = imageMessageStorageRef.child(message_push_id + ".jpg");

            UploadTask uploadTask = file_path.putFile(imageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                    if (!task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(ChatActivity.this, "From ChatActivity, link: " +download_url, Toast.LENGTH_SHORT).show();

                            HashMap<String, Object> message_text_body = new HashMap<>();
                            message_text_body.put("message", download_url);
                            message_text_body.put("seen", false);
                            message_text_body.put("type", "image");
                            message_text_body.put("time", ServerValue.TIMESTAMP);
                            message_text_body.put("from", senderId);

                            HashMap<String, Object> messageBodyDetails = new HashMap<>();
                            messageBodyDetails.put(message_sender_reference + "/" + message_push_id, message_text_body);
                            messageBodyDetails.put(message_receiver_reference + "/" + message_push_id, message_text_body);

                            mRoot.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null){
                                        Log.e("from_image_chat: ", databaseError.getMessage());
                                    }
                                    mActivityChatBinding.sendmessage.setText("");
                                }
                            });
                            Log.e("tag", "Image sent successfully");
                        } else{
                            Toast.makeText(ChatActivity.this, "Failed to send image. Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void loadMessage() {

        mRoot.child("Messages").child(senderId).child(receiverid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                MessageModel model = snapshot.getValue(MessageModel.class);
                mMessageModelList.add(model);
                mMessageAdapter.notifyDataSetChanged();
                mActivityChatBinding.chatRecyclerview.scrollToPosition(mMessageModelList.size()-1);

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