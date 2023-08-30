package com.socket.chat.model.dto;

import com.socket.chat.enums.MessageStatus;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long userID;
    private Long fromUserID;
    private String message;
    private Long time;
    private MessageStatus messageStatus;
    private Long clientTimeInMS;
}
