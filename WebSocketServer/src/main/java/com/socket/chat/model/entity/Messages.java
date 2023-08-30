package com.socket.chat.model.entity;

import com.socket.chat.enums.ContentType;
import com.socket.chat.enums.MessageStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "Messages")
@Table(name = "messages")
public class Messages {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "content_type", nullable = false)
   private ContentType contentType;

   @Column(name = "message_content")
   private String messageContent;

   @Column(name = "sender_id", nullable = false)
   private Long senderId;

   @Column(name = "receiver_id", nullable = false)
   private Long receiverId;

   @Column(name = "message_status")
   private MessageStatus messageStatus;

   @Column(name = "client_time_in_ms")
   private Long clientTimeInMS;

   @CreationTimestamp
   @Column(name = "created_at")
   private Date createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at", insertable = false)
   private Date updatedAt;

}
