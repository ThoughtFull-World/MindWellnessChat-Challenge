package com.socket.chat.model.message;

import com.socket.chat.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long id;
    private Long userID;
    private Long fromUserID;
    private String message;
    private String token;
    private Long time;
    private MessageStatus messageStatus;
    private Long clientTimeInMS;
}
