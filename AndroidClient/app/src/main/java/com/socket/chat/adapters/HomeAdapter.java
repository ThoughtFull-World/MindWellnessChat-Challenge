package com.socket.chat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socket.chat.R;
import com.socket.chat.models.UserResponseDto;
import com.socket.chat.util.Utils;

import java.util.List;

public abstract class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<UserResponseDto> userResponseDtoList;
    private Context context;
    public abstract void onClickUser(Long id, String userName, String avatar);
    public HomeAdapter(Context context, List<UserResponseDto> userResponseDtoList) {
        this.userResponseDtoList = userResponseDtoList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, final int position) {
        holder.chatName.setText(userResponseDtoList.get(position).getName());
        try {
            holder.imageContact.setImageBitmap(Utils.base64ToBitmap(userResponseDtoList.get(position).getAvatar()));
        }catch (Exception e) {
            e.printStackTrace();
        }
//        holder.recentTime.setText(Utils.recentChatTimeFormat.format(userResponseDtoList.get(position).getLastSeen()));
        holder.chatUser.setOnClickListener(view -> {
         onClickUser((long) userResponseDtoList.get(position).getId(), userResponseDtoList.get(position).getName(), userResponseDtoList.get(position).getAvatar());
        });

    }

    @Override
    public int getItemCount() {
        return userResponseDtoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageContact;
        TextView chatName;
        TextView recentTime;
        RelativeLayout chatUser;

        public ViewHolder(View convertView) {
            super(convertView);
            imageContact = convertView.findViewById(R.id.imageContact);
            chatName = convertView.findViewById(R.id.chatName);
            recentTime = convertView.findViewById(R.id.recentTime);
            chatUser = convertView.findViewById(R.id.chatUser);
        }
    }

}
