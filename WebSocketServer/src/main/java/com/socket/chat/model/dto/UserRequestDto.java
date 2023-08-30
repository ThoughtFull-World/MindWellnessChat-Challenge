package com.socket.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
   private Long id;
   private String name;
   private String password;
}
