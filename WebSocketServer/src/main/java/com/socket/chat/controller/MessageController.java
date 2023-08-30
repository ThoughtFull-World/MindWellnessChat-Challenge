package com.socket.chat.controller;

import com.socket.chat.enums.MessageStatus;
import com.socket.chat.exceptions.InvalidTokenException;
import com.socket.chat.model.dto.MessagesDto;
import com.socket.chat.model.message.ChatMessage;
import com.socket.chat.services.MessageService;
import com.socket.chat.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/message")
public class MessageController {

   private final MessageService messageService;
   private final JwtTokenUtil jwtTokenUtil;

   @GetMapping(value = "/user")
   public ResponseEntity<List<ChatMessage>> getAllChatById(@RequestHeader(value = "Authorization") String authorizationToken, @RequestParam Long id,@RequestParam int page, @RequestParam int size) {

//      if(Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
//         throw new InvalidTokenException("Invalid Token");
//      }
//      if(!jwtTokenUtil.getUserIdFromToken(authorizationToken).equalsIgnoreCase(String.valueOf(id))){
//         throw new IllegalArgumentException("User doesn't match with the token user");
//      }
      var pageable = PageRequest.of(page-1, size);
      return Optional.ofNullable(messageService.getAllChatById(id, Long.valueOf(jwtTokenUtil.getUserIdFromToken(authorizationToken)), pageable)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }

   @PutMapping(value = "/user/updateseen")
   public ResponseEntity<Boolean> updateMessage(@RequestHeader(value = "Authorization") String authorizationToken, @RequestParam Long messageId, @RequestParam MessageStatus messageStatus) {

      if(Boolean.FALSE.equals(jwtTokenUtil.validateToken(authorizationToken))) {
         throw new InvalidTokenException("Invalid Token");
      }
      return Optional.ofNullable(messageService.updateMessageStatus(Long.parseLong(jwtTokenUtil.getUserIdFromToken(authorizationToken)), messageId, messageStatus)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }
}
