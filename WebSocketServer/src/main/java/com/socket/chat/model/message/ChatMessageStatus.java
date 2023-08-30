package com.socket.chat.model.message;

import com.socket.chat.enums.MessageStatus;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageStatus {
    private Long userID;
    private Long fromUserID;
    private Long messageID;
    private MessageStatus messageStatus;
    private Long clientTimeInMS;
}
