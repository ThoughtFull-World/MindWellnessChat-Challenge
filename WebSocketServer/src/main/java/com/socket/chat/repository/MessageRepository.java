package com.socket.chat.repository;

import com.socket.chat.model.entity.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {

   //   Page<Messages> findAllBySenderIdOrReceiverId(Long senderId, Long receiverId, Pageable pageable);
   Page<Messages> findAllBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId1, Long receiverId1, Long senderId2, Long receiverId2, Pageable pageable);

   Optional<Messages> findById(Long messageId);

}
