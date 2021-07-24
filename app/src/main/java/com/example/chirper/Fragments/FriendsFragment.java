package com.example.chirper.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirper.Adapters.UsersAdapter;
import com.example.chirper.Models.Users;
import com.example.chirper.R;
import com.example.chirper.databinding.FragmentChatsBinding;
import com.example.chirper.databinding.FragmentFriendsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    public FriendsFragment() {
        // Required empty public constructor
    }
    FragmentFriendsBinding mFragmentFriendsBinding;
    ArrayList<Users> list = new ArrayList<>();
    HashMap<String, Users> Friendlist = new HashMap<>();
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentFriendsBinding = FragmentFriendsBinding.inflate(inflater, container, false);

        UsersAdapter adapter = new UsersAdapter(list,getContext());
        mFragmentFriendsBinding.chatRecyclerview.setAdapter(adapter);



        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
        mFragmentFriendsBinding.chatRecyclerview.setLayoutManager(mlinearLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for ( DataSnapshot dataSnapshot: snapshot.getChildren() ) {

                    Users user = dataSnapshot.getValue(Users.class);
                    user.setUserId(dataSnapshot.getKey());
                    if(!mFirebaseUser.getUid().equals(user.getUserId())) {

                        Friendlist.put(dataSnapshot.getKey(), user);

                    }

                }
                mFirebaseDatabase.getReference().child("Friends/"+mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        list.clear();
                        for ( DataSnapshot dataSnapshot: snapshot.getChildren() ) {

                            if(Friendlist.containsKey(dataSnapshot.getKey())) {

                                list.add(Friendlist.get(dataSnapshot.getKey()));

                            }

                        }

                        adapter.notifyDataSetChanged();
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



        return mFragmentFriendsBinding.getRoot();
    }
}