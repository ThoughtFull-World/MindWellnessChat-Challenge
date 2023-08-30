package com.socket.chat.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class Utils {
   public static String convertToBase64(MultipartFile multipartFile) throws IOException {
      var fileBytes = multipartFile.getBytes();
      var base64Bytes = Base64.getEncoder().encode(fileBytes);
      return new String(base64Bytes);
   }
}
