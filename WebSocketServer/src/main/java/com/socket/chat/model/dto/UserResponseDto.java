package com.socket.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponseDto {
   private Long id;
   private String name;
   private Date lastSeen;
   private String avatar;
}
