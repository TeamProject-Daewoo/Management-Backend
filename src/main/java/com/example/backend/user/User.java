package com.example.backend.user;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name="users")
@Getter @Setter
public class User {

  @Id
  @Column(name="user_name")
  private String userName;

  @Enumerated(EnumType.STRING)
  @Column(name="approval_status")
  private ApprovalStatus approvalStatus;

  private String email;

  @Column(name="join_date")
  private LocalDateTime joinDate;

  private String name;

  @Column(name="password_hash")
  private String passwordHash;

  @Column(name="phone_number")
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  private Role role;

  public enum ApprovalStatus { APPROVED, PENDING, REJECTED }
  public enum Role { ADMIN, BUSINESS, USER }
}
