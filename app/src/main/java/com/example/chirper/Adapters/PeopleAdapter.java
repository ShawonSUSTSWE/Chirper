package com.example.chirper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirper.Models.Users;
import com.example.chirper.ProfileActivity;
import com.example.chirper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {


    ArrayList<Users> people_list;
    Context mContext;

    public PeopleAdapter(ArrayList<Users> people_list, Context context) {
        this.people_list = people_list;
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.sample_people,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PeopleAdapter.ViewHolder holder, int position) {

        Users user = people_list.get(position);
        Picasso.get().load(user.getProfile_picture()).placeholder(R.drawable.user_2).into(holder.mImageView);
        if(user.getEmail()!=null) {
            holder.mTextViewEmail.setText(user.getEmail());
        } else {
            holder.mTextViewEmail.setText(user.getPhoneNo());
        }
        holder.mTextViewName.setText(user.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileintent = new Intent(mContext, ProfileActivity.class);
                profileintent.putExtra("UserID", user.getUserId());
                profileintent.putExtra("Username", user.getUsername());
                profileintent.putExtra("Userpic",user.getProfile_picture());
                mContext.startActivity(profileintent);

            }
        });
        holder.mFloatingActionButtonsendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mFloatingActionButtonsendreq.setVisibility(View.GONE);
                holder.mFloatingActionButtoncancelreq.setVisibility(View.VISIBLE);

            }
        });
        holder.mFloatingActionButtoncancelreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mFloatingActionButtoncancelreq.setVisibility(View.GONE);
                holder.mFloatingActionButtonsendreq.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public int getItemCount() {

        return people_list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextViewName, mTextViewEmail;
        FloatingActionButton mFloatingActionButtonsendreq, mFloatingActionButtoncancelreq;
        
        public ViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

            mImageView = itemView.findViewById(R.id.profile_image);
            mTextViewName = itemView.findViewById(R.id.userNamepeople);
            mTextViewEmail = itemView.findViewById(R.id.peopleemail);
            mFloatingActionButtonsendreq = itemView.findViewById(R.id.sendfriendrequest);
            mFloatingActionButtoncancelreq = itemView.findViewById(R.id.cancelrequest);

        }
    }

}
