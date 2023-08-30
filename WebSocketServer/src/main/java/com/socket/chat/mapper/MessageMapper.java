package com.socket.chat.mapper;

import com.socket.chat.model.dto.MessagesDto;
import com.socket.chat.model.entity.Messages;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
   MessagesDto toMessagesDTO(Messages messages);
   Messages toMessages(MessagesDto messagesDto);
}
