package com.socket.chat.repository;

import com.socket.chat.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
   Optional<Users> findByName(String name);

   Page<Users> findAll(Pageable pageable);
}
