package com.socket.chat.services;

import com.socket.chat.model.dto.UserRequestDto;
import com.socket.chat.model.dto.UserResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {
   UserResponseDto createUser(UserRequestDto user);
   String loginUser(UserRequestDto user);
   boolean logoutUser(String token);
   Set<UserResponseDto> getAllUsers(Pageable pageable, Long selfId);
   UserResponseDto getUser(Long selfId);
   boolean updateLastSeen(Long id);
   boolean uploadPicture(Long id, MultipartFile image);

}
