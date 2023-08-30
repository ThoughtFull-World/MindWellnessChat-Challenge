package com.socket.chat.services.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socket.chat.enums.ContentType;
import com.socket.chat.enums.MessageStatus;
import com.socket.chat.exceptions.IdNotFountException;
import com.socket.chat.exceptions.JsonRelatedException;
import com.socket.chat.mapper.ChatMessageMapper;
import com.socket.chat.mapper.MessageMapper;
import com.socket.chat.model.entity.Messages;
import com.socket.chat.model.message.ChatMessage;
import com.socket.chat.model.message.ChatMessageStatus;
import com.socket.chat.model.message.ChatSocketResponse;
import com.socket.chat.repository.MessageRepository;
import com.socket.chat.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

   private final SimpMessagingTemplate simpMessagingTemplate;
   private final MessageRepository messageRepository;
   private final MessageMapper messageMapper;
   private final ObjectMapper objectMapper;
   private final ChatMessageMapper chatMessageMapper;

   @Override
   public Messages saveMessage(ChatMessage chatMessage) {
      var messages = new Messages();
      messages.setSenderId(chatMessage.getFromUserID());
      messages.setMessageContent(chatMessage.getMessage());
      messages.setReceiverId(chatMessage.getUserID());
      messages.setMessageStatus(MessageStatus.DELIVERED);
      messages.setContentType(ContentType.MESSAGE);
      messages.setClientTimeInMS(chatMessage.getClientTimeInMS());
      return messageRepository.save(messages);
   }

   @Override
   public void sendMessageOneToOne(ChatMessage chatMessage) {
      log.info("Receive point-to-point chat message: [" + chatMessage.getFromUserID() + " -> " + chatMessage.getUserID() + ", " + chatMessage.getMessage() + "]");
      try {
         var message = saveMessage(chatMessage);
         chatMessage.setTime(message.getCreatedAt().getTime());
         chatMessage.setId(message.getId());
         var socketString = objectMapper.writeValueAsString(chatMessageMapper.toChatMessageDto(chatMessage));
         var socketResponse = new ChatSocketResponse(socketString);
         simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getUserID()), "/msg", socketResponse);
      }
      catch (JsonProcessingException e) {
         throw new JsonRelatedException(e.getMessage());
      }
   }

   @Override
   public List<ChatMessage> getAllChatById(Long userId, Long selfId, Pageable pageable) {
      var chatMessageList = new ArrayList<ChatMessage>();
      var messagePage = messageRepository.findAllBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(userId, selfId, selfId,userId, pageable);
      for (var message : messagePage.getContent()) {
         var chatMessage = new ChatMessage();
         chatMessage.setId(message.getId());
         chatMessage.setMessage(message.getMessageContent());
         chatMessage.setUserID(message.getReceiverId());
         chatMessage.setFromUserID(message.getSenderId());
         chatMessage.setTime(message.getCreatedAt().getTime());
         chatMessage.setMessageStatus(message.getMessageStatus());
         chatMessage.setClientTimeInMS(message.getClientTimeInMS());
         chatMessageList.add(chatMessage);
      }
      return chatMessageList;
   }

   @Override
   public Boolean updateMessageStatus(Long userId, Long messageId, MessageStatus messageStatus) {
      var message = messageRepository.findById(messageId).orElseThrow(() -> new IdNotFountException("Id not found for id " + messageId));
      message.setMessageStatus(messageStatus);
      messageRepository.save(message);
      var chatMessageStatus = new ChatMessageStatus();
      chatMessageStatus.setMessageID(messageId);
      chatMessageStatus.setFromUserID(message.getSenderId());
      chatMessageStatus.setUserID(message.getReceiverId());
      chatMessageStatus.setMessageStatus(message.getMessageStatus());
      chatMessageStatus.setClientTimeInMS(message.getClientTimeInMS());
      try {
         var socketString = objectMapper.writeValueAsString(chatMessageStatus);
         var socketResponse = new ChatSocketResponse(socketString);
         simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getSenderId()), "/msg", socketResponse);
         return true;
      }
      catch (JsonProcessingException e) {
         throw new JsonRelatedException(e.getMessage());
      }
   }
}
