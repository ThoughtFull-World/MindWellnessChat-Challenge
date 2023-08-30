package com.socket.chat.services.implementation;

import com.socket.chat.exceptions.CredentialException;
import com.socket.chat.exceptions.DatabaseException;
import com.socket.chat.exceptions.DuplicateUserException;
import com.socket.chat.exceptions.IdNotFountException;
import com.socket.chat.mapper.UserMapper;
import com.socket.chat.model.dto.UserRequestDto;
import com.socket.chat.model.dto.UserResponseDto;
import com.socket.chat.model.entity.BlackListedToken;
import com.socket.chat.repository.BlackListedTokenRepository;
import com.socket.chat.repository.UserRepository;
import com.socket.chat.services.UserService;
import com.socket.chat.utils.JwtTokenUtil;
import com.socket.chat.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final BlackListedTokenRepository blackListedTokenRepository;
   private final UserMapper userMapper;
   private final JwtTokenUtil jwtTokenUtil;
   private final PasswordEncoder passwordEncoder;
   public UserResponseDto createUser(UserRequestDto userRequestDto) {
      // override the id if it is set by the user to prevent the update
      userRequestDto.setId(null);
      userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
      try {
         var user = userRepository.findByName(userRequestDto.getName());
         if(user.isPresent()) {
            throw new DuplicateUserException("User Already Exist");
         }
         var userToCreate = userMapper.toUser(userRequestDto);
         userToCreate.setLastSeen(new Date());
         return userMapper.toUserDTO(userRepository.save(userToCreate));
      } catch (Exception ex) {
         throw new DatabaseException(ex.getMessage());
      }
   }

   @Override
   public String loginUser(UserRequestDto userRequestDto) {
      var user = userRepository.findByName(userRequestDto.getName()).orElseThrow();
      if(!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
         throw new CredentialException("Password Not matched");
      }
      user.setLastSeen(new Date());
      userRepository.save(user);
      return jwtTokenUtil.generateToken(user);
   }

   @Override
   public boolean logoutUser(String token) {
      try {
         var blackListedToken = new BlackListedToken();
         blackListedToken.setToken(token);
         blackListedTokenRepository.save(blackListedToken);
      } catch (Exception e) {
         throw new DatabaseException(e.getMessage());
      }
      return true;
   }

   @Override
   public Set<UserResponseDto> getAllUsers(Pageable pageable, Long selfId) {
      return userRepository.findAll(pageable).getContent().stream().filter(user -> !user.getId().equals(selfId)).map(userMapper::toUserDTO).collect(Collectors.toSet());
   }

   @Override
   public UserResponseDto getUser(Long selfId) {
      return userMapper.toUserDTO(userRepository.findById(selfId).orElseThrow(()->new IdNotFountException("id not found")));
   }

   @Override
   public boolean updateLastSeen(Long id) {
      var user = userRepository.findById(id).orElseThrow(()->new IdNotFountException("Id not found"));
      user.setLastSeen(new Date());
      userRepository.save(user);
      return true;
   }

   @Override
   public boolean uploadPicture(Long id, MultipartFile image) {
      if (image.isEmpty()) {
         return false; // No image to upload
      }

      try {
         var avatar = Utils.convertToBase64(image);
         var user = userRepository.findById(id).orElseThrow(()->new IdNotFountException("Not found"));
         user.setAvatar(avatar);
         userRepository.save(user);
         return true;
      } catch (Exception e) {
         return false;
      }
   }
}
