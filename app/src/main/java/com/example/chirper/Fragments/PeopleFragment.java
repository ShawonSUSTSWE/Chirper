package com.example.chirper.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirper.Adapters.PeopleAdapter;
import com.example.chirper.Adapters.UsersAdapter;
import com.example.chirper.Models.Users;
import com.example.chirper.R;
import com.example.chirper.databinding.FragmentChatsBinding;
import com.example.chirper.databinding.FragmentPeopleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {

    public PeopleFragment() {
        // Required empty public constructor
    }

    FragmentPeopleBinding mFragmentPeopleBinding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferenceforFriendStatus;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentPeopleBinding = FragmentPeopleBinding.inflate(inflater, container, false);

        PeopleAdapter adapter = new PeopleAdapter(list,getContext());
        mFragmentPeopleBinding.peopleRecyclerview.setAdapter(adapter);



        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
        mFragmentPeopleBinding.peopleRecyclerview.setLayoutManager(mlinearLayoutManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReferenceforFriendStatus = mFirebaseDatabase.getReference("Friend_req/"+mFirebaseUser.getUid());
        mFirebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    list.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        Users user = snapshot1.getValue(Users.class);
                        user.setUserId(snapshot1.getKey());
                        if (!mFirebaseUser.getUid().equals(user.getUserId())) {

                            list.add(user);

                        }
                    }
                    adapter.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return mFragmentPeopleBinding.getRoot();
    }
}