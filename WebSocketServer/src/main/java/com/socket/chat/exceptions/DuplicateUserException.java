package com.socket.chat.exceptions;

public class DuplicateUserException extends  RuntimeException{

   public DuplicateUserException(String userAlreadyExist) {
      super(userAlreadyExist);
   }
}
