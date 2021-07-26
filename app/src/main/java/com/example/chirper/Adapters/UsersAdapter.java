package com.example.chirper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirper.ChatActivity;
import com.example.chirper.Dashboard;
import com.example.chirper.Models.Users;
import com.example.chirper.ProfileActivity;
import com.example.chirper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{



    ArrayList<Users> list;
    Context mContext;


    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sample_show_users,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        Users user = list.get(position);
        Picasso.get().load(user.getProfile_picture()).placeholder(R.drawable.user_2).into(holder.mImageView);
        if(user.isOnline_status()) {
            holder.mView.setVisibility(View.VISIBLE);
        } else {
            holder.mView.setVisibility(View.GONE);
        }
        holder.userName.setText(user.getUsername());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileintent = new Intent(mContext, ProfileActivity.class);
                profileintent.putExtra("UserID", user.getUserId());
                profileintent.putExtra("Username", user.getUsername());
                profileintent.putExtra("Userpic",user.getProfile_picture());
                mContext.startActivity(profileintent);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatintent = new Intent(mContext, ChatActivity.class);
                chatintent.putExtra("UserID", user.getUserId());
                chatintent.putExtra("Username", user.getUsername());
                chatintent.putExtra("Userpic",user.getProfile_picture());
                mContext.startActivity(chatintent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView mImageView;
        TextView userName, Lastmsg;
        View mView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


            mImageView = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userName);
            Lastmsg = itemView.findViewById(R.id.lastmsg);
            mView = itemView.findViewById(R.id.online_status_2);

        }
    }

}
