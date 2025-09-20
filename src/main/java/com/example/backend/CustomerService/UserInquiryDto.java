package com.example.backend.CustomerService;

import com.example.backend.authentication.Role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")  // DB 테이블 이름은 그대로 유지해야 함
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserInquiryDto {

    @Id
    @Column(name = "user_name")
    private String username;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    // 필요시: admin 여부 확인용 헬퍼 메서드
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
