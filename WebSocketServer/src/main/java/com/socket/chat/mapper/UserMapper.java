package com.socket.chat.mapper;

import com.socket.chat.model.dto.UserRequestDto;
import com.socket.chat.model.dto.UserResponseDto;
import com.socket.chat.model.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
   UserResponseDto toUserDTO(Users user);
   Users toUser(UserRequestDto userRequestDto);
}
