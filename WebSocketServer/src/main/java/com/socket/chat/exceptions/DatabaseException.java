package com.socket.chat.exceptions;

public class DatabaseException extends  RuntimeException{

   public DatabaseException(String dbException) {
      super(dbException);
   }
}
