package com.socket.chat.model.entity;

import com.socket.chat.enums.UserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "Users")
@Table(name = "users")
public class Users {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "name", nullable = false)
   private String name;

   @Column(name = "password", nullable = false)
   private String password;
   @Lob // Use this annotation for large text data
   @Column(name = "avatar", columnDefinition = "LONGTEXT")
   private String avatar;

   @Column(name = "user_type")
   private UserType userType = UserType.MEMBER;

   @Column(name = "last_seen")
   private Date lastSeen;
   @CreationTimestamp
   @Column(name = "created_at")
   private Date createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at", insertable = false)
   private Date updatedAt;

}
