package com.example.chirper.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chirper.Adapters.UsersAdapter;
import com.example.chirper.Models.Users;
import com.example.chirper.R;
import com.example.chirper.databinding.FragmentChatsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding mFragmentChatsBinding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase mFirebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentChatsBinding = FragmentChatsBinding.inflate(inflater, container, false);

        UsersAdapter adapter = new UsersAdapter(list,getContext());
        mFragmentChatsBinding.chatRecyclerview.setAdapter(adapter);

        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
        mFragmentChatsBinding.chatRecyclerview.setLayoutManager(mlinearLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                list.clear();
                for ( DataSnapshot dataSnapshot: snapshot.getChildren() ) {

                    Users user = dataSnapshot.getValue(Users.class);
                    user.getUserId(dataSnapshot.getKey());
                    list.add(user);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return mFragmentChatsBinding.getRoot();
    }
}