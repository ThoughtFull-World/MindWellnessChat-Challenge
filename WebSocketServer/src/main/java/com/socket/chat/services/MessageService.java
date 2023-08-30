package com.socket.chat.services;

import com.socket.chat.enums.MessageStatus;
import com.socket.chat.model.entity.Messages;
import com.socket.chat.model.message.ChatMessage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
   Messages saveMessage(ChatMessage chatMessage);
   void sendMessageOneToOne(ChatMessage chatMessage);
   List<ChatMessage> getAllChatById(Long userId,Long selfId,Pageable pageable);
   Boolean updateMessageStatus(Long userId, Long messageId, MessageStatus messageStatus);
}
