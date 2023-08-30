package com.socket.chat.models.message;


import com.socket.chat.enums.MessageStatus;

public class ChatMessageStatus {

    private Long userID;
    private Long fromUserID;
    private Long messageID;
    private Long clientTimeInMS;

    public Long getClientTimeInMS() {
        return clientTimeInMS;
    }

    public void setClientTimeInMS(Long clientTimeInMS) {
        this.clientTimeInMS = clientTimeInMS;
    }

    private MessageStatus messageStatus;

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

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }
}
