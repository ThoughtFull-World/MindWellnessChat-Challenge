package com.socket.chat.enums;

public enum MessageStatus {
   DELIVERED("DELIVERED"),
   NOT_SEEN("NOT_SEEN"),
   SEEN("SEEN");

   private String type;
   MessageStatus(String type) {
      this.type = type;
   }
}
