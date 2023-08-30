package com.socket.chat.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.socket.chat.R;
import com.socket.chat.enums.MessageStatus;
import com.socket.chat.models.message.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public abstract class MessageAdapter extends RecyclerView.Adapter {
    public abstract void chatSeen(ChatMessage chatMessage);

    private final List<ChatMessage> chatMessageList;
    private final Context context;
    private final Long userId;
    private final int SENDER_VIEWHOLDER = 0;
    private final int RECEIVER_VIEWHOLDER = 1;


    public MessageAdapter(List<ChatMessage> chatMessageList, Context context, Long userId) {

        this.chatMessageList = chatMessageList;
        this.context = context;
        this.userId = userId;

    }

    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(chatMessageList.get(position).getFromUserID(), userId))
            return SENDER_VIEWHOLDER;
        else
            return RECEIVER_VIEWHOLDER;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEWHOLDER) {
            var view = LayoutInflater.from(context).inflate(R.layout.adapter_sender, parent, false);
            return new OutgoingViewHolder(view);
        } else {
            var view = LayoutInflater.from(context).inflate(R.layout.adapter_receiver, parent, false);
            return new IncomingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getClass() == OutgoingViewHolder.class) {
            ((OutgoingViewHolder) holder).outgoingMsg.setText(chatMessageList.get(position).getMessage());
            if( chatMessageList.get(position).getMessageStatus().equals(MessageStatus.SEEN)) {
                ((OutgoingViewHolder) holder).seenUnseen.setImageDrawable(context.getResources().getDrawable(R.drawable.seen));
            } else {
                ((OutgoingViewHolder) holder).seenUnseen.setImageDrawable(context.getResources().getDrawable(R.drawable.un_seen));
            }
            var time = chatMessageList.get(position).getTime();
            final var cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            final var timeString =
                    new SimpleDateFormat("HH:mm").format(cal.getTime());

            ((OutgoingViewHolder) holder).outgoingMsgTime.setText(timeString);
        } else {
            if(chatMessageList.get(position).getMessageStatus() != MessageStatus.SEEN) {
                chatSeen(chatMessageList.get(position));
            }
            ((IncomingViewHolder) holder).incomingMsg.setText(chatMessageList.get(position).getMessage());
            long time = chatMessageList.get(position).getTime();
            final var cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            final var timeString =
                    new SimpleDateFormat("HH:mm").format(cal.getTime());

            ((IncomingViewHolder) holder).incomingMsgTime.setText(timeString);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public static class OutgoingViewHolder extends RecyclerView.ViewHolder {
        TextView outgoingMsg, outgoingMsgTime;
        ImageView seenUnseen;
        public OutgoingViewHolder(@NonNull View itemView) {
            super(itemView);

            outgoingMsg = itemView.findViewById(R.id.outgoingMessage);
            outgoingMsgTime = itemView.findViewById(R.id.outgoingMessageTime);
            seenUnseen = itemView.findViewById(R.id.seenUnseen);
        }
    }

    public static class IncomingViewHolder extends RecyclerView.ViewHolder {
        TextView incomingMsg, incomingMsgTime;
        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            incomingMsg = itemView.findViewById(R.id.incomingMessage);
            incomingMsgTime = itemView.findViewById(R.id.incomingMessageTime);
        }
    }

}
