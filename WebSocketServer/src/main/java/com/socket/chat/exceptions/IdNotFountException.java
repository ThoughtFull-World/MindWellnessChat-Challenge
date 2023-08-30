package com.socket.chat.exceptions;

public class IdNotFountException extends  RuntimeException{

   public IdNotFountException(String idNotFound) {
      super(idNotFound);
   }
}
