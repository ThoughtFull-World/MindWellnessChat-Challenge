package com.socket.chat.mapper;

import com.socket.chat.model.dto.ChatMessageDto;
import com.socket.chat.model.dto.MessagesDto;
import com.socket.chat.model.entity.Messages;
import com.socket.chat.model.message.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
   ChatMessageDto toChatMessageDto(ChatMessage messages);
}
