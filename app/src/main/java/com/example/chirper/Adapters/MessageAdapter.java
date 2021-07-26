package com.example.chirper.Adapters;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chirper.Models.MessageModel;
import com.example.chirper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {


    private List<MessageModel> mMessagesModelList;
    Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MessageAdapter(List<MessageModel> messagesModelList, Context context) {
        mMessagesModelList = messagesModelList;
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) { mFirebaseAuth = FirebaseAuth.getInstance();
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.outgoing_msg_layout,parent,false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.incoming_msg_layout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(mMessagesModelList.get(position).getFrom().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = mMessagesModelList.get(position);
        String msg_type = messageModel.getType();

        if (holder.getClass() == SenderViewHolder.class) {
            if(msg_type.equals("text")) {
                ((SenderViewHolder) holder).senderimageview.setVisibility(View.GONE);
                ((SenderViewHolder) holder).mMessage.setVisibility(View.VISIBLE);
                ((SenderViewHolder) holder).mMessage.setText(messageModel.getMessage());
                ((SenderViewHolder) holder).mTime.setText(Long.toString(messageModel.getTime()));
            } else {
                ((SenderViewHolder) holder).mMessage.setVisibility(View.GONE);
                ((SenderViewHolder) holder).senderimageview.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(messageModel.getMessage()).into(((SenderViewHolder)holder).senderimageview);
                ((SenderViewHolder) holder).mTime.setText(Long.toString(messageModel.getTime()));
            }
        } else {
            if (msg_type.equals("text")) {
                ((ReceiverViewHolder) holder).receiverimageview.setVisibility(View.GONE);
                ((ReceiverViewHolder) holder).mMessagerec.setVisibility(View.VISIBLE);
                ((ReceiverViewHolder) holder).mMessagerec.setText(messageModel.getMessage());
                ((ReceiverViewHolder) holder).mTimerec.setText(Long.toString(messageModel.getTime()));
            } else {
                ((ReceiverViewHolder) holder).mMessagerec.setVisibility(View.GONE);
                ((ReceiverViewHolder) holder).receiverimageview.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(messageModel.getMessage()).into(((ReceiverViewHolder) holder).receiverimageview);
                ((ReceiverViewHolder) holder).mTimerec.setText(Long.toString(messageModel.getTime()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessagesModelList.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        public TextView mMessage, mTime;
        public ImageView senderimageview;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.sendermessage);
            mTime = itemView.findViewById(R.id.sendertime);
            senderimageview = itemView.findViewById(R.id.senderimage);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        public TextView mMessagerec, mTimerec;
        public ImageView receiverimageview;

        public ReceiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mMessagerec = itemView.findViewById(R.id.receivermessage);
            mTimerec = itemView.findViewById(R.id.receivertime);
            receiverimageview = itemView.findViewById(R.id.receiverimage);
        }
    }
}
