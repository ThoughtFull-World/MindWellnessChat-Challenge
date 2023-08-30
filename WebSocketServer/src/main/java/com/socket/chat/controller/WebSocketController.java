package com.socket.chat.controller;

import com.socket.chat.exceptions.InvalidTokenException;
import com.socket.chat.model.message.ChatMessage;
import com.socket.chat.services.MessageService;
import com.socket.chat.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {
    private final MessageService messageService;
    private final JwtTokenUtil jwtTokenUtil;

    @MessageMapping("/chat")
    public void chat(ChatMessage chatMessage) {
        if(Boolean.FALSE.equals(jwtTokenUtil.validateToken(chatMessage.getToken()))) {
            throw new InvalidTokenException("Invalid Token");
        }
        if(!String.valueOf(chatMessage.getFromUserID()).equalsIgnoreCase(jwtTokenUtil.getUserIdFromToken(chatMessage.getToken()))) {
            throw new IllegalArgumentException("From user id and token user id not matched ");
        }
        messageService.sendMessageOneToOne(chatMessage);
    }
}
