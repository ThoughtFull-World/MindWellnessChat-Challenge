package com.socket.chat.models;

import java.util.Date;

public class UserResponseDto {
   private Long id;
   private String name;
   private Long lastSeen;
   private String avatar;

   public String getAvatar() {
      return avatar;
   }

   public void setAvatar(String avatar) {
      this.avatar = avatar;
   }

   public Long getLastSeen() {
      return lastSeen;
   }

   public void setLastSeen(Long lastSeen) {
      this.lastSeen = lastSeen;
   }
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

}
