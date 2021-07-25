package com.example.chirper.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirper.Models.Users;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    ArrayList<Users> mUsersArrayList;
    Context mContext;

    public RequestsAdapter(ArrayList<Users> usersArrayList, Context mContext) {
        mUsersArrayList = usersArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mUsersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

        }
    }
}
