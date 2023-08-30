package com.socket.chat.services.implementation;

import com.socket.chat.repository.BlackListedTokenRepository;
import com.socket.chat.services.BlackListTokenService;
import com.socket.chat.utils.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlackListTokenServiceImpl implements BlackListTokenService {

   BlackListedTokenRepository blackListedTokenRepository;
   JwtTokenUtil jwtTokenUtil;

   @Override
   public boolean updateBlackListedToken() {
      var blackListedTokens = blackListedTokenRepository.findAll();
      for (var blackListedToken : blackListedTokens) {
         var expirationDate = jwtTokenUtil.getExpirationDateFromToken(blackListedToken.getToken());
         var currentTime = new Date();
         if (expirationDate.before(currentTime)) {
            blackListedTokenRepository.delete(blackListedToken);
         }

      }
      return false;
   }
}
