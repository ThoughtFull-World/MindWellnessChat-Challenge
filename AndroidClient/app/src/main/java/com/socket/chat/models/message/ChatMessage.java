package com.socket.chat.models.message;


import com.socket.chat.enums.MessageStatus;

public class ChatMessage {
    private Long id;
    private Long userID;
    private Long fromUserID;
    private String message;
    private String token;
    private Long time;
    private MessageStatus messageStatus;

    private Long clientTimeInMS;

    public Long getClientTimeInMS() {
        return clientTimeInMS;
    }

    public void setClientTimeInMS(Long clientTimeInMS) {
        this.clientTimeInMS = clientTimeInMS;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Long fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
