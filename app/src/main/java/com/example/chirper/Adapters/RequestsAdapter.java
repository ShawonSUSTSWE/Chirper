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

import com.example.chirper.Models.Users;
import com.example.chirper.ProfileActivity;
import com.example.chirper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    ArrayList<Users> mUsersArrayList;
    Context mContext;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mFirebaseAuth;

    public RequestsAdapter(ArrayList<Users> usersArrayList, Context mContext) {
        mUsersArrayList = usersArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sample_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestsAdapter.ViewHolder holder, int position) {

        mFirebaseDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mFirebaseAuth = FirebaseAuth.getInstance();
        Users user = mUsersArrayList.get(position);
        Picasso.get().load(user.getProfile_picture()).placeholder(R.drawable.user_2).into(holder.mImageview);
        holder.req_Username.setText(user.getUsername());
        if(user.getEmail()!=null) {
            holder.req_email.setText(user.getEmail());
        } else {
            holder.req_email.setText(user.getPhoneNo());
        }
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
        holder.acceptreqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String currentDate = DateFormat.getDateInstance().format(new Date());
                mFirebaseDatabase.getReference().child("Friends").child(mFirebaseAuth.getCurrentUser().getUid()).child(user.getUserId()).child("Date").setValue(currentDate);
                mFirebaseDatabase.getReference().child("Friends").child(user.getUserId()).child(mFirebaseAuth.getCurrentUser().getUid()).child("Date").setValue(currentDate);
                mFirebaseDatabase.getReference("Friend_req").child(mFirebaseAuth.getCurrentUser().getUid()).child(user.getUserId()).removeValue();
                mFirebaseDatabase.getReference("Friend_req").child(user.getUserId()).child(mFirebaseAuth.getCurrentUser().getUid()).removeValue();

            }
        });
        holder.declinereqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabase.getReference("Friend_req").child(mFirebaseAuth.getCurrentUser().getUid()).child(user.getUserId()).removeValue();
                mFirebaseDatabase.getReference("Friend_req").child(user.getUserId()).child(mFirebaseAuth.getCurrentUser().getUid()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageview;
        TextView req_Username, req_email;
        FloatingActionButton acceptreqbtn, declinereqbtn;

        public ViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

            mImageview = itemView.findViewById(R.id.profile_image_req);
            req_Username = itemView.findViewById(R.id.userNamereq);
            req_email = itemView.findViewById(R.id.reqemail);
            acceptreqbtn = itemView.findViewById(R.id.acceptfriendrequestbutton);
            declinereqbtn = itemView.findViewById(R.id.declinerequestbutton);

        }
    }
}
