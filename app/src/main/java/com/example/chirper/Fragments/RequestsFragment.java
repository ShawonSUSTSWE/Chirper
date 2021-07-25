package com.example.chirper.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirper.Adapters.RequestsAdapter;
import com.example.chirper.Models.Users;
import com.example.chirper.R;
import com.example.chirper.databinding.FragmentRequestsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    FragmentRequestsBinding mFragmentRequestsBinding;
    ArrayList<Users> mUsersArrayList = new ArrayList<>();
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentRequestsBinding = FragmentRequestsBinding.inflate(inflater, container, false);

        RequestsAdapter adapter = new RequestsAdapter(mUsersArrayList, getContext());
        mFragmentRequestsBinding.requestRecyclerview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mFragmentRequestsBinding.requestRecyclerview.setLayoutManager(linearLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseDatabase.getReference("Friend_req").child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {

                mUsersArrayList.clear();
                for ( DataSnapshot dataSnapshot: snapshot1.getChildren()) {

                    mFirebaseDatabase.getReference("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot2) {

                            Log.d("Data from Database:", dataSnapshot.getKey() + "   " + dataSnapshot.child("Status").getValue().toString());
                            Users user = snapshot2.child(dataSnapshot.getKey()).getValue(Users.class);
                            Log.d("Users:", user.getUserId() + "   " + user.getUsername());
                            if(dataSnapshot.child("Status").getValue().toString().equals("received")) {

                                mUsersArrayList.add(user);
                                Log.d("Users:", user.getUserId() + "   " + user.getUsername() + " " + mUsersArrayList.size());
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        return mFragmentRequestsBinding.getRoot();

    }
}