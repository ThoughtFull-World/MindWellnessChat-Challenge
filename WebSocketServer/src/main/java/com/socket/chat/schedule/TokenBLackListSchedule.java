package com.socket.chat.schedule;

import com.socket.chat.services.BlackListTokenService;
import org.springframework.scheduling.annotation.Scheduled;

public class TokenBLackListSchedule {
   BlackListTokenService blackListTokenService;
   @Scheduled(fixedDelay = 86400000)
   public boolean updateBlackListedToken() {
      return blackListTokenService.updateBlackListedToken();
   }
}
