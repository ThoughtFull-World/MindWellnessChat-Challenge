package com.socket.chat.controller;

import com.socket.chat.exceptions.InvalidTokenException;
import com.socket.chat.model.dto.UserRequestDto;
import com.socket.chat.model.dto.UserResponseDto;
import com.socket.chat.services.UserService;
import com.socket.chat.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1")
public class UserController {

   private final UserService userService;
   private final JwtTokenUtil jwtTokenUtil;

   @PostMapping(value = "/user/register")
   public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userResponseDto) {
      return Optional.ofNullable(userService.createUser(userResponseDto)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }

   @PostMapping(value = "/user/login")
   public ResponseEntity<String> loginUser(@RequestBody UserRequestDto userResponseDto) {
      return Optional.ofNullable(userService.loginUser(userResponseDto)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }

   @GetMapping(value = "/user/logout")
   public ResponseEntity<Boolean> logoutUser(@RequestHeader(value = "Authorization") String authorizationToken) {
      if (Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
         throw new InvalidTokenException("Invalid Token");
      }
      return Optional.of(userService.logoutUser(jwtTokenUtil.getUserIdFromToken(authorizationToken))).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }

   @GetMapping(value = "/user")
   public ResponseEntity<Set<UserResponseDto>> getAllUsers(@RequestHeader(value = "Authorization") String authorizationToken, @RequestParam int page, @RequestParam int size) {

      if (Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
         throw new InvalidTokenException("Invalid Token");
      }
      var pageable = PageRequest.of(page - 1, size);
      return Optional.ofNullable(userService.getAllUsers(pageable, Long.valueOf(jwtTokenUtil.getUserIdFromToken(authorizationToken)))).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }

   @GetMapping(value = "/selfUser")
   public ResponseEntity<UserResponseDto> getUsers(@RequestHeader(value = "Authorization") String authorizationToken) {

      if (Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
         throw new InvalidTokenException("Invalid Token");
      }
      return Optional.ofNullable(userService.getUser( Long.valueOf(jwtTokenUtil.getUserIdFromToken(authorizationToken)))).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }

   @PutMapping(value = "/user/lastseen")
   public ResponseEntity<Boolean> updateLastSeen(@RequestHeader(value = "Authorization") String authorizationToken) {

      if (Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
         throw new InvalidTokenException("Invalid Token");
      }
      return Optional.of(userService.updateLastSeen(Long.valueOf(jwtTokenUtil.getUserIdFromToken(authorizationToken)))).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }
   @PostMapping(value = "/user/picture")
   public ResponseEntity<Boolean> uploadPicture(@RequestHeader(value = "Authorization") String authorizationToken, @RequestParam("image") MultipartFile image) {

      if (Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
         throw new InvalidTokenException("Invalid Token");
      }
      return Optional.of(userService.uploadPicture(Long.valueOf(jwtTokenUtil.getUserIdFromToken(authorizationToken)), image)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }
}
