package com.socket.chat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socket.chat.R;
import com.socket.chat.adapters.MessageAdapter;
import com.socket.chat.conf.Const;
import com.socket.chat.enums.MessageStatus;
import com.socket.chat.models.UserResponseDto;
import com.socket.chat.models.message.ChatMessage;
import com.socket.chat.models.message.ChatMessageStatus;
import com.socket.chat.util.DialogManager;
import com.socket.chat.util.JwtUtils;
import com.socket.chat.util.NetworkClient;
import com.socket.chat.util.PreferenceManager;
import com.socket.chat.util.StompUtils;
import com.socket.chat.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class UserChatActivity extends AppCompatActivity {
    private String userId;
    private Long chatUserId;
    private CardView sendButton;
    private TextView receiverName;
    private EditText typingSpace;
    private RecyclerView chatMessageRecyclerView;
    private MessageAdapter messageAdapter;
    private List<ChatMessage> chatMessageList;
    private Context mContext;
    private ImageView profilePicture;
    private ImageView backBtn;
    private Disposable disposable;
    private StompClient stompClient;
    private void init() {
        chatMessageList = new ArrayList<>();
        userId = (String) Objects.requireNonNull(JwtUtils.parseJwtPayload(PreferenceManager.getToken())).get("sub");
        profilePicture = findViewById(R.id.profilePicture);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            endConnection();
        });
        sendButton = findViewById(R.id.sendButton);
        typingSpace = findViewById(R.id.typingSpace);
        receiverName = findViewById(R.id.receiverName);
        chatMessageRecyclerView = findViewById(R.id.chatMessageRecyclerView);
        chatUserId = getIntent().getLongExtra("CHAT_USER_ID", -1);
        receiverName.setText(getIntent().getStringExtra("CHAT_USER_NAME"));
        try {
            profilePicture.setImageBitmap(Utils.base64ToBitmap(Utils.currentChatUserAvatar));
        } catch (Exception e) {
            e.printStackTrace();
        }

        messageAdapter = new MessageAdapter(chatMessageList, this, Long.parseLong(userId)) {
            @Override
            public void chatSeen(ChatMessage chatMessage) {
                updateSeen(chatMessage);
            }
        };
        chatMessageRecyclerView.setAdapter(messageAdapter);
        chatMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        mContext = this;
        this.init();
        manageMessage ();
        getPreviousMessage();
    }
    private void manageMessage () {
        var jwtToken = "Bearer " + PreferenceManager.getToken();
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address+ "?token=" + jwtToken);
        Toast.makeText(this, "Start connecting to server", Toast.LENGTH_SHORT).show();
        stompClient.connect();

        StompUtils.lifecycle(stompClient);

        Log.i(Const.TAG, "Subscribe chat endpoint to receive response");
        disposable = stompClient.topic(Const.chatResponse.replace(Const.placeholder, userId)).subscribe(stompMessage -> {
            var jsonObject = new JSONObject(stompMessage.getPayload());
            Log.i(Const.TAG, "Receive: " + jsonObject.toString());
            runOnUiThread(() -> {
                try {
                    var chatMessage = new Gson().fromJson(jsonObject.getString("response"), ChatMessage.class);
                    if (chatMessage.getMessage() == null) {
                        try {
                            var chatMessageStatus = new Gson().fromJson(jsonObject.getString("response"), ChatMessageStatus.class);
                            updateChatMessageStatus(chatMessageStatus);

                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        chatMessageList.add(chatMessage);
                        messageAdapter.notifyItemInserted(chatMessageList.size() - 1);
                        chatMessageRecyclerView.scrollToPosition(chatMessageList.size() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        sendButton.setOnClickListener(v -> {
            if(StringUtils.length(typingSpace.getText().toString()) > 0) {
                var chatMessage = new ChatMessage();
                chatMessage.setUserID(chatUserId);
                chatMessage.setFromUserID(Long.valueOf(userId));
                chatMessage.setToken(jwtToken);
                chatMessage.setMessage(typingSpace.getText().toString());
                chatMessage.setTime(System.currentTimeMillis());
                chatMessage.setMessageStatus(MessageStatus.NOT_SEEN);
                chatMessage.setClientTimeInMS(System.currentTimeMillis());
                stompClient.send(Const.chat, new Gson().toJson(chatMessage)).subscribe();
                typingSpace.setText("");
                chatMessageList.add(chatMessage);
                messageAdapter.notifyItemInserted(chatMessageList.size() - 1);
                try {
                    chatMessageRecyclerView.scrollToPosition(chatMessageList.size() - 1);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateChatMessageStatus(ChatMessageStatus chatMessageStatus) {
        var pos = 0;
        for(var i = 0; i< chatMessageList.size(); i++) {
            if(chatMessageStatus.getMessageID().equals(chatMessageList.get(i).getId()) || Objects.equals(chatMessageStatus.getClientTimeInMS(), chatMessageList.get(i).getClientTimeInMS())) {
                chatMessageList.get(i).setMessageStatus(chatMessageStatus.getMessageStatus());
                pos = i;
                break;
            }
        }
        messageAdapter.notifyItemChanged(pos);
//        chatMessageRecyclerView.scrollToPosition(chatMessageList.size() - 1);
    }
    private void getPreviousMessage() {
        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address+"/api/v1/message/user?id="+chatUserId+"&page=1&size=1000";
        networkClient.makeGetRequest(apiUrl,
                response -> {
                    var typeToken = new TypeToken<List<ChatMessage>>() {};
                    chatMessageList.addAll(new Gson().fromJson(response, typeToken.getType()));
                    messageAdapter.notifyDataSetChanged();
                    chatMessageRecyclerView.scrollToPosition(chatMessageList.size()-1);
                },
                error -> {
                    Utils.hideProgressBar();
                    DialogManager.showGeneralMessage(mContext, "Somethings went wrong", "Can't parse chat list");
                });
    }
    private void updateSeen(ChatMessage chatMessage) {
        var queue = Volley.newRequestQueue(mContext);
        var networkClient = new NetworkClient(queue);
        var apiUrl = Const.rest_address+"/api/v1/message/user/updateseen?messageId="+chatMessage.getId()+"&messageStatus="+MessageStatus.SEEN;
        networkClient.makePutRequest(apiUrl,
                response -> {},
                error -> {});
    }

    public void endConnection() {
        disposable.dispose();
        stompClient.disconnect();
        finish();
    }

    @Override
    public void onBackPressed() {
        endConnection();
        super.onBackPressed();
    }
}
