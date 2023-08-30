package com.socket.chat.model.dto;

import com.socket.chat.enums.ContentType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessagesDto {

   private Long id;
   private ContentType contentType;
   private Long senderId;
   private Long receiverId;
   private boolean isRead;
   private Long clientTimeInMS;

}
