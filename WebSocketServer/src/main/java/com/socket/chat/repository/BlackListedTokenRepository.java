package com.socket.chat.repository;

import com.socket.chat.model.entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long> {
   Optional<String> findByToken(String token);
}
