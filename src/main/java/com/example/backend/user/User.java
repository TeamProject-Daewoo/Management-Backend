package com.example.backend.user;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

  @Id
  @Column(name = "user_name")
  private String userName;

  @Column(name = "join_date")
  private LocalDateTime joinDate;

  @Column(name = "login_type")
  private String loginType;

  @Column(name = "name")
  private String name;

  @Column(name = "password_hash")
  private String passwordHash;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "uuid")
  private String uuid;

  @Enumerated(EnumType.STRING)
  @Column(name = "approval_status")
  private ApprovalStatus approvalStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @Column(name = "business_registration_number")
  private String businessRegistrationNumber;

  public enum ApprovalStatus {
    APPROVED,
    PENDING,
    REJECTED
  }

  public enum Role {
    ADMIN,
    BUSINESS,
    USER
  }
}
