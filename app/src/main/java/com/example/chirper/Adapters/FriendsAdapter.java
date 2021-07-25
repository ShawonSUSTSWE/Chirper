package com.example.chirper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirper.ChatActivity;
import com.example.chirper.Models.Users;
import com.example.chirper.ProfileActivity;
import com.example.chirper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{



    ArrayList<Users> list;
    Context mContext;


    public FriendsAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sample_people,parent,false);

        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendsAdapter.ViewHolder holder, int position) {

        Users user = list.get(position);
        Picasso.get().load(user.getProfile_picture()).placeholder(R.drawable.user_2).into(holder.mImageView);
        if(user.getEmail()!=null) {
            holder.Email.setText(user.getEmail());
        } else {
            holder.Email.setText(user.getPhoneNo());
        }
        holder.userName.setText(user.getUsername());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileintent = new Intent(mContext, ProfileActivity.class);
                profileintent.putExtra("UserID", user.getUserId());
                profileintent.putExtra("Username", user.getUsername());
                profileintent.putExtra("Userpic",user.getProfile_picture());

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
        TextView userName, Email;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


            mImageView = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNamepeople);
            Email = itemView.findViewById(R.id.peopleemail);

        }
    }

}
