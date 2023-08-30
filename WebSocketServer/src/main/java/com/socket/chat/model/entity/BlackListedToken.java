package com.socket.chat.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "BlackListedToken")
@Table(name = "black_listed_token")
public class BlackListedToken {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "token", nullable = false)
   private String token;


   @CreationTimestamp
   @Column(name = "created_at")
   private Date createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at", insertable = false)
   private Date updatedAt;

}
